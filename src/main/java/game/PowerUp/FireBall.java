package game.PowerUp;

import game.GameObject.BallsManager;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
import game.PowerUp.Index.PowerUpManager;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public class FireBall extends PowerUp {
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
        PowerUpManager.instance.onFireBall.addListener((sender, fireBall) -> {
            handleOnFireBallRequested(fireBall);
        });
    }

    private void handleOnFireBallRequested(StatusEffect statusEffect) {
        BallsManager.instance.applyStatusPowerUpEffect(statusEffect);
    }

    @Override
    protected void destroyComponent() {

    }
}
