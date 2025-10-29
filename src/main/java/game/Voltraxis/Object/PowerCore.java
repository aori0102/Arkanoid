package game.Voltraxis.Object;

import game.Voltraxis.Interface.ITakePlayerDamage;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import utils.Time;
import utils.Vector2;

/**
 * Voltraxis' power core.
 */
public class PowerCore extends MonoBehaviour implements ITakePlayerDamage {

    private static final double MAX_CORE_FLUCTUATION_DISTANCE = 3.2;
    private static final double CORE_FLUCTUATION_RATE = 0.49;

    private int health;
    private int maxHealth;
    private PowerCoreVisual powerCoreVisual = null;
    private Vector2 defaultPosition = Vector2.zero();

    public EventHandler<Void> onPowerCoreDestroyed = new EventHandler<>(PowerCore.class);
    public EventHandler<Void> onHealthChanged = new EventHandler<>(PowerCore.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerCore(GameObject owner) {
        super(owner);
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
        onHealthChanged.invoke(this, null);
        if (health <= 0) {
            GameObjectManager.destroy(gameObject);
        }
    }

    @Override
    public void awake() {
        defaultPosition = getTransform().getGlobalPosition();
    }

    @Override
    public void update() {
        var delta = Math.sin(CORE_FLUCTUATION_RATE * Time.getTime() * Math.PI) * MAX_CORE_FLUCTUATION_DISTANCE;
        var deltaVector = new Vector2(0.0, delta);
        getTransform().setGlobalPosition(defaultPosition.add(deltaVector));
    }

    @Override
    public void onDestroy() {
        onPowerCoreDestroyed.invoke(this, null);
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

    public void setPowerCoreVisual(PowerCoreVisual powerCoreVisual) {
        this.powerCoreVisual = powerCoreVisual;
    }

}