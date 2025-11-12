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
 * BlizzardBall is a type of PowerUp that applies the FrostBite status effect
 * to all balls in the game. When applied, it makes balls deal extra damage
 * or have additional effects defined by FrostBite.
 *
 * After applying its effect, the power-up destroys itself.
 */
public class BlizzardBall extends PowerUp {

    // Event ID for listening to BlizzardBall requests
    private EventActionID blizzardBallEventActionID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The GameObject that owns this component.
     */
    public BlizzardBall(GameObject owner) {
        super(owner);
    }

    /**
     * Initialize the BlizzardBall power-up.
     * Sets the PowerUpIndex to Blizzard.
     */
    public void awake() {
        setPowerUpIndex(PowerUpIndex.Blizzard);
    }

    /**
     * Handle the application of the Blizzard effect to all balls.
     *
     * @param statusEffect The status effect to apply (FrostBite).
     */
    public void handleOnBlizzardBallRequested(StatusEffect statusEffect) {
        BallsManager.getInstance().applyStatusPowerUpEffect(statusEffect);
    }

    /**
     * Called when the power-up is applied.
     * Applies the FrostBite effect to all balls and destroys this GameObject.
     */
    @Override
    public void onApplied() {
        handleOnBlizzardBallRequested(StatusEffect.FrostBite);
        GameObjectManager.destroy(gameObject);
    }

}
