package game.PowerUp;

import game.Effect.StatusEffect;
import game.GameObject.BallsManager;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
import game.PowerUp.Index.PowerUpManager;
import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;

public class FireBall extends PowerUp {

    private EventActionID fireBallEventActionID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public FireBall(GameObject owner) {
        super(owner);
    }

    public void awake() {
        setPowerUpIndex(PowerUpIndex.FireBall);
    }

    public void start() {
    }

    private void handleOnFireBallRequested(StatusEffect statusEffect) {
        BallsManager.getInstance().applyStatusPowerUpEffect(statusEffect);
    }

    @Override
    public void onApplied() {
        handleOnFireBallRequested(StatusEffect.Burn);
        GameObjectManager.destroy(gameObject);
    }
}
