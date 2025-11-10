package game.Voltraxis;

import org.Animation.AnimationClipData;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
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
    private static final double ULTIMATE_LASER_OFFSET = 70.0;

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

    private Time.CoroutineID unleashLaser_coroutineID = null;
    private Time.CoroutineID resumeCharging_coroutineID = null;
    private Time.CoroutineID finishUnleashing_coroutineID = null;
    private Time.CoroutineID terminateCharging_coroutineID = null;
    private Time.CoroutineID startCharging_coroutineID = null;

    private boolean isCharging = false;
    private GameObject ultimateLaser = null;
    private VoltraxisEffectManager.EffectID chargingEffectID = null;

    /**
     * <b>Read-only. Write via {@link #setCurrentCharge}.</b>
     */
    private double _currentCharge = 0.0;

    /**
     * <b>Read-only. Update via {@link #setCurrentCharge}.</b>
     */
    private ChargingPhase _currentChargingPhase = ChargingPhase.None;

    public EventHandler<ChargingPhase> onChargingPhaseChanged = new EventHandler<>(VoltraxisCharging.class);
    public EventHandler<Double> onChargingRatioChanged = new EventHandler<>(VoltraxisCharging.class);
    public EventHandler<Void> onChargingEntered = new EventHandler<>(VoltraxisCharging.class);
    public EventHandler<Void> onChargingTerminated = new EventHandler<>(VoltraxisCharging.class);
    public EventHandler<Void> onStartUnleashing = new EventHandler<>(VoltraxisCharging.class);
    public EventHandler<Void> onUnleashingLaser = new EventHandler<>(VoltraxisCharging.class);
    public EventHandler<Void> onFinishUnleashing = new EventHandler<>(VoltraxisCharging.class);
    public EventHandler<Void> onFullyCharged = new EventHandler<>(VoltraxisCharging.class);
    public EventHandler<Void> onBossWeakened = new EventHandler<>(VoltraxisCharging.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisCharging(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        Voltraxis.getInstance().getVoltraxisGroggy().onGroggyReachedMax
                .addListener(this::voltraxisGroggy_onGroggyReachedMax);
        Voltraxis.getInstance().getVoltraxisPowerCoreManager().onPowerCoreDestroyed
                .addListener(this::voltraxisPowerCoreManager_onPowerCoreDestroyed);
    }

    @Override
    public void update() {
        if (isCharging && !isFullyCharged()) {
            setCurrentCharge(_currentCharge + VoltraxisData.CHARGING_RATE * Time.getDeltaTime());
        }
    }

    @Override
    public void onDestroy() {
        Time.removeCoroutine(unleashLaser_coroutineID);
        Time.removeCoroutine(terminateCharging_coroutineID);
        Time.removeCoroutine(startCharging_coroutineID);
        Time.removeCoroutine(unleashLaser_coroutineID);
        Time.removeCoroutine(finishUnleashing_coroutineID);
    }

    /**
     * Whether Voltraxis is in charging phase.
     *
     * @return Whether Voltraxis is in charging phase.
     */
    public boolean isCharging() {
        return isCharging;
    }

    /**
     * Called when {@link VoltraxisPowerCoreManager#onPowerCoreDestroyed} is invoked.<br><br>
     * This function handles charging after a power core is destroyed:<br>
     * <ul>
     *     <li><b>Halt</b> if there is still one power core.</li>
     *     <li><b>Terminate</b> if there is no more power core.</li>
     * </ul>
     */
    private void voltraxisPowerCoreManager_onPowerCoreDestroyed(Object sender, Void e) {

        if (Voltraxis.getInstance() == null) {
            return;
        }

        if (!isCharging || isFullyCharged()) {
            return;
        }

        // Kill all coroutine in case the halt is in progress if terminated unexpectedly
        Time.removeCoroutine(resumeCharging_coroutineID);
        Time.removeCoroutine(startCharging_coroutineID);

        if (Voltraxis.getInstance().getVoltraxisPowerCoreManager().hasPowerCore()) {
            haltCharging();
        } else {
            weakenBoss();
            terminateCharging();
        }

    }

    /**
     * Called when {@link VoltraxisGroggy#onGroggyReachedMax} is invoked.<br><br>
     * This function initiate the charging phase after groggy is max.
     */
    private void voltraxisGroggy_onGroggyReachedMax(Object sender, Void e) {

        onChargingEntered.invoke(this, null);
        startCharging_coroutineID
                = Time.addCoroutine(this::startCharging, Time.getTime() + STARTING_DELAY);

    }

    /**
     * Start charging towards EX for Voltraxis.
     */
    private void startCharging() {

        isCharging = true;
        setCurrentCharge(0.0);

        // Add charging effect
        var chargingEffectInfo = new VoltraxisEffectManager.EffectInputInfo();
        chargingEffectInfo.index = VoltraxisData.EffectIndex.ChargingEX;
        chargingEffectInfo.value = 0.0;
        chargingEffectInfo.duration = Double.MAX_VALUE;
        chargingEffectInfo.effectEndedCallback = null;
        chargingEffectID
                = Voltraxis.getInstance().getVoltraxisEffectManager().addEffect(chargingEffectInfo);

    }

    /**
     * Halt the charging process by
     * {@link VoltraxisData#CHARGING_HALT_AMOUNT}, then
     * delay charging for a set amount of time as defined in
     * {@link VoltraxisData#CHARGING_HALT_DELAY}.
     */
    private void haltCharging() {

        isCharging = false;
        setCurrentCharge(_currentCharge - VoltraxisData.CHARGING_HALT_AMOUNT);

        // Delay before charging again
        resumeCharging_coroutineID
                = Time.addCoroutine(this::resumeCharging, Time.getTime() + VoltraxisData.CHARGING_HALT_DELAY);

    }

    /**
     * Resume charging after halting when a Power core is destroyed.
     */
    private void resumeCharging() {
        isCharging = true;
    }

    /**
     * Set the current charge and update charging state accordingly.
     *
     * @param amount The amount to set.
     */
    private void setCurrentCharge(double amount) {
        _currentCharge = Math.clamp(amount, 0, VoltraxisData.CHARGING_MAX);
        if (_currentCharge == VoltraxisData.CHARGING_MAX) {
            onFullyCharged.invoke(this, null);
        }
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

    /**
     * The phase right before Voltraxis unleashes the Ultimate Laser.
     */
    private void startUnleashing() {
        onStartUnleashing.invoke(this, null);
        isCharging = false;
        unleashLaser_coroutineID
                = Time.addCoroutine(this::unleashLaser, Time.getTime() + START_UNLEASH_DELAY);
    }

    /**
     * Unleashes Voltraxis' ultimate laser.
     */
    private void unleashLaser() {
        onUnleashingLaser.invoke(this, null);
        ultimateLaser = PrefabManager.instantiatePrefab(PrefabIndex.Voltraxis_UltimateLaser);
        ultimateLaser.getTransform().setGlobalPosition(
                getTransform().getGlobalPosition().add(Vector2.down().multiply(ULTIMATE_LASER_OFFSET))
        );
        finishUnleashing_coroutineID
                = Time.addCoroutine(this::finishUnleashing, Time.getTime() + UNLEASH_DURATION);
    }

    /**
     * Handle when Voltraxis finishes unleashing its ultimate laser.
     */
    private void finishUnleashing() {
        onFinishUnleashing.invoke(this, null);
        GameObjectManager.destroy(ultimateLaser);
        ultimateLaser = null;
        terminateCharging_coroutineID
                = Time.addCoroutine(this::terminateCharging, Time.getTime() + FINISH_UNLEASH_DELAY);
    }

    /**
     * Force Voltraxis into weaken state after both Power core are destroyed.
     */
    private void weakenBoss() {
        onBossWeakened.invoke(this, null);
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
        Time.removeCoroutine(resumeCharging_coroutineID);

        Voltraxis.getInstance().getVoltraxisEffectManager().removeEffect(chargingEffectID);
        chargingEffectID = null;

    }

    /**
     * Whether Voltraxis is fully charged to proceed into EX.
     *
     * @return Whether Voltraxis is fully charged.
     */
    private boolean isFullyCharged() {
        return _currentCharge == VoltraxisData.CHARGING_MAX;
    }

}