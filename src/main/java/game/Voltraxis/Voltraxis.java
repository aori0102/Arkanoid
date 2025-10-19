package game.Voltraxis;

import game.Voltraxis.Interface.IBossTarget;
import game.Voltraxis.Object.ElectricBall;
import game.Voltraxis.Object.PowerCore;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;
import utils.Vector2;

import java.util.HashMap;

/**
 * Main class of the Voltraxis boss. Handle central logic
 * (damage, health, status...) and skills.
 */
public class Voltraxis extends MonoBehaviour implements IBossTarget {

    public EventHandler<Void> onHealthChanged = new EventHandler<>(this);
    public EventHandler<Void> onDamaged = new EventHandler<>(this);

    private VoltraxisEffectManager voltraxisEffectManager = null;
    private VoltraxisPowerCoreManager voltraxisPowerCoreManager = null;
    private VoltraxisGroggy voltraxisGroggy = null;
    private VoltraxisCharging voltraxisCharging = null;

    private int health = VoltraxisData.BASE_MAX_HEALTH;
    private Time.CoroutineID chargingCoroutineID = null;
    private final HashMap<ElectricBall, EventActionID> electricBallEventActionIDMap = new HashMap<>();

    public Voltraxis(GameObject owner) {
        super(owner);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new Voltraxis(newOwner);
    }

    @Override
    protected void destroyComponent() {
        chargingCoroutineID = null;
    }

    @Override
    public void awake() {
        Time.addCoroutine(this::enhanceSkill, VoltraxisData.ENHANCE_SKILL_COOLDOWN);
        Time.addCoroutine(this::basicSkill, VoltraxisData.BASIC_SKILL_COOLDOWN * voltraxisEffectManager.getBasicCooldownMultiplier());
    }

    @Override
    public void takeDamage(int amount) {

        int finalDamage = (int) (amount * ((double) getDefence() / (getDefence() + VoltraxisData.DEFENSE_STRENGTH_SCALE)));
        health -= finalDamage;
        if (health <= 0) {
            health = 0;
            System.out.println("Boss ded");
        }

        onHealthChanged.invoke(this, null);
        onDamaged.invoke(this, null);

    }

    /**
     * <b>Voltraxis' basic skill: Arc Discharge</b><br><br>
     * Fires up to <b>5</b> electric orbs at varying angles
     * toward the player, each dealing <b>63.2%</b> ATK. If hit,
     * the player is stunned for <b>3s</b>.
     */
    private void basicSkill() {

        // Sample directions for every electric ball
        Vector2[] directionArray = {
                new Vector2(0.0, 1.0),
                new Vector2(-0.2, 1.0),
                new Vector2(-0.4, 1.0),
                new Vector2(0.2, 1.0),
                new Vector2(0.4, 1.0),
        };

        for (Vector2 direction : directionArray) {

            // Instantiate electric ball
            var electricBall = VoltraxisPrefab.instantiateElectricBall();

            // Modify value
            electricBallEventActionIDMap.put(
                    electricBall,
                    electricBall.onPaddleHit.addListener(this::electricBall_onPaddleHit)
            );
            electricBall.getTransform().setGlobalPosition(getTransform().getGlobalPosition());
            electricBall.setDirection(direction);

        }

        // Repeat basic skill after cooldown
        Time.addCoroutine(this::basicSkill, Time.time + getBasicSkillCooldown());

    }

    /**
     * <b>Voltraxis' enhance skill: Power Surge</b><br><br>
     * Increases ATK by <b>10%</b> every <b>30s</b> for <b>20s</b>.
     */
    private void enhanceSkill() {

        // Add attack increment effect
        var effectInfo = new VoltraxisEffectManager.EffectInfo();
        effectInfo.index = VoltraxisData.EffectIndex.AttackIncrement;
        effectInfo.value = VoltraxisData.ENHANCE_ATTACK_INCREMENT;
        effectInfo.effectEndingConstraint = (delta) -> delta >= VoltraxisData.ENHANCE_SKILL_DURATION;
        voltraxisEffectManager.addEffect(effectInfo, null);

        // Repeat enhance skill
        Time.addCoroutine(this::enhanceSkill, Time.time + VoltraxisData.ENHANCE_SKILL_COOLDOWN);

    }

