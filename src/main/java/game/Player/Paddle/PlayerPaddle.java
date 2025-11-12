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

import javafx.scene.input.MouseEvent;

public class PlayerPaddle extends MonoBehaviour {

    // The constant specs of the ball
    private static final double STUNNED_MOVEMENT_SPEED_MULTIPLIER = 0.1;
    private static final double DOT_LIMIT_ANGLE_RIGHT = 30;
    private static final double DOT_LIMIT_ANGLE_LEFT = 150;
    private static final double PADDLE_MAX_MOVABLE_AMPLITUDE = 680.0;
    private static final double STUN_TIME = 3.2;
    private static final Vector2 DIRECTION_VECTOR = new Vector2(1, 0);

    private final PaddleHealth paddleHealth = addComponent(PaddleHealth.class);
    private final PlayerStat playerStat = addComponent(PlayerStat.class);
    private final BoxCollider boxCollider = addComponent(BoxCollider.class);
    private final PaddleEffectController paddleEffectController = addComponent(PaddleEffectController.class);

    private Time.CoroutineID stunnedCoroutineID;
    private PaddleDashParticle paddleDashParticle;

    private boolean canInvoke;
    private Arrow arrow;
    private Vector2 fireDirection = new Vector2();
    public boolean isFired = false;

    public EventHandler<Vector2> onMouseReleased = new EventHandler<>(PlayerPaddle.class);
    public static EventHandler<PowerUp> onAnyPowerUpConsumed = new EventHandler<>(PlayerPaddle.class);

    private Vector2 movementVector = Vector2.zero();

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
    public void update() {
        handlePowerUps();
        clampPaddlePositioning();
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
                    onAnyPowerUpConsumed.invoke(this, powerUp);
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

    public PlayerStat getPaddleStat() {
        return playerStat;
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
                    .multiply(playerStat.getActualMovementSpeed() * Time.getDeltaTime());
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

    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param paddleDashParticle .
     */
    public void linkPaddleDashParticle(PaddleDashParticle paddleDashParticle) {
        this.paddleDashParticle = paddleDashParticle;
    }

    public PaddleDashParticle getPaddleDashParticle() {
        return paddleDashParticle;
    }

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