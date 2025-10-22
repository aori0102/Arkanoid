package game.Voltraxis;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * Central class to handle Voltraxis' groggy status.
 */
public class VoltraxisGroggy extends MonoBehaviour {

    private static final double MAX_GROGGY = 1.0;

    private boolean isGroggyLocked = false;
    private double groggy = 0.0;
    private VoltraxisGroggyUI voltraxisGroggyUI = null;

    /**
     * Fired when Voltraxis' groggy is full<br><br>
     * Use within {@link Voltraxis} to initiate groggy skill.
     */
    public EventHandler<Void> onGroggyReachedMax = new EventHandler<>(VoltraxisGroggy.class);

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
    protected void destroyComponent() {
        voltraxisGroggyUI = null;
    }

    /**
     * Listen to event {@link Voltraxis#onDamaged}.
     *
     * @param voltraxis The central class of the boss {@link Voltraxis}.
     */
    protected void setVoltraxis(Voltraxis voltraxis) {
        voltraxis.onDamaged.addListener(this::voltraxis_onDamaged);
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

        if (isGroggyLocked) {
            return;
        }

        groggy += VoltraxisData.GROGGY_DELTA;
        if (isGroggyToDeployPowerCore()) {
            onGroggyToDeployPowerCore.invoke(this, null);
        }
        if (isMaxGroggy()) {
            onGroggyReachedMax.invoke(this, null);
        }
        voltraxisGroggyUI.setTargetRatio(groggy / MAX_GROGGY);

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
     * Lock the ability to modify groggy.<br><br>
     * <b>Control within {@link Voltraxis}.</b>
     */
    public void lockGroggy() {
        isGroggyLocked = true;
    }

    /**
     * Unlock the ability to modify groggy.<br><br>
     * <b>Control within {@link Voltraxis}.</b>
     */
    public void unlockGroggy() {
        isGroggyLocked = false;
    }

    /**
     * Attach the object with {@link VoltraxisGroggyUI} to enable
     * groggy UI update.<br><br>
     * <b><i><u>NOTE:</u> Only use within {@link VoltraxisPrefab}
     * as a component linking process.</i></b>
     *
     * @param voltraxisGroggyUI The UI component of the groggy.
     */
    public void attachVoltraxisGroggyUI(VoltraxisGroggyUI voltraxisGroggyUI) {
        this.voltraxisGroggyUI = voltraxisGroggyUI;
    }

}