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
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;
import org.Layer.Layer;
import org.Main;
import org.Physics.BoxCollider;
import org.Physics.PhysicsManager;
import utils.Time;
import utils.Vector2;

/**
 * Represents the player's paddle in the game.
 * <p>
 * This class handles the paddle's movement, interaction with power-ups,
 * arrow aiming, firing logic, and reaction to status effects.
 * </p>
 */
public class PlayerPaddle extends MonoBehaviour {

    // --- Constants for directional limits when firing ---
    private static final double DOT_LIMIT_ANGLE_RIGHT = 30;
    private static final double DOT_LIMIT_ANGLE_LEFT = 150;
    private static final Vector2 DIRECTION_VECTOR = new Vector2(1, 0); // reference vector for angle calculation

    // --- Stun effect constants ---
    private static final double STUN_TIME = 3.2;
    private static final double STUNNED_MOVEMENT_SPEED_MULTIPLIER = 0.1;

    // --- Paddle movement constraint ---
    private static final double PADDLE_MAX_MOVABLE_AMPLITUDE = 650.0;

    // --- Components attached to this paddle ---
    private final PaddleHealth paddleHealth = addComponent(PaddleHealth.class);
    private final PlayerStat playerStat = addComponent(PlayerStat.class);
    private final BoxCollider boxCollider = addComponent(BoxCollider.class);
    private final PaddleEffectController paddleEffectController = addComponent(PaddleEffectController.class);

    private Time.CoroutineID stunnedCoroutineID; // Coroutine ID for handling stun effect duration
    private PaddleDashParticle paddleDashParticle; // linked dash particle effect

    private Arrow arrow; // arrow visual for aiming
    private Vector2 fireDirection = new Vector2(); // direction vector for firing

    private boolean canInvoke; // if the paddle can fire
    public boolean isFired = false; // indicates if a shot has been fired

    private Vector2 movementVector = new Vector2(0, 0); // current movement vector

    // --- Events ---
    public EventHandler<Vector2> onMouseReleased = new EventHandler<>(PlayerPaddle.class);
    public EventHandler<PowerUp> onPowerUpConsumed = new EventHandler<>(PlayerPaddle.class);

    /**
     * Create a PlayerPaddle MonoBehaviour and set its layer.
     */
    public PlayerPaddle(GameObject owner) {
        super(owner);
        owner.setLayer(Layer.Paddle);
    }

    /**
     * Initialization of paddle.
     * <p>
     * Sets up event listeners for status effects, key/mouse input.
     * </p>
     */
    @Override
    public void awake() {
        // Listen for effects applied to paddle
        paddleEffectController.onEffectInflicted.addListener(this::paddleEffectController_onEffectInflicted);

        // Listen for player input
        var actionMap = Player.getInstance().getPlayerController().getActionMap();
        actionMap.onKeyHeld.addListener(this::handlePaddleMovement);
        actionMap.onMouseHeld.addListener(this::handleRayDirection);
        actionMap.onMouseReleased.addListener(this::handleRayReleased);
    }

    /**
     * Update called every frame.
     * Handles interactions with power-ups on the paddle.
     */
    @Override
    public void update() {
        handlePowerUps();
        clampPaddlePositioning();
    }

    @Override
    protected void onDestroy() {
        Time.removeCoroutine(stunnedCoroutineID);
    }

    // --- Power-up handling ---
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

    private void clampPaddlePositioning() {
        var paddleSize = boxCollider.getGlobalSize();
        var position = getTransform().getGlobalPosition();
        var movableAmplitudeEachSide = (PADDLE_MAX_MOVABLE_AMPLITUDE - paddleSize.x) / 2.0;
        position.x = Math.clamp(
                position.x,
                Main.STAGE_WIDTH / 2.0 - movableAmplitudeEachSide,
                Main.STAGE_WIDTH / 2.0 + movableAmplitudeEachSide
        );
        getTransform().setGlobalPosition(position);
    }

    /**
     * Return the current movement vector of the paddle.
     */
    public Vector2 getMovementVector() {
        return movementVector;
    }

    public PaddleHealth getPaddleHealth() {
        return paddleHealth;
    }

    public PlayerStat getPaddleStat() {
        return playerStat;
    }

    // --- Input Handling ---

    /**
     * Handles left/right movement via key input.
     */
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
                    .multiply(playerStat.getActualMovementSpeed() * Time.getDeltaTime());
        }

        getTransform().translate(movementVector);
    }

    /**
     * Called when mouse is released, triggers firing event.
     */
    private void handleRayReleased(Object e, ActionMap.Action action) {
        if (LevelManager.getInstance().getLevelState() != LevelState.Playing) return;

        arrow.turnOff();
        if (isDirectionValid(fireDirection) && canInvoke) {
            onMouseReleased.invoke(this, fireDirection);
            Player.getInstance().getPlayerController().getPlayerInput().isMouseReleased = false;
        }
    }

    /**
     * Handles arrow aiming based on mouse position.
     */
    private void handleRayDirection(Object e, ActionMap.Action action) {
        if (isFired || LevelManager.getInstance().getLevelState() != LevelState.Playing) return;

        var mouseEvent = Player.getInstance().getPlayerController().getPlayerInput().getMouseEvent(MouseButton.PRIMARY);
        if (mouseEvent == null) return;

        Vector2 mousePos = new Vector2(mouseEvent.getX(), mouseEvent.getY());

        // Can only fire downward
        canInvoke = mousePos.y >= getTransform().getGlobalPosition().y;
        if (!canInvoke) return;

        Vector2 expectedDirection = (getTransform().getGlobalPosition().subtract(mousePos)).normalize();
        if (isDirectionValid(expectedDirection)) {
            fireDirection = expectedDirection;
            arrow.turnOn();
            double angle = Math.toDegrees(Vector2.angle(fireDirection.normalize(), DIRECTION_VECTOR));
            arrow.handleArrowDirection(angle);
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

    // --- Linking external components ---

    public void linkArrow(Arrow arrow) {
        this.arrow = arrow;
    }

    public void linkPaddleDashParticle(PaddleDashParticle paddleDashParticle) {
        this.paddleDashParticle = paddleDashParticle;
    }

    public PaddleDashParticle getPaddleDashParticle() {
        return paddleDashParticle;
    }

    // --- Status Effects ---

    private void paddleEffectController_onEffectInflicted(Object o, StatusEffect statusEffect) {
        if (statusEffect == StatusEffect.Stunned) {
            playerStat.setStatMultiplier(
                    PlayerStat.PlayerStatIndex.MovementSpeed,
                    playerStat.getMovementSpeedMultiplier() + STUNNED_MOVEMENT_SPEED_MULTIPLIER
            );
            stunnedCoroutineID = Time.addCoroutine(this::resetPaddleSpeed, STUN_TIME);
        }
    }

    private void resetPaddleSpeed() {
        playerStat.setStatMultiplier(
                PlayerStat.PlayerStatIndex.MovementSpeed,
                playerStat.getMovementSpeedMultiplier() - STUNNED_MOVEMENT_SPEED_MULTIPLIER
        );
    }

}
