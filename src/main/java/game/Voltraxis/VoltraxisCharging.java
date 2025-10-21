package game.Voltraxis;

import game.Voltraxis.Object.PowerCore;
import game.Voltraxis.Object.UltimateLaser;
import org.Animation.AnimationClipData;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import utils.Time;
import utils.Vector2;

/**
 * Class to handling charging state for Voltraxis towards unleashing
 * EX skill.
 */
public class VoltraxisCharging extends MonoBehaviour {

    private static final double STARTING_DELAY = 0.5;
    private static final double START_UNLEASH_DELAY = 0.9;
    private static final double UNLEASH_DURATION = 4.8;
    private static final double FINISH_UNLEASH_DELAY = 0.7;
    private static final double ULTIMATE_LASER_OFFSET = 90.0;

    private Voltraxis voltraxis = null;
    private boolean isCharging = false;
    private Time.CoroutineID haltDelayCoroutineID = null;
    private Time.CoroutineID startCoroutineID = null;
    private UltimateLaser ultimateLaser = null;

    public enum ChargingPhase {
        Phase_1(AnimationClipData.Voltraxis_Charging_Phase_1),
        Phase_2(AnimationClipData.Voltraxis_Charging_Phase_2),
        Phase_3(AnimationClipData.Voltraxis_Charging_Phase_3),
        Phase_4(AnimationClipData.Voltraxis_Charging_Phase_4),
        Phase_5(AnimationClipData.Voltraxis_Charging_Phase_5),
        Phase_6(AnimationClipData.Voltraxis_Charging_Phase_6),
        None(AnimationClipData._None);

        public static final int MAX_PHASE = 6;
        public final AnimationClipData animationIndex;

        ChargingPhase(AnimationClipData animationIndex) {
            this.animationIndex = animationIndex;
        }
    }

    /**
     * <b>Read-only. Write via {@link #setCurrentCharge}.</b>
     */
    private double _currentCharge = 0.0;

    /**
     * <b>Read-only. Update via {@link #setCurrentCharge}.</b>
     */
    private ChargingPhase _currentChargingPhase = ChargingPhase.None;

    public EventHandler<ChargingPhase> onChargingPhaseChanged = new EventHandler<>(this);
    public EventHandler<Double> onChargingRatioChanged = new EventHandler<>(this);
    public EventHandler<Void> onChargingEntered = new EventHandler<>(this);
    public EventHandler<Void> onChargingTerminated = new EventHandler<>(this);
    public EventHandler<Void> onStartUnleashing = new EventHandler<>(this);
    public EventHandler<Void> onUnleashingLaser = new EventHandler<>(this);
    public EventHandler<Void> onFinishUnleashing = new EventHandler<>(this);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisCharging(GameObject owner) {
        super(owner);
    }

    @Override
    protected void destroyComponent() {
    }

    @Override
    public void awake() {
        voltraxis.getVoltraxisGroggy().onGroggyReachedMax.addListener(this::voltraxisGroggy_onGroggyReachedMax);
        voltraxis.getVoltraxisPowerCoreManager().onPowerCoreDestroyed
                .addListener(this::voltraxisPowerCoreManager_onPowerCoreDestroyed);
    }

    @Override
    public void update() {
        if (isCharging) {
            setCurrentCharge(_currentCharge + VoltraxisData.CHARGING_RATE * Time.deltaTime);
        }
    }

    /**
     * Link the central brain of Voltraxis to this object.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link VoltraxisPrefab}
     * as part of component linking process.</i></b>
     *
     * @param voltraxis The central brain of Voltraxis.
     */
    public void linkVoltraxis(Voltraxis voltraxis) {
        this.voltraxis = voltraxis;
    }

    /**
     * Called when {@link VoltraxisPowerCoreManager#onPowerCoreDestroyed} is invoked.<br><br>
     * This function handles charging after a power core is destroyed:<br>
     * <ul>
     *     <li><b>Halt</b> if there is still one power core.</li>
     *     <li><b>Terminate</b> if there is no more power core.</li>
     * </ul>
     */
    private void voltraxisPowerCoreManager_onPowerCoreDestroyed(Object sender, PowerCore e) {
        if (voltraxis.getVoltraxisPowerCoreManager().hasPowerCore()) {
            haltCharging();
        } else {
            terminateCharging();
        }
    }