    /**
     * <b>Voltraxis' groggy skill: Overhaul</b><br><br>
     * When the Groggy Gauge is filled, Voltraxis enters the
     * <b>Overhaul</b> state. In this mode, Arc Discharge cooldown
     * is reduced by <b>35%</b> and ATK is increased by <b>20%</b>.
     * When entering <b>Overhaul</b> state, Voltraxis begins charging
     * toward <b>Super Nova</b>.
     */
    private void groggySkill() {

        voltraxisGroggy.lockGroggy();

        // Add attack increment effect
        var attackIncrementEffectInfo = new VoltraxisEffectManager.EffectInfo();
        attackIncrementEffectInfo.index = VoltraxisData.EffectIndex.AttackIncrement;
        attackIncrementEffectInfo.value = VoltraxisData.GROGGY_ATTACK_INCREMENT;
        attackIncrementEffectInfo.effectEndingConstraint
                = (delta) -> delta >= VoltraxisData.GROGGY_DURATION;
        voltraxisEffectManager.addEffect(attackIncrementEffectInfo, null);

        // Add skill cooldown reduction effect
        var skillCooldownEffectInfo = new VoltraxisEffectManager.EffectInfo();
        skillCooldownEffectInfo.index = VoltraxisData.EffectIndex.SkillCooldownDecrement;
        skillCooldownEffectInfo.value = VoltraxisData.GROGGY_BASIC_COOLDOWN_REDUCTION;
        skillCooldownEffectInfo.effectEndingConstraint
                = (delta) -> delta >= VoltraxisData.GROGGY_DURATION;
        voltraxisEffectManager.addEffect(skillCooldownEffectInfo, null);

        // Add charging effect
        var chargingEffectInfo = new VoltraxisEffectManager.EffectInfo();
        chargingEffectInfo.index = VoltraxisData.EffectIndex.ChargingEX;
        chargingEffectInfo.value = 0.0;
        chargingEffectInfo.effectEndingConstraint
                = (_) -> !voltraxisPowerCoreManager.hasPowerCore();
        voltraxisEffectManager.addEffect(
                chargingEffectInfo,
                this::terminateCurrentCharging
        );

        // Charge towards EX Skill
        voltraxisCharging.startCharging();

    }

    /**
     * Terminate the current coroutine during EX charging.
     */
    private void terminateCurrentCharging() {
        if (chargingCoroutineID != null) {
            Time.removeCoroutine(chargingCoroutineID);
            chargingCoroutineID = null;
        }
    }

    /**
     * <b>Voltraxis' EX skill: Sword of Light, Super Nova</b><br><br>
     * Voltraxis unleashes a devastating electric slash, dealing <b>247%</b> ATK.
     */
    protected void exSkill() {

        chargingCoroutineID = null;
        // TODO: implement EX skill - Aori

    }

    /**
     * Get Voltraxis' current HP.
     *
     * @return Voltraxis' currentHP.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Get Voltraxis' current DEF, with modifier included.
     *
     * @return Voltraxis' current DEF.
     */
    public int getDefence() {
        return (int) (voltraxisEffectManager.getDefenseMultiplier() * VoltraxisData.BASE_DEFENSE);
    }

    /**
     * Get Voltraxis' current ATK, with modifier included.
     *
     * @return Voltraxis' current DEF.
     */
    public int getAttack() {
        return (int) (voltraxisEffectManager.getAttackMultiplier() * VoltraxisData.BASE_ATTACK);
    }

    /**
     * Get Voltraxis' basic skill cooldown, with modifier included.
     *
     * @return Voltraxis' basic skill cooldown.
     */
    public double getBasicSkillCooldown() {
        return VoltraxisData.BASIC_SKILL_COOLDOWN * voltraxisEffectManager.getBasicCooldownMultiplier();
    }

    /**
     * Attach {@link VoltraxisEffectManager} to this
     * object.<br>
     * <b>This function can only be called within
     * {@link VoltraxisPrefab} as part of initialization.</b>
     *
     * @param voltraxisEffectManager The {@link VoltraxisEffectManager}
     *                               object to attach.
     */
    public void attachVoltraxisEffectManager(VoltraxisEffectManager voltraxisEffectManager) {
        this.voltraxisEffectManager = voltraxisEffectManager;
    }

    /**
     * Attach {@link VoltraxisGroggy} to this object.<br>
     * <b>This function can only be called within
     * {@link VoltraxisPrefab} as part of initialization.</b>
     *
     * @param voltraxisGroggy The {@link VoltraxisGroggy}
     *                        object to attach.
     */
    public void attachVoltraxisGroggyGauge(VoltraxisGroggy voltraxisGroggy) {
        this.voltraxisGroggy = voltraxisGroggy;
        voltraxisGroggy.onGroggyReachedMax.addListener(this::groggyGauge_onGroggyReachedMax);
        voltraxisGroggy.onGroggyToDeployPowerCore.addListener(this::groggyGauge_onGroggyToDeployPowerCore);
    }

