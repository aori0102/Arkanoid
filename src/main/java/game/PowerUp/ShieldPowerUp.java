package game.PowerUp;

import game.GameObject.Shield.Shield;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;

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
    }

    private void handleOnShieldSpawn() {
        Shield.getInstance().turnOn();
    }

    @Override
    public void onApplied() {
        handleOnShieldSpawn();
        GameObjectManager.destroy(gameObject);
    }
}
