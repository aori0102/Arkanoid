package game.Player;

import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
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
    private static final double SHIELD_MAX_DURATION = 7.0;

    /**
     * Upon called, all {@link game.GameObject.Ball} duplicate itself
     * by the multiple provided within the event argument.
     */
    public EventHandler<Integer> onDuplicateBallRequested = new EventHandler<>(this);

    /**
     * Upon called, all {@link game.GameObject.Ball} triplicate itself
     * by the multiple provided within the event argument.
     */
    public EventHandler<Integer> onTriplicateBallRequested = new EventHandler<>(this);

    /**
     * Upon called, all {@link game.GameObject.Ball} turns into explosive
     * with the constraints provided.
     */
    public EventHandler<OnBallToExplosiveEventArgs> onBallToExplosiveRequested = new EventHandler<>(this);

    /**
     * Event arguments for {@link #onBallToExplosiveRequested}.
     *
     * @param maxDuration The maximum duration in seconds before the ball turns normal.
     * @param maxHit      The maximum hit before the ball turns normal.
     */
    public record OnBallToExplosiveEventArgs(double maxDuration, int maxHit) {
    }

    /**
     * Upon called, the {@link game.GameObject.Paddle} modify its scale
     * by the amount provided within the event argument.
     */
    public EventHandler<Double> onPaddleScaleChangeRequested = new EventHandler<>(this);

    /**
     * Upon called, the {@link Player} creates a shield with the duration
     * as provided within the event argument.
     */
    public EventHandler<Double> onShieldSpawnRequested = new EventHandler<>(this);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerPowerUpHandler(GameObject owner) {
        super(owner);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {
        onDuplicateBallRequested = null;
    }

    protected void apply(PowerUp powerUp) {

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

        }

    }

}
