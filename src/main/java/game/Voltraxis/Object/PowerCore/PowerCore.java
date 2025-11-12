package game.Voltraxis.Object.PowerCore;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import utils.Time;
import utils.Vector2;

/**
 * Voltraxis' power core.
 */
public class PowerCore extends MonoBehaviour {

    private static final double MAX_CORE_FLUCTUATION_DISTANCE = 3.2;
    private static final double CORE_FLUCTUATION_RATE = 0.49;

    private Vector2 defaultPosition = Vector2.zero();

    private final PowerCoreHealth powerCoreHealth = addComponent(PowerCoreHealth.class);
    private final PowerCoreStat powerCoreStat = addComponent(PowerCoreStat.class);

    public EventHandler<Void> onPowerCoreDestroyed = new EventHandler<>(PowerCore.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerCore(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        defaultPosition = getTransform().getGlobalPosition();
    }

    @Override
    public void start() {
        powerCoreHealth.onHealthReachesZero
                .addListener(this::powerCoreHealth_onHealthReachesZero);
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

    public PowerCoreHealth getPowerCoreHealth() {
        return powerCoreHealth;
    }

    public PowerCoreStat getPowerCoreStat() {
        return powerCoreStat;
    }

    /**
     * Called when {@link PowerCoreHealth#onHealthReachesZero} is invoked.<br><br>
     * This function destroys this power core when its health reaches zero.
     *
     * @param sender Event caller {@link PowerCoreHealth}.
     * @param e      Empty event argument.
     */
    private void powerCoreHealth_onHealthReachesZero(Object sender, Void e) {
        GameObjectManager.destroy(gameObject);
    }

}