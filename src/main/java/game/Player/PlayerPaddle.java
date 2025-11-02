package game.Player;

import game.GameManager.GameManager;
import game.GameManager.GameState;
import game.GameObject.Arrow;
import game.Obstacle.Index.ObstacleManager;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Recovery;
import javafx.scene.input.MouseButton;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;
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
    private static final Vector2 DIRECTION_VECTOR = new Vector2(1, 0);

    //Event
    public EventHandler<Vector2> onMouseReleased = new EventHandler<Vector2>(PlayerPaddle.class);
    public EventHandler<PowerUp> onPowerUpConsumed = new EventHandler<>(PlayerPaddle.class);

    private Arrow arrow;
    private Vector2 fireDirection = new Vector2();

    private boolean canInvoke;
    private boolean canStartStunnedCounter = false;
    private boolean canReduceSpeed = true;
    private double stunnedCounter = 0;

    public boolean isFired = false;

    private Vector2 movementVector = new Vector2(0, 0);

    public PlayerPaddle(GameObject owner) {
        super(owner);
        owner.setLayer(Layer.Paddle);
    }

    /**
     * Initialize paddle specs.
     */
    @Override
    public void awake() {
        // Assign components
        BoxCollider boxCollider = gameObject.getComponent(BoxCollider.class);
        //Assign line specs
        boxCollider.setOnTriggerEnterCallback(this::onTriggerEnter);

        ObstacleManager.getInstance().onPaddleCollidedWithObstacle.addListener((e, voi) -> {
            canStartStunnedCounter = true;
        });

        Player.getInstance().getPlayerController().getActionMap().
                onKeyHeld.addListener(this::handlePaddleMovement);
        Player.getInstance().getPlayerController().getActionMap().
                onKeyReleased.addListener((_, action) ->{
                });
        Player.getInstance().getPlayerController().getActionMap().
                onMouseHeld.addListener(this::handleRayDirection);
        Player.getInstance().getPlayerController().getActionMap().
                onMouseReleased.addListener(this::handleRayReleased);
    }

    @Override
    public void update() {
        handleCollisionWithObstacles();
    }

    public Vector2 getMovementVector() {
        return movementVector;
    }

    private void handlePaddleMovement(Object e, ActionMap.Action action) {

        movementVector = new Vector2(0, 0);

        switch (action) {
            case GoLeft -> movementVector = movementVector.add(new Vector2(-1, 0));
            case GoRight -> movementVector = movementVector.add(new Vector2(1, 0));
            default -> {
            }
        }

        if (!movementVector.equals(Vector2.zero())) {
            movementVector = movementVector.normalize()
                    .multiply(Player.getInstance().getCurrentSpeed() * Time.getDeltaTime());
        }

        getTransform().translate(movementVector);
    }

    private void handleRayReleased(Object e, ActionMap.Action action) {
        if (GameManager.getInstance().getGameState() != GameState.Playing) {
            return;
        }
        arrow.turnOff();
        if (isDirectionValid(fireDirection) && canInvoke) {
            onMouseReleased.invoke(this, fireDirection);
            Player.getInstance().getPlayerController().getPlayerInput().isMouseReleased = false;
        }
    }

    /**
     * Handle the direction ray.
     */
    private void handleRayDirection(Object e, ActionMap.Action action) {
        if (isFired || GameManager.getInstance().getGameState() != GameState.Playing) {
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
        stunnedCounter += Time.getDeltaTime();

        if (canReduceSpeed) {
            int currentSpeed = Player.getInstance().getCurrentSpeed() / 10;
            Player.getInstance().setCurrentSpeed(currentSpeed);
            canReduceSpeed = false;
        }

        if (stunnedCounter >= STUNNED_TIME) {
            Player.getInstance().setCurrentSpeed(Player.getInstance().getBaseSpeed());
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

        var powerUp = collisionData.otherCollider.getComponent(PowerUp.class);

        if (powerUp instanceof Recovery) {
            onPowerUpConsumed.invoke(this, powerUp);
        } else {
            if (!isFired) {
                return;
            }

            if (powerUp != null) {
                onPowerUpConsumed.invoke(this, powerUp);
            }
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
}