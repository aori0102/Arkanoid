package game.GameObject;

import game.Brick.Brick;
import game.Voltraxis.Interface.IBossTarget;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;
import utils.Vector2;
import utils.Time;

public class Ball extends MonoBehaviour {

    private static final int BALL_DAMAGE = 360;

    private Vector2 direction;
    private double ballSpeed = 500;
    private BoxCollider ballCollider;
    private Paddle paddle;
    private Vector2 offsetVector = new Vector2(0.5, 0.5);
    private Vector2 offsetBallPosition;

    private boolean isMoving = false;

    public Ball(GameObject owner) {
        super(owner);
        owner.setLayer(Layer.Ball);
    }

    /**
     * Assign collider specs and function to other aspects.
     */
    public void awake() {

        // Assign collider specs and the function
        ballCollider = getComponent(BoxCollider.class);
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
            isMoving = true;
            paddle.isFired = true;
        });

        // Off set ball position when it follows the paddle in order to make it is in the surface
        offsetBallPosition = new Vector2(-ballCollider.getLocalSize().x / 2, -20);
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
            getTransform().translate(direction.normalize().multiply(ballSpeed * Time.deltaTime));
        }

        if (getTransform().getGlobalPosition().y > 1000) {
            GameObjectManager.destroy(getGameObject());
        }
    }

    private void handleCollision(CollisionData collisionData) {
        var boss = collisionData.otherCollider.getComponent(IBossTarget.class);
        if (boss != null) {
            boss.takeDamage(BALL_DAMAGE);
            return;
        }
        var brick = collisionData.otherCollider.getComponent(Brick.class);
        if (brick != null) {
            brick.decreaseHealth(BALL_DAMAGE);
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
        var normal = collisionData.hitNormal.normalize().multiply(2.0);

        // Dot product of the ball's direction with the surface
        double dotCoefficient = Vector2.dot(direction.normalize(), normal.normalize());

        // Check if the ball is perpendicular with the surface
        boolean nearlyParallel = (dotCoefficient <= 1 && dotCoefficient >= 0.95) ||
                (dotCoefficient >= -1 && dotCoefficient <= -0.95);

        // Reflect direction
        Vector2 reflectDir = normal.add(direction.normalize());

        // If the ball's direction is perpendicular then adding the offset vector to it in order to avoid horizontal movement
        if (nearlyParallel) {
            reflectDir = reflectDir.add(offsetVector.normalize());
        }

        // If the ball interacts with the moving paddle, the reflected direction will be different from the motionless paddle,
        // and it will be calculated by adding the moving vector to the reflected direction
        if (isCollidedWith(collisionData, Paddle.class) && !paddle.movementVector.equals(Vector2.zero())) {
            reflectDir = reflectDir.add(paddle.movementVector.normalize());
        }


        direction = reflectDir;
    }

    private void handleBrickCollision(CollisionData collisionData) {
    }

    /**
     * Set the ball's direction.
     *
     * @param direction : the direction we want the ball to follow.
     */
    public void setDirection(Vector2 direction) {
        this.direction = direction;
        paddle.onMouseReleased.removeAllListeners();
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
     * Set the paddle to listen to its event.
     *
     * @param paddle : invoked event's position.
     */
    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    /**
     * Check if the ball is collided with the expected object.
     *
     * @param collisionData : the collision data of the object.
     * @param type          : the desired type.
     * @return true if the object matches with the desired type.
     */
    private boolean isCollidedWith(CollisionData collisionData, Class type) {
        GameObject collidedObject = collisionData.otherCollider.getGameObject();
        return collidedObject.getComponent(type) != null;
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }


}
