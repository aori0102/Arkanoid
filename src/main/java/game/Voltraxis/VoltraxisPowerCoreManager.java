package game.Voltraxis;

import game.Voltraxis.Object.PowerCore;
import game.Voltraxis.Prefab.PowerCorePrefab;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import utils.Vector2;

import java.util.HashMap;

/**
 * Manages {@link PowerCore} for Voltraxis.
 */
public class VoltraxisPowerCoreManager extends MonoBehaviour {

    private enum PowerCoreIndex {
        Left(VoltraxisData.LEFT_CORE_POSITION),
        Right(VoltraxisData.RIGHT_CORE_POSITION);

        public final Vector2 corePosition;

        PowerCoreIndex(Vector2 corePosition) {
            this.corePosition = corePosition;
        }
    }

    private static class PowerCoreInfo {
        public PowerCore core;
        public EventActionID destroyedEventActionID;
        public VoltraxisEffectManager.EffectInputInfo powerCoreEffectInfo;
        public VoltraxisEffectManager.EffectInputInfo damageTakenEffectInfo;
    }

    private final HashMap<PowerCoreIndex, PowerCoreInfo> powerCoreInfoMap = new HashMap<>();

    public EventHandler<Void> onPowerCoreDestroyed = new EventHandler<>(VoltraxisPowerCoreManager.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisPowerCoreManager(GameObject owner) {
        super(owner);
        powerCoreInfoMap.put(PowerCoreIndex.Left, new PowerCoreInfo());
        powerCoreInfoMap.put(PowerCoreIndex.Right, new PowerCoreInfo());
    }

    @Override
    public void awake() {
        Voltraxis.getInstance().getVoltraxisGroggy().onGroggyToDeployPowerCore
                .addListener(this::voltraxisGroggy_onGroggyToDeployPowerCore);
        Voltraxis.getInstance().getVoltraxisCharging().onFullyCharged
                .addListener(this::voltraxisCharging_onFullyCharged);
    }

    /**
     * Called when {@link VoltraxisCharging#onFullyCharged} is invoked.<br><br>
     * This function destroys all {@link PowerCore}s after Voltraxis' EX is fully charged.
     *
     * @param sender Event caller {@link VoltraxisCharging}.
     * @param e      Empty event argument.
     */
    private void voltraxisCharging_onFullyCharged(Object sender, Void e) {
        for (var powerCoreInfo : powerCoreInfoMap.values()) {
            GameObjectManager.destroy(powerCoreInfo.core.getGameObject());
        }
    }

    /**
     * Called when {@link VoltraxisGroggy#onGroggyToDeployPowerCore} is invoked.<br><br>
     * This function deploys two {@link PowerCore}s when Voltraxis' groggy reaches a certain amount
     *
     * @param sender Event caller {@link VoltraxisGroggy}.
     * @param e      Empty event argument.
     */
    private void voltraxisGroggy_onGroggyToDeployPowerCore(Object sender, Void e) {
        if (hasPowerCore()) {
            return;
        }

        spawnCore(PowerCoreIndex.Left);
        spawnCore(PowerCoreIndex.Right);
    }

    /**
     * Power core spawning utility. Spawn a new power core and attach
     * to {@code target}. {@code target} must be {@code null}, which means
     * that the power core to be spawned to should not exist. The power
     * core will be placed at {@code position}, the event
     * {@link PowerCore#onPowerCoreDestroyed} will be listened to and
     * Voltraxis will gain the pre-determined effects.
     *
     * @param powerCoreIndex The index of the power core to spawn in.
     */
    private void spawnCore(PowerCoreIndex powerCoreIndex) {

        // Get info
        var powerCoreInfo = powerCoreInfoMap.get(powerCoreIndex);

        // Instantiate the power core
        var newCore = new PowerCorePrefab().instantiatePrefab()
                .getComponent(PowerCore.class);
        newCore.getTransform().setGlobalPosition(powerCoreIndex.corePosition);
        int powerCoreHealth = (int) (VoltraxisData.BASE_MAX_HEALTH * VoltraxisData.POWER_CORE_PROPORTIONAL_HEALTH);
        newCore.setHealth(powerCoreHealth);

        // Add power core effect
        var powerCoreEffectInfo = new VoltraxisEffectManager.EffectInputInfo();
        powerCoreEffectInfo.index = VoltraxisData.EffectIndex.PowerCore;
        powerCoreEffectInfo.value = 0.0;
        //TODO:
//        powerCoreEffectInfo.effectEndingConstraint
//                = (_) -> newCore.getGameObject().isDestroyed();
        powerCoreEffectInfo.effectEndedCallback = null;
        Voltraxis.getInstance().getVoltraxisEffectManager().addEffect(powerCoreEffectInfo);


        // Add effect on damage taken decrement
        var damageTakenDecrementEffectInfo = new VoltraxisEffectManager.EffectInputInfo();
        damageTakenDecrementEffectInfo.index = VoltraxisData.EffectIndex.DamageTakenDecrement;
        damageTakenDecrementEffectInfo.value = VoltraxisData.POWER_CORE_DAMAGE_TAKEN_REDUCTION;
        //TODO:
//        damageTakenDecrementEffectInfo.effectEndingConstraint
//                = (_) -> newCore.getGameObject().isDestroyed();
        damageTakenDecrementEffectInfo.effectEndedCallback = null;
        Voltraxis.getInstance().getVoltraxisEffectManager().addEffect(damageTakenDecrementEffectInfo);

        // Link event and update info
        powerCoreInfo.destroyedEventActionID
                = newCore.onPowerCoreDestroyed.addListener(this::powerCore_onPowerCoreDestroyed);
        powerCoreInfo.core = newCore;
        powerCoreInfo.damageTakenEffectInfo = damageTakenDecrementEffectInfo;
        powerCoreInfo.powerCoreEffectInfo = powerCoreEffectInfo;

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

            var leftCoreInfo = powerCoreInfoMap.get(PowerCoreIndex.Left);
            var rightCoreInfo = powerCoreInfoMap.get(PowerCoreIndex.Right);

            if (core == leftCoreInfo.core) {
                System.out.println("Destroyed left core");
                leftCoreInfo.core = null;
                if (leftCoreInfo.destroyedEventActionID != null) {
                    core.onPowerCoreDestroyed.removeListener(leftCoreInfo.destroyedEventActionID);
                    leftCoreInfo.destroyedEventActionID = null;
                }
            } else if (core == rightCoreInfo.core) {
                System.out.println("Destroyed right core");
                rightCoreInfo.core = null;
                if (rightCoreInfo.destroyedEventActionID != null) {
                    core.onPowerCoreDestroyed.removeListener(rightCoreInfo.destroyedEventActionID);
                    rightCoreInfo.destroyedEventActionID = null;
                }
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
        return powerCoreInfoMap.get(PowerCoreIndex.Left).core != null
                || powerCoreInfoMap.get(PowerCoreIndex.Right).core != null;
    }

}