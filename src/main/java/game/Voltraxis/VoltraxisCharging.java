package game.Voltraxis;

import game.Voltraxis.Object.UltimateLaser;
import game.Voltraxis.Prefab.UltimateLaserPrefab;
import org.Animation.AnimationClipData;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import utils.Time;
import utils.Vector2;

import java.util.EnumMap;

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

    private enum CoroutineIndex {
        ToRechargeAfterHalt,
        ToFinishCharging,
        ToUnleashLaser,
        ToTerminateCharging,
        ToStartCharging,
    }

    private final EnumMap<CoroutineIndex, Time.CoroutineID> coroutineIDMap = new EnumMap<>(CoroutineIndex.class);

    private boolean isCharging = false;
    private UltimateLaser ultimateLaser = null;
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
        for (var coroutineID : coroutineIDMap.values()) {
            Time.removeCoroutine(coroutineID);
        }
    }

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

        if (!isCharging || isFullyCharged()) {
            return;
        }

        // Kill all coroutine in case the halt is in progress if terminated unexpectedly
        if (coroutineIDMap.get(CoroutineIndex.ToRechargeAfterHalt) != null) {
            Time.removeCoroutine(coroutineIDMap.remove(CoroutineIndex.ToRechargeAfterHalt));
        }
        if (coroutineIDMap.get(CoroutineIndex.ToStartCharging) != null) {
            Time.removeCoroutine(coroutineIDMap.remove(CoroutineIndex.ToStartCharging));
        }

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
        coroutineIDMap.put(
                CoroutineIndex.ToStartCharging,
                Time.addCoroutine(this::startCharging, Time.getTime() + STARTING_DELAY)
        );

    }

    /**
     * Start charging towards EX for Voltraxis.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link Voltraxis}
     * to start charging (when groggy is full).</i></b>
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
     * {@link VoltraxisData#CHARGING_HALT_DELAY}.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link Voltraxis}
     * when one of the two power cores is destroyed.</i></b>
     */
    private void haltCharging() {

        isCharging = false;
        setCurrentCharge(_currentCharge - VoltraxisData.CHARGING_HALT_AMOUNT);

        // Delay before charging again
        coroutineIDMap.put(
                CoroutineIndex.ToRechargeAfterHalt,
                Time.addCoroutine(() -> isCharging = true, Time.getTime() + VoltraxisData.CHARGING_HALT_DELAY)
        );

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

    private void startUnleashing() {

        onStartUnleashing.invoke(this, null);
        isCharging = false;
        coroutineIDMap.put(
                CoroutineIndex.ToUnleashLaser,
                Time.addCoroutine(this::unleashLaser, Time.getTime() + START_UNLEASH_DELAY)
        );
    }

    private void unleashLaser() {

        onUnleashingLaser.invoke(this, null);
        ultimateLaser = new UltimateLaserPrefab().instantiatePrefab()
                .getComponent(UltimateLaser.class);
        ultimateLaser.getTransform().setGlobalPosition(
                getTransform().getGlobalPosition().add(Vector2.down().multiply(ULTIMATE_LASER_OFFSET))
        );
        coroutineIDMap.put(
                CoroutineIndex.ToFinishCharging,
                Time.addCoroutine(this::finishUnleashing, Time.getTime() + UNLEASH_DURATION)
        );
    }

    private void finishUnleashing() {

        onFinishUnleashing.invoke(this, null);
        GameObjectManager.destroy(ultimateLaser.getGameObject());
        coroutineIDMap.put(
                CoroutineIndex.ToTerminateCharging,
                Time.addCoroutine(this::terminateCharging, Time.getTime() + FINISH_UNLEASH_DELAY)
        );
    }

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
        if (coroutineIDMap.containsKey(CoroutineIndex.ToRechargeAfterHalt)) {
            Time.removeCoroutine(coroutineIDMap.remove(CoroutineIndex.ToRechargeAfterHalt));
        }

        Voltraxis.getInstance().getVoltraxisEffectManager().removeEffect(chargingEffectID);
        chargingEffectID = null;

    }

    private boolean isFullyCharged() {
        return _currentCharge == VoltraxisData.CHARGING_MAX;
    }

}