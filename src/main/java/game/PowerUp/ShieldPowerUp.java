package game.PowerUp;

import game.GameObject.Shield;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
import game.PowerUp.Index.PowerUpManager;
import org.GameObject.GameObject;

public class ShieldPowerUp extends PowerUp {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ShieldPowerUp(GameObject owner) {
        super(owner);
        setPowerUpIndex(PowerUpIndex.Shield);
    }

    public void awake() {
        PowerUpManager.getInstance().onShieldSpawn.addListener(this::handleOnShieldSpawn);
    }

    private void handleOnShieldSpawn(Object o, Double duration) {
        Shield.getInstance().turnOn(duration);
    }
}
