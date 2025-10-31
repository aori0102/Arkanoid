package game.Player;

import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
import game.PowerUp.StatusEffect;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * The player's handler for power ups.
 */
public class PlayerPowerUpHandler extends MonoBehaviour {

    private static final int DOUBLE_MULTIPLE = 2;
    private static final int TRIPLE_MULTIPLE = 3;
    private static final double EXPLOSION_MAX_DURATION = 5.0;
    private static final int EXPLOSION_MAX_HIT = 3;
    private static final double PADDLE_EXPANSION_SCALE = 1.5;
    private static final double SHIELD_MAX_DURATION = 5.0;
    private static final int HEAL_AMOUNT = 20;

    /**
     * Upon called, all {@link game.GameObject.Ball} duplicate itself
     * by the multiple provided within the event argument.
     */
    public EventHandler<Integer> onDuplicateBallRequested = new EventHandler<>(PlayerPowerUpHandler.class);

    /**
     * Upon called, all {@link game.GameObject.Ball} triplicate itself
     * by the multiple provided within the event argument.
     */
    public EventHandler<Integer> onTriplicateBallRequested = new EventHandler<>(PlayerPowerUpHandler.class);

    /**
     * Upon called, all {@link game.GameObject.Ball} turns into explosive
     * with the constraints provided.
     */
    public EventHandler<OnBallToExplosiveEventArgs> onBallToExplosiveRequested = new EventHandler<>(PlayerPowerUpHandler.class);

    /**
     * Event arguments for {@link #onBallToExplosiveRequested}.
     *
     * @param maxDuration The maximum duration in seconds before the ball turns normal.
     * @param maxHit      The maximum hit before the ball turns normal.
     */
    public record OnBallToExplosiveEventArgs(double maxDuration, int maxHit) {
    }

    /**
     * Upon called, the {@link PlayerPaddle} modify its scale
     * by the amount provided within the event argument.
     */
    public EventHandler<Double> onPaddleScaleChangeRequested = new EventHandler<>(PlayerPowerUpHandler.class);

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

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerPowerUpHandler(GameObject owner) {
        super(owner);
    }

    private void apply(PowerUp powerUp) {

        switch (powerUp.getPowerUpIndex()) {

            case None -> throw new RuntimeException("Cannot process " + PowerUpIndex.None);
            case DuplicateBall -> onDuplicateBallRequested
                    .invoke(this, DOUBLE_MULTIPLE);
            case TriplicateBall -> onTriplicateBallRequested
                    .invoke(this, TRIPLE_MULTIPLE);
            case Explosive -> onBallToExplosiveRequested
                    .invoke(this, new OnBallToExplosiveEventArgs(EXPLOSION_MAX_DURATION, EXPLOSION_MAX_HIT));
            case Expand -> onPaddleScaleChangeRequested
                    .invoke(this, PADDLE_EXPANSION_SCALE);
            case Shield -> onShieldSpawnRequested
                    .invoke(this, SHIELD_MAX_DURATION);
            case FireBall -> onFireBallRequested
                    .invoke(this, StatusEffect.Burn);
            case Blizzard ->  onBlizzardBallRequested
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


    @Override
    public void awake() {
        Player.getInstance().getPlayerPaddle().onPowerUpConsumed.addListener(this::paddle_onPowerUpConsumed);
    }

}
