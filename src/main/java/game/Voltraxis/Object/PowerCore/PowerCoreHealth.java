package game.Voltraxis.Object.PowerCore;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public final class PowerCoreHealth extends MonoBehaviour {

    private int health = 0;
    private int maxHealth = 0;

    public EventHandler<Void> onHealthReachedZero = new EventHandler<>(PowerCoreHealth.class);
    public EventHandler<Void> onHealthChanged = new EventHandler<>(PowerCoreHealth.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerCoreHealth(GameObject owner) {
        super(owner);
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

    public void damage(int amount) {
        health -= amount;
        if (health <= 0) {
            onHealthReachedZero.invoke(this, null);
        }
        onHealthChanged.invoke(this, null);
    }

}