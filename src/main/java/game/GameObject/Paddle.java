package game.GameObject;

import game.Obstacle.Index.ObstacleManager;
import game.PowerUp.Index.PowerUp;
import javafx.scene.input.MouseButton;
import org.*;
import utils.Time;
import utils.Vector2;


import javafx.scene.input.MouseEvent;

public class Paddle extends MonoBehaviour {
    //The constant specs of the ball
    private final double DOT_LIMIT_ANGLE_RIGHT = 30;
    private final double DOT_LIMIT_ANGLE_LEFT = 150;
    private final double STUNNED_TIME = 3.6;

    //Event
    public EventHandler<Vector2> onMouseReleased = new EventHandler<Vector2>(this);
    public EventHandler<PowerUp> onPowerUpConsumed = new EventHandler<>(this);

    private ActionMap actionMap;
    private PlayerInput playerInput;
    private BoxCollider boxCollider;
    private Arrow arrow;
    private Vector2 fireDirection = new Vector2();

    private boolean canInvoke;
    private boolean isMoving = true;
    private boolean canStartStunnedCounter = false;
    private boolean canReduceSpeed = true;
    private double stunnedCounter = 0;
    private double basePaddleSpeed = 1000;
    private double currentSpeed = 1000;

    public boolean isFired = false;

    public Vector2 movementVector = new Vector2(0, 0);
    private Vector2 directionVector = new Vector2(1, 0);

    public Paddle(GameObject owner) {
        super(owner);
        owner.setLayer(Layer.Paddle);
    }

    /**
     * Initialize paddle specs.
     */
    public void awake() {
        // Assign components
        actionMap = gameObject.getComponent(ActionMap.class);
        boxCollider = gameObject.getComponent(BoxCollider.class);
        playerInput = gameObject.getComponent(PlayerInput.class);

        //Assign collider specs
        boxCollider.setLocalCenter(new Vector2(0, 0));
        boxCollider.setLocalSize(new Vector2(80, 20));

        //Assign line specs
        boxCollider.setOnTriggerEnter(this::onTriggerEnter);

        ObstacleManager.instance.onPaddleCollidedWithObstacle.addListener((e, voi) -> {
            canStartStunnedCounter = true;
        });

    }

    public void update() {
        handleMovement();
        handleCollisionWithObstacles();
    }


    /**
     * Handle the movement of the paddle.
     * Using Action defined in Action map to decide move direction.
     */
    public void handleMovement() {
        if (!isMoving) {
            return;
        }

        getTransform().translate(movementVector);
        // Current action
        switch (actionMap.currentAction) {
            // Go left
            case GoLeft -> movementVector = new Vector2(-currentSpeed * Time.deltaTime, 0);
            // Go Right
            case GoRight -> movementVector = new Vector2(currentSpeed * Time.deltaTime, 0);
            // Adjust ray direction by pressing left mouse button
            case MousePressed -> {
                HandleRayDirection();
            }
            default -> {
                // Set the movement vector to zero to stop the paddle
                movementVector = new Vector2(0, 0);
                arrow.turnOff();
                //Fire the ball if the ball is not fired
                if (playerInput.isMouseReleased) {
                    if (isDirectionValid(fireDirection)) {
                        if (canInvoke) {
                            onMouseReleased.invoke(this, fireDirection);
                            playerInput.isMouseReleased = false;
                        }
                    }
                }
            }
        }
    }

    /**
     * Handle the direction ray.
     */
    private void HandleRayDirection() {
        if (isFired) {
            return;
        }

        // If the mouse input is left button, then turn on the line and calculate the fire direction as well as the line's end point
        if (playerInput.getMouseEvent(MouseButton.PRIMARY) != null) {
            // Get mouse event
            MouseEvent mouseEvent = playerInput.getMouseEvent(MouseButton.PRIMARY);

            // Get mouse position
            Vector2 mousePos = new Vector2(mouseEvent.getX(), mouseEvent.getY());

            if (mousePos.y < getTransform().getGlobalPosition().y) {
                canInvoke = false;
                return;
            } else {
                canInvoke = true;
            }

            // The direction we want the ball to follow
            Vector2 expectedDirection = ((getTransform().getGlobalPosition()
                    .subtract(mousePos)).normalize());
            // If the direction is in the range, then assigning it to fire direction
            if (isDirectionValid(expectedDirection)) {
                fireDirection = expectedDirection;
                arrow.turnOn();
                double angle = Math.toDegrees(Vector2.angle(fireDirection.normalize(), directionVector));
                arrow.handleArrowDirection(angle);
            }

        }
    }
    /**
     * Handle the movement of the paddle when an obstacle hits it. Its speed will be
     * reduced by 10 times when hit
     */
    private void handleCollisionWithObstacles() {
        if (!canStartStunnedCounter) return;
        stunnedCounter += Time.deltaTime;

        if (canReduceSpeed) {
            currentSpeed /= 10;
            canReduceSpeed = false;
        }

        if (stunnedCounter >= STUNNED_TIME) {
            currentSpeed = basePaddleSpeed;
            canReduceSpeed = true;
            stunnedCounter = 0;
            canStartStunnedCounter = false;
        }
    }


    /**
     * Check if the direction is in the valid range.
     * @param direction : the fire direction.
     * @return true if the direction is valid.
     */
    private boolean isDirectionValid(Vector2 direction) {
        if (direction == null) return false;
        double angle = Math.toDegrees(Vector2.angle(direction.normalize(), directionVector.normalize()));
        return angle >= DOT_LIMIT_ANGLE_RIGHT &&  angle <= DOT_LIMIT_ANGLE_LEFT;
    }

    /**
     * Trigger the event when the paddle consumes a power up.
     * It will invoke an event which is listened by player
     * @param collisionData : the collision data of the power up
     */
    private void onTriggerEnter(CollisionData collisionData) {

        var powerUp = collisionData.otherCollider.getComponent(PowerUp.class);
        if (powerUp != null) {
            onPowerUpConsumed.invoke(this, powerUp);
        }

    }

    /**
     * Link the arrow
     * @param arrow: linked arrow
     */
    public void linkArrow(Arrow arrow) {
        this.arrow = arrow;
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }
}