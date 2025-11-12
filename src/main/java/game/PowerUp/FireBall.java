package game.PowerUp;

import game.Effect.StatusEffect;
import game.Ball.BallsManager;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
import game.PowerUp.Index.PowerUpManager;
import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;

/**
 * FireBall is a power-up that applies a burning effect to all balls.
 *
 * When applied, all existing balls will gain the Burn status effect, which
 * typically causes them to deal additional damage over time or have special interactions.
 * After applying the effect, the power-up destroys itself.
 */
public class FireBall extends PowerUp {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The GameObject that owns this component.
     */
    public FireBall(GameObject owner) {
        super(owner);
    }

    /**
     * Initialize the FireBall power-up.
     * Sets the PowerUpIndex to FireBall.
     */
    public void awake() {
        setPowerUpIndex(PowerUpIndex.FireBall);
    }

    /**
     * Apply the FireBall effect to all balls by adding the Burn status effect.
     *
     * @param statusEffect The status effect to apply (Burn in this case).
     */
    private void handleOnFireBallRequested(StatusEffect statusEffect) {
        BallsManager.getInstance().applyStatusPowerUpEffect(statusEffect);
    }

    /**
     * Called when the power-up is applied.
     * Applies the Burn effect to all balls and then destroys this power-up GameObject.
     */
    @Override
    public void onApplied() {
        handleOnFireBallRequested(StatusEffect.Burn);
        GameObjectManager.destroy(gameObject);
    }
}
