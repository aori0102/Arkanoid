package game.Ball;

import game.Brick.Brick;
import game.Effect.StatusEffect;
import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
import game.Player.Player;
import game.Player.Paddle.PlayerPaddle;
import org.Annotation.LinkViaPrefab;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.ParticleSystem.Emitter.ConeEmitter;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Random;
import utils.Vector2;
import utils.Time;

// TODO: Doc
public class Ball extends MonoBehaviour {

    private static final double NEAR_PARALLEL_THRESHOLD = 0.1;
    private static final double BASE_BALL_SPEED = 500;

    private final BallDamageDealer ballDamageDealer = addComponent(BallDamageDealer.class);
    private final BallEffectController ballEffectController = addComponent(BallEffectController.class);

    private Vector2 direction = Vector2.zero();
    private PlayerPaddle playerPaddle;
    private boolean hitPaddle = false;

    @LinkViaPrefab
    private ConeEmitter coneEmitter = null;

    private EventActionID ball_onAnyBallHitBrick_ID = null;
    private EventActionID ballEffectController_onEffectInflicted_ID = null;
    private EventActionID ballEffectController_onEffectCleared_ID = null;

    public static EventHandler<Void> onAnyBallHitBrick = new EventHandler<>(Ball.class);
    public static EventHandler<Void> onAnyBallJustHitPaddle = new EventHandler<>(Ball.class);
    public static EventHandler<Void> onAnyBallDestroyed = new EventHandler<>(Ball.class);

    public Ball(GameObject owner) {
        super(owner);
        addComponent(BoxCollider.class).setOnCollisionEnterCallback((data) -> {
            handleHitHandle(data);
            handleCollision(data);
        });
    }

    /**
     * Assign collider specs and function to other aspects.
     */
    @Override
    public void awake() {
        playerPaddle = Player.getInstance().getPlayerPaddle();

        // Add listener to paddle event
        playerPaddle.onMouseReleased.addListener((e, vector2) -> {
            setDirection(vector2);
            playerPaddle.isFired = true;
        });

        ballEffectController_onEffectInflicted_ID = ballEffectController.onEffectInflicted
                .addListener(this::ballEffectController_onEffectInflicted);
        ballEffectController_onEffectCleared_ID = ballEffectController.onEffectCleared
                .addListener(this::ballEffectController_onEffectCleared);
    }

    @Override
    public void start() {
        ball_onAnyBallHitBrick_ID = Ball.onAnyBallHitBrick
                .addListener(this::ball_onAnyBallHitBrick);
    }

    @Override
    public void update() {
        handleMovement();
    }

    @Override
    protected void onDestroy() {
        if (BallsManager.getInstance() != null) {
            BallsManager.getInstance().removeBall(this);
        }
        ballEffectController.onEffectInflicted.removeListener(ballEffectController_onEffectInflicted_ID);
        ballEffectController.onEffectCleared.removeListener(ballEffectController_onEffectCleared_ID);
        coneEmitter.stopEmit();
        Ball.onAnyBallHitBrick.removeListener(ball_onAnyBallHitBrick_ID);
        onAnyBallDestroyed.invoke(this, null);
    }

    /**
     * Link ball particle<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link BallPrefab}
     * as part of component linking process.</i></b>
     *
     * @param coneEmitter Particle object to link.
     */
    public void linkBallParticle(ConeEmitter coneEmitter) {
        this.coneEmitter = coneEmitter;
    }

    public BallDamageDealer getBallDamageDealer() {
        return ballDamageDealer;
    }

    public BallEffectController getBallEffectController() {
        return ballEffectController;
    }

    public boolean isHitPaddle() {
        return hitPaddle;
    }

    /**
     * Called when {@link Ball#onAnyBallHitBrick} is invoked.<br><br>
     * This function resets {@link #hitPaddle} when any other ball hits a brick.
     *
     * @param sender Event caller {@link Ball}.
     * @param e      Empty event argument.
     */
    private void ball_onAnyBallHitBrick(Object sender, Void e) {
        hitPaddle = false;
    }

