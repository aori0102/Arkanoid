package game.PowerUp;

import game.Effect.StatusEffect;
import game.Ball.BallsManager;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
import game.PowerUp.Index.PowerUpManager;
import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;

public class BlizzardBall extends PowerUp {

    private EventActionID blizzardBallEventActionID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BlizzardBall(GameObject owner) {
        super(owner);
    }

    public void awake() {
        setPowerUpIndex(PowerUpIndex.Blizzard);
    }

    public void handleOnBlizzardBallRequested(StatusEffect statusEffect) {
        BallsManager.getInstance().applyStatusPowerUpEffect(statusEffect);
    }

    @Override
    public void onApplied() {
        handleOnBlizzardBallRequested(StatusEffect.FrostBite);
        GameObjectManager.destroy(gameObject);
    }

}