    /**
     * Called when {@link VoltraxisGroggy#onGroggyReachedMax} is invoked.<br><br>
     * This function initiate the charging phase after groggy is max.
     */
    private void voltraxisGroggy_onGroggyReachedMax(Object sender, Void e) {
        onChargingEntered.invoke(this, null);
        startCoroutineID = Time.addCoroutine(this::startCharging, Time.time + STARTING_DELAY);
    }

    /**
     * Start charging towards EX for Voltraxis.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link Voltraxis}
     * to start charging (when groggy is full).</i></b>
     */
    private void startCharging() {
        isCharging = true;
        setCurrentCharge(0.0);
    }

    /**
     * Halt the charging process by
     * {@link VoltraxisData#CHARGING_HALT_AMOUNT}, then
     * delay charging for a set amount of time as defined in
     * {@link VoltraxisData#CHARGING_HALT_DELAY}.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link Voltraxis}
     * when one of the two power cores is destroyed.</i></b>
     */
    private void haltCharging() {

        isCharging = false;
        setCurrentCharge(_currentCharge - VoltraxisData.CHARGING_HALT_AMOUNT);

        // Delay before charging again
        haltDelayCoroutineID = Time.addCoroutine(() -> isCharging = true, Time.time + VoltraxisData.CHARGING_HALT_DELAY);

    }

    /**
     * Set the current charge and update charging state accordingly.
     *
     * @param amount The amount to set.
     */
    private void setCurrentCharge(double amount) {
        _currentCharge = Math.clamp(amount, 0, VoltraxisData.CHARGING_MAX);
        double ratio = _currentCharge / VoltraxisData.CHARGING_MAX;
        var targetChargingPhase = ChargingPhase.values()[(int) (ratio * ChargingPhase.MAX_PHASE)];
        if (targetChargingPhase != _currentChargingPhase) {
            _currentChargingPhase = targetChargingPhase;
            onChargingPhaseChanged.invoke(this, targetChargingPhase);
        }
        onChargingRatioChanged.invoke(this, ratio);
        if (_currentCharge == VoltraxisData.CHARGING_MAX) {
            startUnleashing();
        }
    }

    private void startUnleashing() {
        onStartUnleashing.invoke(this, null);
        Time.addCoroutine(this::unleashLaser, Time.time + START_UNLEASH_DELAY);
        isCharging = false;
    }

    private void unleashLaser() {
        onUnleashingLaser.invoke(this, null);
        ultimateLaser = VoltraxisPrefab.instantiateLaser();
        ultimateLaser.getTransform().setGlobalPosition(
                getTransform().getGlobalPosition().add(Vector2.down().multiply(ULTIMATE_LASER_OFFSET))
        );
        Time.addCoroutine(this::finishUnleashing, Time.time + UNLEASH_DURATION);
    }

    private void finishUnleashing() {
        onFinishUnleashing.invoke(this, null);
        GameObjectManager.destroy(ultimateLaser.getGameObject());
        Time.addCoroutine(this::terminateCharging, Time.time + FINISH_UNLEASH_DELAY);
    }

    /**
     * Terminate the charging process and deactivate the
     * charging UI.<br><br>
     * <b><i><u>NOTE</u> : Use within {@link Voltraxis}
     * when both power cores are destroyed, forcing Voltraxis
     * into weakened state.</i></b>
     */
    private void terminateCharging() {

        isCharging = false;
        setCurrentCharge(0.0);
        onChargingTerminated.invoke(this, null);

        // Remove delay coroutine in case this function is called within halting
        if (haltDelayCoroutineID != null) {
            Time.removeCoroutine(haltDelayCoroutineID);
            haltDelayCoroutineID = null;
        }

    }

}