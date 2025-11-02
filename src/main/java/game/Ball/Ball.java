package game.Ball;

import game.Brick.Brick;
import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
import game.Player.Player;
import game.Player.Paddle.PlayerPaddle;
import game.Effect.StatusEffect;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Random;
import utils.Vector2;
import utils.Time;

public class Ball extends MonoBehaviour {

    private static final double BASE_BALL_SPEED = 500;
    private static final Vector2 BOUNCE_OFFSET = new Vector2(0.2, 0.2);

    private Vector2 direction = Vector2.zero();
    private PlayerPaddle playerPaddle;
    private StatusEffect currentStatusEffect = StatusEffect.None;
    private StatusEffect pendingEffect = StatusEffect.None;
    private boolean hitPaddle = false;

    private EventActionID ball_onAnyBallHitBrick_ID = null;

    public static EventHandler<Void> onAnyBallHitBrick = new EventHandler<>(Ball.class);
    public static EventHandler<Void> onAnyBallJustHitPaddle = new EventHandler<>(Ball.class);
    public static EventHandler<Void> onAnyBallDestroyed = new EventHandler<>(Ball.class);

    public Ball(GameObject owner) {
        super(owner);
        owner.setLayer(Layer.Ball);
    }

    /**
     * Assign collider specs and function to other aspects.
     */
    @Override
    public void awake() {
        playerPaddle = Player.getInstance().getPlayerPaddle();

        // Assign collider specs and the function
        var ballCollider = getComponent(BoxCollider.class);
        ballCollider.setExcludeLayer(Layer.Ball.getUnderlyingValue());
        ballCollider.setLocalCenter(new Vector2(0, 0));
        ballCollider.setLocalSize(new Vector2(20, 16));
        ballCollider.setOnCollisionEnterCallback((data) -> {
            handleHitHandle(data);
            handleCollision(data);
        });

        // Add listener to paddle event
        playerPaddle.onMouseReleased.addListener((e, vector2) -> {
            setDirection(vector2);
            playerPaddle.isFired = true;
        });

        if (pendingEffect != StatusEffect.None) {
            addEffect(pendingEffect);
            pendingEffect = StatusEffect.None;
        }

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
        //TODO: let ball manager listen to event
        BallsManager.getInstance().removeBall(this);
        Ball.onAnyBallHitBrick.removeListener(ball_onAnyBallHitBrick_ID);
        onAnyBallDestroyed.invoke(this, null);
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
        if (!playerPaddle.isFired && direction == null) {
            getTransform().setGlobalPosition(playerPaddle.getTransform().getGlobalPosition());
        }
        // Moving the ball
        else {
            getTransform().translate(direction.normalize().multiply(BASE_BALL_SPEED * Time.getDeltaTime()));
        }

    }

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

    private void handleHitHandle(CollisionData collisionData) {
        direction = collisionData.hitNormal.normalize().multiply(2.0).add(direction.normalize());
        var paddle = collisionData.otherCollider.getComponent(PlayerPaddle.class);
        if (paddle != null) {
            direction = direction.normalize().add(paddle.getMovementVector().normalize().multiply(0.2));
        }
        direction = direction.normalize();
        if (Vector2.dot(direction, Vector2.left()) < 0.1) {
            direction = direction.rotateBy(Random.range(-0.019, 0.078));
        } else if (Vector2.dot(direction, Vector2.right()) < 0.1) {
            direction = direction.rotateBy(Random.range(-0.078, 0.019));
        }
    }

    /**
     * Calculating the direction of the ball.<br>
     * The reflected direction is calculated by the formular : <br>
     * {@code r = d - 2 * (d,n) * n} <br>
     * In that formular: <br>
     * r is reflected direction <br>
     * d is the previous direction <br>
     * n is the normal vector (base on which side the ball interacts with) <br>
     * If the direction is vertical with the surface, the reflected direction will be added <br>
     * an offset vector to avoid stuck.
     *
     * @param collisionData : the hit object's collision data
     */
    private void handleAngleDirection(CollisionData collisionData) {
        if (direction == null) return;

        Vector2 normal = collisionData.hitNormal.normalize();

        if (Math.abs(normal.x) > Math.abs(normal.y)) {
            normal = new Vector2(Math.signum(normal.x), 0);
        } else {
            normal = new Vector2(0, Math.signum(normal.y));
        }

        Vector2 dirNorm = direction.normalize();

        double dot = Vector2.dot(dirNorm, normal);
        Vector2 reflectDirection = dirNorm.subtract(normal.multiply(2 * dot)).normalize();

        double dotCoefficient = Vector2.dot(dirNorm, normal);

        boolean nearlyParallel = dotCoefficient == 1;

        if (nearlyParallel) {
            reflectDirection = reflectDirection.add(BOUNCE_OFFSET.multiply(0.3).normalize());
        }

        if (isCollidedWith(collisionData, PlayerPaddle.class)) {
            Vector2 paddleVel = playerPaddle.getMovementVector();
            if (!paddleVel.equals(Vector2.zero())) {
                reflectDirection = reflectDirection.add(paddleVel.normalize().multiply(0.3)).normalize();
            }
        }

        Vector2 offset = getTransform().getGlobalPosition().add(normal.inverse().multiply(1));
        getTransform().setGlobalPosition(offset);

        direction = reflectDirection;
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

    public void changeBallVisual() {
        if (getBallVisual() != null) {
            switch (currentStatusEffect) {
                case FrostBite -> {
                    getBallVisual().setImage(ImageAsset.ImageIndex.BlizzardBall.getImage());
                }
                case Burn -> {
                    getBallVisual().setImage(ImageAsset.ImageIndex.FireBall.getImage());
                }

                default -> {
                    getBallVisual().setImage(ImageAsset.ImageIndex.Ball.getImage());
                }
            }
        }
    }

    public void addEffect(StatusEffect statusEffect) {
        this.currentStatusEffect = statusEffect;
        changeBallVisual();
    }

    public void setPendingEffect(StatusEffect statusEffect) {
        this.pendingEffect = statusEffect;
    }

    public StatusEffect getCurrentStatusEffect() {
        return currentStatusEffect;
    }

}