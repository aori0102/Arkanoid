package game.Voltraxis.Object.PowerCore;

import org.Event.EventHandler;
import org.GameObject.GameObject;
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

    private PowerCoreDamageAcceptor powerCoreDamageAcceptor = null;
    private PowerCoreHealth powerCoreHealth = null;

    public EventHandler<Void> onPowerCoreDestroyed = new EventHandler<>(PowerCore.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerCore(GameObject owner) {
        super(owner);
        powerCoreDamageAcceptor = addComponent(PowerCoreDamageAcceptor.class);
        powerCoreHealth = addComponent(PowerCoreHealth.class);
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

    public PowerCoreDamageAcceptor getPowerCoreDamageAcceptor() {
        return powerCoreDamageAcceptor;
    }

    public PowerCoreHealth getPowerCoreHealth() {
        return powerCoreHealth;
    }

}