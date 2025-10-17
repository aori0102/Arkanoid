package game.Voltraxis.Object;

import game.Voltraxis.Interface.IBossTarget;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;

/**
 * Voltraxis' power core.
 */
public class PowerCore extends MonoBehaviour implements IBossTarget {

    private int health;
    private int maxHealth;

    public EventHandler<Void> onPowerCoreDestroyed = new EventHandler<>(this);
    public EventHandler<Void> onHealthChanged = new EventHandler<>(this);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerCore(GameObject owner) {
        super(owner);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {
        onPowerCoreDestroyed = null;
        onHealthChanged = null;
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
        onHealthChanged.invoke(this, null);
        if (health <= 0) {
            onPowerCoreDestroyed.invoke(this, null);
            GameObjectManager.destroy(gameObject);
        }
    }

    /**
     * Get the current HP of the power core.
     *
     * @return The current HP of the power core.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Get the power core's max HP.
     *
     * @return The power core's max HP.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Set the health for this power core. Only
     * do this upon spawning. This function sets
     * both health and max health.
     *
     * @param health The health of the power core.
     */
    public void setHealth(int health) {
        this.health = health;
        this.maxHealth = health;
    }

}