package game.Voltraxis.PowerCore;

import game.Voltraxis.Interface.IBossTarget;
import org.EventHandler;
import org.GameObject;
import org.GameObjectManager;
import org.MonoBehaviour;

public class PowerCore extends MonoBehaviour implements IBossTarget {

    private int health;
    private int maxHealth;

    public EventHandler<Void> onPowerCoreDestroyed = new EventHandler<>(this);
    public EventHandler<Void> onHealthChanged = new EventHandler<>(this);

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int health) {
        this.health = health;
        this.maxHealth = health;
    }

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
    public void damage(int amount) {
        health -= amount;
        onHealthChanged.invoke(this, null);
        if (health <= 0) {
            onPowerCoreDestroyed.invoke(this, null);
            GameObjectManager.destroy(gameObject);
        }
    }

}