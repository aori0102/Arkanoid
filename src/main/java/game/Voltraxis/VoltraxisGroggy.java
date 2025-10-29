package game.Voltraxis;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;

/**
 * Central class to handle Voltraxis' groggy status.
 */
public class VoltraxisGroggy extends MonoBehaviour {

    private static final double MAX_GROGGY = 1.0;
    private static final double GROGGY_DELAY_AFTER_CHARGING = 1.7;

    private double groggy = 0.0;
    private boolean groggyLocked = false;
    private Time.CoroutineID resetGroggyCoroutineID = null;

    /**
     * Fired when Voltraxis' groggy is full<br><br>
     * Use within {@link Voltraxis} to initiate groggy skill.
     */
    public EventHandler<Void> onGroggyReachedMax = new EventHandler<>(VoltraxisGroggy.class);

    /**
     * Fired when Voltraxis' groggy changes. This event's argument contains
     * a single double representing the groggy's ratio in range {@code [0, 1]}.
     */
    public EventHandler<Double> onGroggyRatioChanged = new EventHandler<>(VoltraxisGroggy.class);

    /**
     * Fired when Voltraxis' groggy has surpassed the minimum
     * amount to spawn {@link game.Voltraxis.Object.PowerCore}s,
     * which is indicated by
     * {@link VoltraxisData#MIN_GROGGY_ON_POWER_CORE_DEPLOY}.<br><br>
     * Use within {@link Voltraxis} to spawn
     * {@link game.Voltraxis.Object.PowerCore}s.
     */
    public EventHandler<Void> onGroggyToDeployPowerCore = new EventHandler<>(VoltraxisGroggy.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisGroggy(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        Voltraxis.getInstance().onDamaged.addListener(this::voltraxis_onDamaged);
        Voltraxis.getInstance().getVoltraxisCharging().onChargingTerminated
                .addListener(this::voltraxisCharging_onChargingTerminated);
    }

    @Override
    public void onDestroy() {
        Time.removeCoroutine(resetGroggyCoroutineID);
    }

    /**
     * Called when {@link Voltraxis#onDamaged} is invoked. This function
     * increment the groggy amount and fire {@link #onGroggyReachedMax}
     * or {@link #onGroggyToDeployPowerCore} if meets certain criteria.
     *
     * @param sender {@link Voltraxis}.
     * @param e      Empty event argument.
     */
    private void voltraxis_onDamaged(Object sender, Void e) {

        if (groggyLocked) {
            return;
        }

        groggy += VoltraxisData.GROGGY_DELTA;
        if (isGroggyToDeployPowerCore()) {
            onGroggyToDeployPowerCore.invoke(this, null);
        }
        if (isMaxGroggy()) {
            onGroggyReachedMax.invoke(this, null);
            groggyLocked = true;
        }
        onGroggyRatioChanged.invoke(this, groggy / MAX_GROGGY);

    }

    /**
     * Utility class to determine the validity to spawn
     * {@link game.Voltraxis.Object.PowerCore}s, or if
     * the groggy has surpassed a certain amount to spawn
     * them, indicating by {@link VoltraxisData#MIN_GROGGY_ON_POWER_CORE_DEPLOY}.
     *
     * @return {@code true} if groggy is enough to spawn
     * {@link game.Voltraxis.Object.PowerCore}s, otherwise
     * {@code false}.
     */
    private boolean isGroggyToDeployPowerCore() {
        return groggy >= VoltraxisData.MIN_GROGGY_ON_POWER_CORE_DEPLOY;
    }

    /**
     * Utility class to determine if the groggy as reached
     * its maximum capacity, indicating by {@link #MAX_GROGGY}.
     *
     * @return {@code true} if groggy is max, otherwise {@code false}.
     */
    private boolean isMaxGroggy() {
        return groggy >= MAX_GROGGY;
    }

    /**
     * Called when {@link VoltraxisCharging#onChargingTerminated} is invoked.<br><br>
     * This function reset the groggy gauge for Voltraxis after charging is terminated.
     *
     * @param sender Event caller, {@link VoltraxisCharging}.
     * @param e      Empty event argument.
     */
    private void voltraxisCharging_onChargingTerminated(Object sender, Void e) {
        groggy = 0.0;
        onGroggyRatioChanged.invoke(this, 0.0);
        resetGroggyCoroutineID = Time.addCoroutine(this::resetGroggy, Time.getTime() + GROGGY_DELAY_AFTER_CHARGING);
    }

    private void resetGroggy() {
        groggyLocked = false;
    }

}