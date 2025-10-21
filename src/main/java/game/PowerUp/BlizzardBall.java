package game.PowerUp;

import game.GameObject.BallsManager;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
import game.PowerUp.Index.PowerUpManager;
import org.GameObject.GameObject;

public class BlizzardBall extends PowerUp {

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

    public void start() {
        PowerUpManager.instance.onBlizzardBall.addListener((sender, powerEffect) -> {
            handleOnBlizzardBallRequested(powerEffect);
        });
    }

    public void handleOnBlizzardBallRequested(StatusEffect statusEffect) {
        BallsManager.instance.applyStatusPowerUpEffect(statusEffect);
    }
}
