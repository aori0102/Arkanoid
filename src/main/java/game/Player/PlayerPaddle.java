package game.Player;

import game.GameObject.Arrow;
import game.Obstacle.Index.ObstacleManager;
import game.PowerUp.Index.PowerUp;
import javafx.scene.input.MouseButton;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;
import org.InputAction.PlayerInput;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;
import utils.Time;
import utils.Vector2;


import javafx.scene.input.MouseEvent;

public class PlayerPaddle extends MonoBehaviour {
    //The constant specs of the ball
    private static final double DOT_LIMIT_ANGLE_RIGHT = 30;
    private static final double DOT_LIMIT_ANGLE_LEFT = 150;
    private static final double STUNNED_TIME = 3.6;
    private static final double BASE_PADDLE_SPEED = 1000;
    private static final Vector2 DIRECTION_VECTOR = new Vector2(1, 0);


    //Event
    public EventHandler<Vector2> onMouseReleased = new EventHandler<Vector2>(PlayerPaddle.class);
    public EventHandler<PowerUp> onPowerUpConsumed = new EventHandler<>(PlayerPaddle.class);

    private Arrow arrow;
    private Vector2 fireDirection = new Vector2();

    private boolean canInvoke;
    private boolean isMoving = true;
    private boolean canStartStunnedCounter = false;
    private boolean canReduceSpeed = true;
    private double stunnedCounter = 0;
    private double currentSpeed = 1000;

    public boolean isFired = false;

    public Vector2 movementVector = new Vector2(0, 0);

    public PlayerPaddle(GameObject owner) {
        super(owner);
        owner.setLayer(Layer.Paddle);
    }

    /**
     * Initialize paddle specs.
     */
    public void awake() {
        // Assign components
        BoxCollider boxCollider = gameObject.getComponent(BoxCollider.class);

        //Assign collider specs
        boxCollider.setLocalCenter(new Vector2(0, 0));
        boxCollider.setLocalSize(new Vector2(64, 16));

        //Assign line specs
        boxCollider.setOnTriggerEnter(this::onTriggerEnter);

        ObstacleManager.getInstance().onPaddleCollidedWithObstacle.addListener((e, voi) -> {
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
        if (!isMoving) return;

        movementVector = new Vector2(0, 0);

        for (ActionMap.Action action : Player.getInstance().getPlayerController().getActions()) {
            switch (action) {
                case GoLeft -> movementVector = movementVector.add(new Vector2(-1, 0));
                case GoRight -> movementVector = movementVector.add(new Vector2(1, 0));
                case MousePressed -> HandleRayDirection();
                default -> {}
            }
        }

        if (Player.getInstance().getPlayerController().getActions().isEmpty()) {
            arrow.turnOff();

            if (Player.getInstance().getPlayerController().getPlayerInput().isMouseReleased) {
                if (isDirectionValid(fireDirection) && canInvoke) {
                    onMouseReleased.invoke(this, fireDirection);
                    Player.getInstance().getPlayerController().getPlayerInput().isMouseReleased = false;
                }
            }
        }

        if (!movementVector.equals(Vector2.zero())) {
            movementVector = movementVector.normalize()
                    .multiply(currentSpeed * Time.deltaTime);
        }

        getTransform().translate(movementVector);
    }


    /**
     * Handle the direction ray.
     */
    private void HandleRayDirection() {
        if (isFired) {
            return;
        }

        // If the mouse input is left button, then turn on the line and calculate the fire direction as well as the line's end point
        if (Player.getInstance().getPlayerController().
                getPlayerInput().getMouseEvent(MouseButton.PRIMARY) != null) {
            // Get mouse event
            MouseEvent mouseEvent = Player.getInstance().getPlayerController().
                    getPlayerInput().getMouseEvent(MouseButton.PRIMARY);

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
                double angle = Math.toDegrees(Vector2.angle(fireDirection.normalize(), DIRECTION_VECTOR));
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
            currentSpeed = BASE_PADDLE_SPEED;
            canReduceSpeed = true;
            stunnedCounter = 0;
            canStartStunnedCounter = false;
        }
    }


    /**
     * Check if the direction is in the valid range.
     *
     * @param direction : the fire direction.
     * @return true if the direction is valid.
     */
    private boolean isDirectionValid(Vector2 direction) {
        if (direction == null) return false;
        double angle = Math.toDegrees(Vector2.angle(direction.normalize(), DIRECTION_VECTOR.normalize()));
        return angle >= DOT_LIMIT_ANGLE_RIGHT && angle <= DOT_LIMIT_ANGLE_LEFT;
    }

    /**
     * Trigger the event when the paddle consumes a power up.
     * It will invoke an event which is listened by player
     *
     * @param collisionData : the collision data of the power up
     */
    private void onTriggerEnter(CollisionData collisionData) {

        if (!isFired) {
            return;
        }
        var powerUp = collisionData.otherCollider.getComponent(PowerUp.class);
        if (powerUp != null) {
            onPowerUpConsumed.invoke(this, powerUp);
        }

    }

    /**
     * Link the arrow
     *
     * @param arrow: linked arrow
     */
    public void linkArrow(Arrow arrow) {
        this.arrow = arrow;
    }

    @Override
    protected void destroyComponent() {

    }
}