    /**
     * Handle ball movement.
     */
    public void handleMovement() {
        // Make the ball follow the paddle position if player haven't fired it
        if (!playerPaddle.isFired && direction.equals(Vector2.zero())) {
            getTransform().setGlobalPosition(playerPaddle.getTransform()
                    .getGlobalPosition().add(Vector2.up().multiply(20)));
            coneEmitter.stopEmit();
        }
        // Moving the ball
        else {
            coneEmitter.startEmit();
            getTransform().translate(direction.normalize().multiply(BASE_BALL_SPEED * Time.getDeltaTime())); // die
        }

    }

    /**
     *
     * @param collisionData
     */
    private void handleCollision(CollisionData collisionData) {
        if (isCollidedWith(collisionData, Border.class)) {
            var border = collisionData.otherCollider.getComponent(Border.class);
            if (border != null && border.getBorderType() == BorderType.BorderBottom) {
                GameObjectManager.destroy(gameObject);
            }
        } else if (isCollidedWith(collisionData, Brick.class)) {
            onAnyBallHitBrick.invoke(this, null);
        } else if (isCollidedWith(collisionData, PlayerPaddle.class)) {
            if (!hitPaddle) {
                hitPaddle = true;
                onAnyBallJustHitPaddle.invoke(this, null);
            }
        }
    }

    /**
     *
     * @param collisionData
     */
    private void handleHitHandle(CollisionData collisionData) {
        direction = collisionData.hitNormal.normalize().multiply(2.0).add(direction.normalize());
        var paddle = collisionData.otherCollider.getComponent(PlayerPaddle.class);
        if (paddle != null) {
            direction = direction.normalize().add(paddle.getMovementVector().normalize().multiply(0.2));
        }
        direction = direction.normalize();
        if (Vector2.dot(direction, Vector2.left()) < NEAR_PARALLEL_THRESHOLD) {
            direction = direction.rotateBy(Random.range(-0.019, 0.078));
        } else if (Vector2.dot(direction, Vector2.right()) < NEAR_PARALLEL_THRESHOLD) {
            direction = direction.rotateBy(Random.range(-0.078, 0.019));
        }
        coneEmitter.setBaseDirection(direction.inverse());
    }

    /**
     * Set the ball's direction.
     *
     * @param direction : the direction we want the ball to follow.
     */
    public void setDirection(Vector2 direction) {
        this.direction = direction;
        Player.getInstance().getPlayerPaddle().onMouseReleased.removeAllListeners();
    }

    /**
     * Get the ball's direction
     *
     * @return the ball's direction
     */
    public Vector2 getDirection() {
        return direction;
    }

    /**
     *
     * @return
     */
    public SpriteRenderer getBallVisual() {
        for (var child : getTransform().getChildren()) {
            if (child.getComponent(SpriteRenderer.class) != null) {
                return child.getComponent(SpriteRenderer.class);
            }
        }
        return null;
    }

    /**
     * Check if the ball is collided with the expected object.
     *
     * @param collisionData : the collision data of the object.
     * @param type          : the desired type.
     * @return true if the object matches with the desired type.
     */
    private boolean isCollidedWith(CollisionData collisionData, Class<?> type) {
        return collisionData.otherCollider.getComponent(type) != null;
    }

    /**
     * Called when {@link BallEffectController#onEffectInflicted} is invoked.<br><br>
     * This function changes the ball visual when an effect is inflicted.
     *
     * @param sender Event caller {@link BallEffectController}.
     * @param e      Event argument indicating the effect that was inflicted.
     */
    private void ballEffectController_onEffectInflicted(Object sender, StatusEffect e) {
        if (getBallVisual() != null) {
            switch (e) {

                case FrostBite -> {
                    getBallVisual().setImage(ImageAsset.ImageIndex.BlizzardBall.getImage());
                }

                case Burn -> {
                    getBallVisual().setImage(ImageAsset.ImageIndex.FireBall.getImage());
                }

            }
        }
    }

    /**
     * Called when {@link BallEffectController#onEffectCleared} is invoked.<br><br>
     * This function resets the ball's visual when effect is removed.
     *
     * @param sender Event caller {@link BallEffectController}.
     * @param e      Event argument indicating the effect that was removed.
     */
    private void ballEffectController_onEffectCleared(Object sender, StatusEffect e) {
        getBallVisual().setImage(ImageAsset.ImageIndex.Ball.getImage());
    }

}