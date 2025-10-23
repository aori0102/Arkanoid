package game.Voltraxis;

import game.Voltraxis.Object.PowerCore;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Vector2;

/**
 * Manages {@link PowerCore} for Voltraxis.
 */
public class VoltraxisPowerCoreManager extends MonoBehaviour {

    private PowerCore leftCore = null;
    private PowerCore rightCore = null;
    private EventActionID leftCoreEventActionID = null;
    private EventActionID rightCoreEventActionID = null;

    public EventHandler<PowerCore> onPowerCoreDeployed = new EventHandler<>(VoltraxisPowerCoreManager.class);
    public EventHandler<PowerCore> onPowerCoreDestroyed = new EventHandler<>(VoltraxisPowerCoreManager.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisPowerCoreManager(GameObject owner) {
        super(owner);
    }

    /**
     * Spawn two power cores on either side of the board.
     */
    public void spawnPowerCores() {
        if (hasPowerCore()) {
            return;
        }
        leftCore = spawnCore(VoltraxisData.LEFT_CORE_POSITION);
        leftCoreEventActionID = leftCore.onPowerCoreDestroyed.addListener(this::powerCore_onPowerCoreDestroyed);
        rightCore = spawnCore(VoltraxisData.RIGHT_CORE_POSITION);
        rightCoreEventActionID = rightCore.onPowerCoreDestroyed.addListener(this::powerCore_onPowerCoreDestroyed);
    }

    /**
     * Power core spawning utility. Spawn a new power core and attach
     * to {@code target}. {@code target} must be {@code null}, which means
     * that the power core to be spawned to should not exist. The power
     * core will be placed at {@code position}, the event
     * {@link PowerCore#onPowerCoreDestroyed} will be listened to and
     * Voltraxis will gain the pre-determined effects.
     *
     * @param position The position to spawn the power core
     */
    private PowerCore spawnCore(Vector2 position) {

        // Instantiate the power core
        var newCore = VoltraxisPrefab.instantiatePowerCore();
        newCore.getTransform().setGlobalPosition(position);
        int powerCoreHealth = (int) (VoltraxisData.BASE_MAX_HEALTH * VoltraxisData.POWER_CORE_PROPORTIONAL_HEALTH);
        newCore.setHealth(powerCoreHealth);

        onPowerCoreDeployed.invoke(this, newCore);

        return newCore;

    }

    /**
     * Called from {@link PowerCore#onPowerCoreDestroyed} when the {@link PowerCore}
     * is destroyed. The function unlinks the corresponding power core and revert
     * Voltraxis' stat prior to the power core's buff.
     *
     * @param sender The {@link PowerCore}.
     * @param e      Empty event argument, should be passed in as {@code null}.
     */
    private void powerCore_onPowerCoreDestroyed(Object sender, Void e) {

        if (sender instanceof PowerCore core) {

            if (core == leftCore) {
                leftCore = null;
                core.onPowerCoreDestroyed.removeListener(leftCoreEventActionID);
                leftCoreEventActionID = null;
            } else if (core == rightCore) {
                rightCore = null;
                core.onPowerCoreDestroyed.removeListener(rightCoreEventActionID);
                rightCoreEventActionID = null;
            } else {
                throw new RuntimeException("Invalid power core");
            }

            onPowerCoreDestroyed.invoke(this, null);

        }

    }

    /**
     * Check if there is any power core left.
     *
     * @return {@code true} if there is at least on power core,
     * otherwise {@code false}.
     */
    public boolean hasPowerCore() {
        return leftCore != null || rightCore != null;
    }

}