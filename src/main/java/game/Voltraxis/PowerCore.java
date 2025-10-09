package game.Voltraxis;

import org.EventHandler;
import org.GameObject;
import org.GameObjectManager;
import org.MonoBehaviour;

public class PowerCore extends MonoBehaviour {

    private int health;
    private int maxHealth;

    public EventHandler<Void> onPowerCoreDestroyed = new EventHandler<>(this);

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

    }

    private void damage(int amount) {
        health -= amount;
        if (health <= 0) {
            onPowerCoreDestroyed.invoke(this, null);
            GameObjectManager.destroy(gameObject);
        }
    }

}