package game.Voltraxis.Object;

import game.Voltraxis.Interface.IBossTarget;
import game.Voltraxis.Voltraxis;
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
    private Voltraxis voltraxis = null;
    private PowerCoreVisual powerCoreVisual = null;

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
        powerCoreVisual=null;
        onHealthChanged = null;
        voltraxis.onChargingMedium.removeListener(this::voltraxis_onChargingMedium);
        voltraxis.onChargingLow.removeListener(this::voltraxis_onChargingLow);
        voltraxis.onChargingHigh.removeListener(this::voltraxis_onChargingHigh);
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

    public void setVoltraxis(Voltraxis voltraxis) {
        this.voltraxis = voltraxis;
        voltraxis.onChargingLow.addListener(this::voltraxis_onChargingLow);
        voltraxis.onChargingHigh.addListener(this::voltraxis_onChargingHigh);
        voltraxis.onChargingMedium.addListener(this::voltraxis_onChargingMedium);
    }

    private void voltraxis_onChargingLow(Object sender, Void e) {
        powerCoreVisual.animateLowCharging();
    }

    private void voltraxis_onChargingMedium(Object sender, Void e) {
        powerCoreVisual.animateMediumCharging();
    }

    private void voltraxis_onChargingHigh(Object sender, Void e) {
        powerCoreVisual.animateHighCharging();
    }

    public void setPowerCoreVisual(PowerCoreVisual powerCoreVisual) {
        this.powerCoreVisual=powerCoreVisual;
    }

}