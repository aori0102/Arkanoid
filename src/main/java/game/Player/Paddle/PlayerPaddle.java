package game.Player.Paddle;

import game.Effect.StatusEffect;
import game.GameManager.LevelState;
import game.GameObject.Arrow;
import game.Level.LevelManager;
import game.Player.Player;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Recovery;
import javafx.scene.input.MouseButton;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Physics.PhysicsManager;
import utils.Time;
import utils.Vector2;

import javafx.scene.input.MouseEvent;

public class PlayerPaddle extends MonoBehaviour {

    // The constant specs of the ball
    private static final double DOT_LIMIT_ANGLE_RIGHT = 30;
    private static final double DOT_LIMIT_ANGLE_LEFT = 150;
    private static final Vector2 DIRECTION_VECTOR = new Vector2(1, 0);

    private final PaddleHealth paddleHealth = addComponent(PaddleHealth.class);
    private final PaddleStat paddleStat = addComponent(PaddleStat.class);
    private final BoxCollider boxCollider = addComponent(BoxCollider.class);
    private final PaddleEffectController paddleEffectController = addComponent(PaddleEffectController.class);

    private Time.CoroutineID stunnedCoroutineID;

    private Arrow arrow;
    private Vector2 fireDirection = new Vector2();

    private boolean canInvoke;

    // Event
    public EventHandler<Vector2> onMouseReleased = new EventHandler<Vector2>(PlayerPaddle.class);
    public static EventHandler<PowerUp> onPowerUpConsumed = new EventHandler<>(PlayerPaddle.class);

    public boolean isFired = false;
    private boolean canBeDamaged = true;

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
        paddleEffectController.onEffectInflicted.addListener(this::paddleEffectController_onEffectInflicted);

        Player.getInstance().getPlayerController().getActionMap().
                onKeyHeld.addListener(this::handlePaddleMovement);
        Player.getInstance().getPlayerController().getActionMap().
                onMouseHeld.addListener(this::handleRayDirection);
        Player.getInstance().getPlayerController().getActionMap().
                onMouseReleased.addListener(this::handleRayReleased);
    }

    @Override
    public void start() {
        paddleHealth.onPaddleHealthReachesZero
                .addListener(this::playerPaddleHealth_onPaddleHealthReachesZero);
    }

    @Override
    public void update() {
        handlePowerUps();
    }

    @Override
    protected void onDestroy() {
        Time.removeCoroutine(stunnedCoroutineID);
    }

    private void handlePowerUps() {

        var overlapCollider = PhysicsManager.getOverlapColliders(boxCollider, true);
        for (var other : overlapCollider) {
            var powerUp = other.getComponent(PowerUp.class);
            if (powerUp != null) {
                if (powerUp instanceof Recovery || isFired) {
                    onPowerUpConsumed.invoke(this, powerUp);
                }
            }
        }

    }

    /**
     * Called when {@link PaddleHealth#onPaddleHealthReachesZero} is invoked.<br><br>
     * This function destroys the paddle when paddle's health reaches zero.
     *
     * @param sender Event caller {@link PaddleHealth}.
     * @param e      Empty event argument.
     */
    private void playerPaddleHealth_onPaddleHealthReachesZero(Object sender, Void e) {
        GameObjectManager.destroy(gameObject);
    }

    /**
     * Get the paddle's current movement vector.
     *
     * @return The paddle's current movement vector.
     */
    public Vector2 getMovementVector() {
        return movementVector;
    }

    public PaddleHealth getPaddleHealth() {
        return paddleHealth;
    }

    public PaddleStat getPaddleStat() {
        return paddleStat;
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
        if (LevelManager.getInstance().getLevelState() != LevelState.Playing) {
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
        if (isFired || LevelManager.getInstance().getLevelState() != LevelState.Playing) {
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
     * Link the arrow
     *
     * @param arrow: linked arrow
     */
    public void linkArrow(Arrow arrow) {
        this.arrow = arrow;
    }


    private void paddleEffectController_onEffectInflicted(Object o, StatusEffect statusEffect) {
        if (!canBeDamaged) {
            return;
        }

        if (statusEffect == StatusEffect.Stunned) {
            int currentSpeedSpeed = Player.getInstance().getCurrentSpeed();
            Player.getInstance().setCurrentSpeed(currentSpeedSpeed /= 10);
            stunnedCoroutineID = Time.addCoroutine(this::resetPaddleSpeed, Time.getTime() + 3);
        }
    }

    private void resetPaddleSpeed() {
        Player.getInstance().setCurrentSpeed(Player.getInstance().getBaseSpeed());
        Time.removeCoroutine(stunnedCoroutineID);
    }

    public boolean canBeDamaged() {
        return canBeDamaged;
    }

    public void setCanBeDamaged(boolean canBeDamaged) {
        this.canBeDamaged = canBeDamaged;
    }
}