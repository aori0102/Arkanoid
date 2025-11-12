package game.PowerUp;

import game.GameObject.Shield.Shield;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;

/**
 * Shield power-up.
 *
 * When applied, this power-up activates the player's shield,
 * providing temporary protection against incoming damage or effects.
 */
public class ShieldPowerUp extends PowerUp {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The GameObject that owns this component.
     */
    public ShieldPowerUp(GameObject owner) {
        super(owner);
        setPowerUpIndex(PowerUpIndex.Shield);
    }

    /**
     * Activates the player's shield.
     * <p>
     * This method retrieves the singleton Shield instance and turns it on,
     * granting the player temporary invincibility.
     */
    private void handleOnShieldSpawn() {
        Shield.getInstance().turnOn();
    }

    /**
     * Called when this power-up is applied.
     * <p>
     * This method triggers the shield activation and then destroys
     * the power-up object to remove it from the game.
     */
    @Override
    public void onApplied() {
        handleOnShieldSpawn();
        GameObjectManager.destroy(gameObject);
    }

}