    /**
     * Attach {@link VoltraxisPowerCoreManager} to this object.
     * <b>This function can only be called within {@link VoltraxisPrefab}
     * as part of initialization.</b>
     *
     * @param voltraxisPowerCoreManager The {@link VoltraxisPowerCoreManager}
     *                                  to attach.
     */
    public void attachVoltraxisPowerCoreManager(VoltraxisPowerCoreManager voltraxisPowerCoreManager) {
        this.voltraxisPowerCoreManager = voltraxisPowerCoreManager;
        voltraxisPowerCoreManager.onPowerCoreDestroyed
                .addListener(this::voltraxisPowerCoreManager_onPowerCoreDestroyed);
        voltraxisPowerCoreManager.onPowerCoreDeployed
                .addListener(this::voltraxisPowerCoreManager_onPowerCoreDeployed);
    }

    /**
     * Link the charging state manager to Voltraxis.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link VoltraxisPrefab}
     * as part of component linking process.</i></b>
     *
     * @param voltraxisCharging The charging manager of Voltraxis.
     */
    public void linkVoltraxisCharging(VoltraxisCharging voltraxisCharging) {
        this.voltraxisCharging = voltraxisCharging;
    }

    /**
     * Called from {@link VoltraxisGroggy#onGroggyReachedMax}
     * when Voltraxis' groggy gauge has reached is maximum value. Thus,
     * Voltraxis will activate {@link #groggySkill()}.
     *
     * @param sender {@link VoltraxisGroggy}.
     * @param e      Empty event argument.
     */
    private void groggyGauge_onGroggyReachedMax(Object sender, Void e) {
        groggySkill();
    }

    /**
     * Called from {@link VoltraxisGroggy#onGroggyToDeployPowerCore}
     * when Voltraxis' groggy gauge surpasses the minimum amount to deploy
     * {@link PowerCore}s. This value is indicated through
     * {@link VoltraxisData#MIN_GROGGY_ON_POWER_CORE_DEPLOY}.
     *
     * @param sender {@link VoltraxisGroggy}.
     * @param e      Empty event argument.
     */
    private void groggyGauge_onGroggyToDeployPowerCore(Object sender, Void e) {
        voltraxisPowerCoreManager.spawnPowerCores();
    }

    /**
     * Called when {@link game.Voltraxis.Object.ElectricBall#onPaddleHit}
     * is invoked. This function deals damage to {@link game.Player.Player}
     * based on Voltraxis' current stat.
     *
     * @param sender {@link game.Voltraxis.Object.ElectricBall}.
     * @param e      Empty event argument.
     */
    private void electricBall_onPaddleHit(Object sender, Void e) {
        if (sender instanceof ElectricBall electricBall) {
            // TODO: caution when removing listener. - Aori
            electricBall.onPaddleHit
                    .removeListener(electricBallEventActionIDMap.get(electricBall));
            // TODO: damage player - Aori
        }
    }

    private void voltraxisPowerCoreManager_onPowerCoreDeployed(Object sender, PowerCore powerCore) {

        // Link destroyed event
        powerCore.setVoltraxis(this);

        // Add power core effect
        var powerCoreEffectInfo = new VoltraxisEffectManager.EffectInfo();
        powerCoreEffectInfo.index = VoltraxisData.EffectIndex.PowerCore;
        powerCoreEffectInfo.value = 0.0;
        powerCoreEffectInfo.effectEndingConstraint
                = (_) -> powerCore.getGameObject().isDestroyed();
        voltraxisEffectManager.addEffect(powerCoreEffectInfo, null);

        // Add effect on damage taken decrement
        var damageTakenDecrementEffectInfo = new VoltraxisEffectManager.EffectInfo();
        damageTakenDecrementEffectInfo.index = VoltraxisData.EffectIndex.DamageTakenDecrement;
        damageTakenDecrementEffectInfo.value = VoltraxisData.POWER_CORE_DAMAGE_TAKEN_REDUCTION;
        damageTakenDecrementEffectInfo.effectEndingConstraint
                = (_) -> powerCore.getGameObject().isDestroyed();
        voltraxisEffectManager.addEffect(damageTakenDecrementEffectInfo, null);

    }

    private void voltraxisPowerCoreManager_onPowerCoreDestroyed(Object sender, PowerCore e) {
        if(voltraxisPowerCoreManager.hasPowerCore()){
            voltraxisCharging.haltCharging();
        }else{
            voltraxisCharging.terminateCharging();
            // TODO: force the boss into weakened state - Aori
        }
    }

    // TODO: link charging state - Aori

}