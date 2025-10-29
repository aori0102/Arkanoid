package game.GameObject;

import game.Brick.Brick;
import game.Player.Player;
import game.Player.PlayerPaddle;
import game.PowerUp.StatusEffect;
import game.Voltraxis.Interface.ITakePlayerDamage;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;
import utils.Time;

// TODO: ur code stinky af man - Aori to Kine.

public class Ball extends MonoBehaviour {

    private static final int BALL_DAMAGE = 80;
    private static final double BASE_BALL_SPEED = 500;
    private static final Vector2 BOUNCE_OFFSET = new Vector2(0.2, 0.2);

    private Vector2 direction;
    private PlayerPaddle paddle;
    private Vector2 offsetBallPosition;
    private StatusEffect currentStatusEffect = StatusEffect.None;
    private StatusEffect pendingEffect = StatusEffect.None;

    public Ball(GameObject owner) {
        super(owner);
        owner.setLayer(Layer.Ball);
    }

    /**
     * Assign collider specs and function to other aspects.
     */
    public void awake() {
        paddle = Player.getInstance().getPlayerPaddle();

        // Assign collider specs and the function
        var ballCollider = getComponent(BoxCollider.class);
        ballCollider.setExcludeLayer(Layer.Ball.getUnderlyingValue());
        ballCollider.setLocalCenter(new Vector2(0, 0));
        ballCollider.setLocalSize(new Vector2(20, 16));
        ballCollider.setOnCollisionEnterCallback(e -> {
            handleAngleDirection(e);
            handleCollision(e);
        });

        // Add listener to paddle event
        paddle.onMouseReleased.addListener((e, vector2) -> {
            setDirection(vector2);
            paddle.isFired = true;
        });

        // Off set ball position when it follows the paddle in order to make it is in the surface
        offsetBallPosition = new Vector2(0, 0);

        if (pendingEffect != StatusEffect.None) {
            addEffect(pendingEffect);
            pendingEffect = StatusEffect.None;
        }

    }

    public void update() {
        handleMovement();
    }

    /**
     * Handle ball movement.
     */
    public void handleMovement() {
        // Make the ball follow the paddle position if player haven't fired it
        if (!paddle.isFired) {
            getTransform().setGlobalPosition(paddle.getTransform().getGlobalPosition().add(offsetBallPosition));
        }
        // Moving the ball
        else {
            getTransform().translate(direction.normalize().multiply(BASE_BALL_SPEED * Time.deltaTime));
        }

        if (getTransform().getGlobalPosition().y > 1000) {
            GameObjectManager.destroy(getGameObject());
        }
    }

    private void handleCollision(CollisionData collisionData) {
        if (isCollidedWith(collisionData, ITakePlayerDamage.class)) {
            var target = collisionData.otherCollider.getComponent(ITakePlayerDamage.class);
            if (target != null) {
                if (target instanceof Brick brick) {
                    if (currentStatusEffect != StatusEffect.None) {
                        brick.setStatusBrickEffect(currentStatusEffect);
                        currentStatusEffect = StatusEffect.None;
                        changeBallVisual();
                    }
                }
                target.takeDamage(BALL_DAMAGE);
            }
        }
    }

    /**
     * Calculating the direction of the ball.
     *
     * @param collisionData : the collision data of the interacted surface.
     */
    private void handleAngleDirection(CollisionData collisionData) {

        if (direction == null) return;

        // Normal vector to calculate reflect direction
        var normal = collisionData.hitNormal.normalize();

        // Dot product of the ball's direction with the surface
        double dotCoefficient = Vector2.dot(direction.normalize(), normal.normalize());

        // Check if the ball is perpendicular with the surface
        boolean nearlyParallel = (dotCoefficient <= 1 && dotCoefficient >= 0.95) ||
                (dotCoefficient >= -1 && dotCoefficient <= -0.95);

        // Reflect direction
        Vector2 reflectDir = normal.add(normal).add(direction.normalize());

        // If the ball's direction is perpendicular then adding the offset vector to it in order to avoid horizontal movement
        if (nearlyParallel) {
            reflectDir = reflectDir.add(BOUNCE_OFFSET.normalize());
        }

        // If the ball interacts with the moving paddle, the reflected direction will be different from the motionless paddle,
        // and it will be calculated by adding the moving vector to the reflected direction
        if (isCollidedWith(collisionData, PlayerPaddle.class)) {;;
            if (Math.abs(normal.y) > Math.abs(normal.x)
                    && !paddle.movementVector.equals(Vector2.zero())
                    && !reflectDir.add(paddle.movementVector.normalize()).equals(Vector2.zero())) {
                reflectDir = reflectDir.add(paddle.movementVector.normalize());
            }

        }

        direction = reflectDir.normalize();
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
        for(var child : getTransform().getChildren()) {
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
        GameObject collidedObject = collisionData.otherCollider.getGameObject();
        return collidedObject.getComponent(type) != null;
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
