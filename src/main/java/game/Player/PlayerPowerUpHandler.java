package game.Player;

import game.Ball.Ball;
import game.Player.Paddle.PlayerPaddle;
import game.PowerUp.Index.PowerUp;
import game.Effect.StatusEffect;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * The player's handler for power ups.
 */
public class PlayerPowerUpHandler extends MonoBehaviour {

    private static final int DOUBLE_MULTIPLE = 2;
    private static final int TRIPLE_MULTIPLE = 3;
    private static final double SHIELD_MAX_DURATION = 5.0;
    private static final int HEAL_AMOUNT = 20;

    private EventActionID paddle_onPowerUpConsumed_ID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerPowerUpHandler(GameObject owner) {
        super(owner);
    }

    @Override
    public void start() {
        paddle_onPowerUpConsumed_ID = Player.getInstance().getPlayerPaddle().onPowerUpConsumed
                .addListener(this::paddle_onPowerUpConsumed);
    }

    @Override
    protected void onDestroy() {
        try {
            Player.getInstance().getPlayerPaddle().onPowerUpConsumed
                    .removeListener(paddle_onPowerUpConsumed_ID);
        } catch (NullPointerException _) {
        }
    }

    /**
     * Upon called, all {@link Ball} duplicate itself
     * by the multiple provided within the event argument.
     */
    public EventHandler<Integer> onDuplicateBallRequested = new EventHandler<>(PlayerPowerUpHandler.class);

    /**
     * Upon called, all {@link Ball} triplicate itself
     * by the multiple provided within the event argument.
     */
    public EventHandler<Integer> onTriplicateBallRequested = new EventHandler<>(PlayerPowerUpHandler.class);

    /**
     * Upon called, the {@link Player} creates a shield with the duration
     * as provided within the event argument.
     */
    public EventHandler<Double> onShieldSpawnRequested = new EventHandler<>(PlayerPowerUpHandler.class);

    /**
     * Upon called, the {@code Opponent} will be attached the
     * {@code Burn} effect, which will deal damage each time in
     * a specific amount of time.
     */
    public EventHandler<StatusEffect> onFireBallRequested = new EventHandler<>(PlayerPowerUpHandler.class);

    /**
     * Upon called, the {@code Opponent} will be attached the
     * {@code FrostBite} effect, which will make the enemy takes more damage
     * when hit by ball or other damaged resources.
     */
    public EventHandler<StatusEffect> onBlizzardBallRequested = new EventHandler<>(PlayerPowerUpHandler.class);

    public EventHandler<Integer> onRecoveryRequested = new EventHandler<>(PlayerPowerUpHandler.class);

    private void apply(PowerUp powerUp) {

        switch (powerUp.getPowerUpIndex()) {

            case null -> throw new RuntimeException("Cannot process null power up");
            case DuplicateBall -> onDuplicateBallRequested
                    .invoke(this, DOUBLE_MULTIPLE);
            case TriplicateBall -> onTriplicateBallRequested
                    .invoke(this, TRIPLE_MULTIPLE);
            case Shield -> onShieldSpawnRequested
                    .invoke(this, SHIELD_MAX_DURATION);
            case FireBall -> onFireBallRequested
                    .invoke(this, StatusEffect.Burn);
            case Blizzard -> onBlizzardBallRequested
                    .invoke(this, StatusEffect.FrostBite);
            case Recovery -> onRecoveryRequested
                    .invoke(this, HEAL_AMOUNT);

        }

    }

    /**
     * Called when a power up triggers against this player's
     * registered paddle.
     *
     * @param sender The caller, {@link PlayerPaddle}.
     * @param e      The power up that was triggered.
     */
    private void paddle_onPowerUpConsumed(Object sender, PowerUp e) {
        apply(e);
        e.onApplied();
    }

}