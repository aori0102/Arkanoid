package game.object;

import org.*;
import utils.Vector2;
import utils.Time;

public class Ball extends MonoBehaviour {

    private Vector2 direction;
    private double ballSpeed = 500;
    private BoxCollider ballCollider;
    private Paddle paddle;
    private Vector2 offsetVector = new Vector2(0.5, 0.5);

    private boolean isMoving = false;

    public Ball(GameObject owner) {
        super(owner);
    }

    /**
     * Assign collider specs and function to other aspects.
     */
    public void awake() {

        // Assign collider specs and the function
        ballCollider = getComponent(BoxCollider.class);
        ballCollider.setLocalCenter(new Vector2(0, 0));
        ballCollider.setLocalSize(new Vector2(16, 20));
        ballCollider.setOnCollisionEnterCallback(e -> {
            handleAngleDirection(e);

        });

        // Add listener to paddle event
        paddle.onMouseReleased.addListener((sender, e) -> {
            setDirection(e);
            isMoving = true;
            paddle.isFired = true;
        });
    }

    public void update() {
        handleMovement();
    }

    /**
     * Handle ball movement.
     */
    public void handleMovement() {
        // If the ball cannot move, then return.
        if (!isMoving) return;

        // Make the ball follow the paddle position if player haven't fired it
        if (!paddle.isFired) {
            getTransform().setGlobalPosition(paddle.getTransform().getGlobalPosition());
        } else {
            getTransform().translate(direction.normalize().multiply(ballSpeed * Time.deltaTime));
        }
    }

    /**
     * Calculating the direction of the ball.
     *
     * @param collisionData : the collision data of the interacted surface.
     */
    public void handleAngleDirection(CollisionData collisionData) {
        if (!isMoving) return;

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
        if (isPaddleCollided(collisionData) && !paddle.movementVector.equals(Vector2.zero())) {
            reflectDir = reflectDir.add(paddle.movementVector.normalize());
        }

        direction = reflectDir;
    }


    /**
     * Set the ball's direction.
     *
     * @param direction : the direction we want the ball to follow.
     */
    private void setDirection(Vector2 direction) {
        this.direction = direction;
        paddle.onMouseReleased.removeAllListeners();
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
     * Check if the ball is collided with the paddle
     *
     * @param collisionData : data of the collided object.
     * @return true if the object is the paddle.
     */
    private boolean isPaddleCollided(CollisionData collisionData) {
        GameObject collidedObject = collisionData.otherCollider.getGameObject();
        return collidedObject.getComponent(Paddle.class) != null;
    }


    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }
}
