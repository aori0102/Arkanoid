package game.Voltraxis;

import game.Voltraxis.Interface.IBossTarget;
import game.Voltraxis.Object.ElectricBall;
import game.Voltraxis.Object.PowerCore;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import utils.Time;
import utils.Vector2;

/**
 * Main class of the Voltraxis boss. Handle central logic
 * (damage, health, status...) and skills.
 */
public class Voltraxis extends MonoBehaviour implements IBossTarget {

    private static final Vector2 LEFT_CORE_POSITION = new Vector2(200.0, 400.0);
    private static final Vector2 RIGHT_CORE_POSITION = new Vector2(800.0, 400.0);

    private PowerCore leftCore = null;
    private PowerCore rightCore = null;
    private VoltraxisEffectManager voltraxisEffectManager = null;

    /// Stats
    private int health = VoltraxisData.BASE_MAX_HEALTH;

    public EventHandler<Void> onHealthChanged = new EventHandler<>(this);
    public EventHandler<Void> onDamaged = new EventHandler<>(this);
    private boolean powerCoreDeployed = false;

    public Voltraxis(GameObject owner) {
        super(owner);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new Voltraxis(newOwner);
    }

    @Override
    protected void destroyComponent() {
        GameObjectManager.destroy(leftCore.getGameObject());
        leftCore = null;
        GameObjectManager.destroy(rightCore.getGameObject());
        rightCore = null;
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
     * <b>Voltraxis' basic skill: Arc Discharge</b><br>
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
            electricBall.onPaddleHit.addListener(this::electricBall_onPaddleHit);
            electricBall.getTransform().setGlobalPosition(getTransform().getGlobalPosition());
            electricBall.setDirection(direction);

        }

        // Repeat basic skill after cooldown
        Time.addCoroutine(this::basicSkill, Time.time + getBasicSkillCooldown());

    }

    /**
     * <b>Voltraxis' enhance skill: Power Surge</b><br>
     * Increases ATK by <b>10%</b> every <b>30s</b> for <b>20s</b>.
     */
    private void enhanceSkill() {

        // Add attack increment effect
        voltraxisEffectManager.addEffect(new VoltraxisEffectManager.EffectInfo(
                VoltraxisData.EffectIndex.AttackIncrement,
                VoltraxisData.ENHANCE_ATTACK_INCREMENT,
                (delta) -> delta >= VoltraxisData.ENHANCE_SKILL_DURATION,
                null
        ));

        // Repeat enhance skill
        Time.addCoroutine(this::enhanceSkill, Time.time + VoltraxisData.ENHANCE_SKILL_COOLDOWN);

    }

    /**
     * <b>Voltraxis' groggy skill: Overhaul</b><br>
     * When the Groggy Gauge is filled, Voltraxis enters the
     * <b>Overhaul</b> state. In this mode, Arc Discharge cooldown
     * is reduced by <b>35%</b> and ATK is increased by <b>20%</b>.
     * When entering <b>Overhaul</b> state, Voltraxis begins charging
     * toward <b>Super Nova</b>.
     */
    private void groggySkill() {

        // Add attack increment effect
        voltraxisEffectManager.addEffect(new VoltraxisEffectManager.EffectInfo(
                VoltraxisData.EffectIndex.AttackIncrement,
                VoltraxisData.GROGGY_ATTACK_INCREMENT,
                (delta) -> delta >= VoltraxisData.GROGGY_DURATION,
                null
        ));
        // Add skill cooldown reduction effect
        voltraxisEffectManager.addEffect(new VoltraxisEffectManager.EffectInfo(
                VoltraxisData.EffectIndex.SkillCooldownDecrement,
                VoltraxisData.GROGGY_BASIC_COOLDOWN_REDUCTION,
                (delta) -> delta >= VoltraxisData.GROGGY_DURATION,
                null
        ));
        // Add charging effect
        voltraxisEffectManager.addEffect(new VoltraxisEffectManager.EffectInfo(
                VoltraxisData.EffectIndex.ChargingEX,
                0.0,
                (delta) -> delta >= VoltraxisData.GROGGY_TO_EX_CHARGE_TIME,
                null
        ));

        // Charge towards EX Skill
        Time.addCoroutine(this::exSkill, VoltraxisData.GROGGY_TO_EX_CHARGE_TIME);

    }

    /**
     * <b>Voltraxis' EX skill: Sword of Light, Super Nova</b><br>
     * Voltraxis unleashes a devastating electric slash, dealing <b>247%</b> ATK.
     */
    protected void exSkill() {

        // Check if EX is disrupted (both core destroyed)
        if (leftCore != null || rightCore != null) {
            System.out.println("Slash!!!!");
        }

    }

    /**
     * Spawn two power cores on either side of the board.
     */
    private void spawnPowerCores() {
        if (powerCoreDeployed) {
            return;
        }
        powerCoreDeployed = true;
        leftCore = spawnCore(LEFT_CORE_POSITION);
        System.out.println(leftCore);
        rightCore = spawnCore(RIGHT_CORE_POSITION);
        System.out.println(rightCore);
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
        PowerCore newCore = VoltraxisPrefab.instantiatePowerCore();
        System.out.println("a:" + newCore);
        newCore.getTransform().setGlobalPosition(position);
        int powerCoreHealth = (int) (VoltraxisData.BASE_MAX_HEALTH * VoltraxisData.POWER_CORE_PROPORTIONAL_HEALTH);
        newCore.setHealth(powerCoreHealth);

        // Add power core effect
        voltraxisEffectManager.addEffect(new VoltraxisEffectManager.EffectInfo(
                VoltraxisData.EffectIndex.PowerCore,
                0.0,
                (_) -> newCore.getGameObject().isDestroyed(),
                null
        ));

        // Add effect on damage taken decrement
        voltraxisEffectManager.addEffect(new VoltraxisEffectManager.EffectInfo(
                VoltraxisData.EffectIndex.DamageTakenDecrement,
                VoltraxisData.POWER_CORE_DAMAGE_TAKEN_REDUCTION,
                (_) -> newCore.getGameObject().isDestroyed(),
                null
        ));

        // Link destroyed event
        newCore.onPowerCoreDestroyed.addListener(this::powerCore_onPowerCoreDestroyed);

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
            } else if (core == rightCore) {
                rightCore = null;
            } else {
                throw new RuntimeException("Invalid power core");
            }

            core.onPowerCoreDestroyed.removeListener(this::powerCore_onPowerCoreDestroyed);

        }

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
     * Attach {@link VoltraxisGroggyGauge} to this object.<br>
     * <b>This function can only be called within
     * {@link VoltraxisPrefab} as part of initialization.</b>
     *
     * @param voltraxisGroggyGauge The {@link VoltraxisGroggyGauge}
     *                             object to attach.
     */
    public void attachVoltraxisGroggyGauge(VoltraxisGroggyGauge voltraxisGroggyGauge) {
        voltraxisGroggyGauge.onGroggyReachedMax.addListener(this::groggyGauge_onGroggyReachedMax);
        voltraxisGroggyGauge.onGroggyToDeployPowerCore.addListener(this::groggyGauge_onGroggyToDeployPowerCore);
    }

    /**
     * Called from {@link VoltraxisGroggyGauge#onGroggyReachedMax}
     * when Voltraxis' groggy gauge has reached is maximum value. Thus,
     * Voltraxis will activate {@link #groggySkill()}.
     *
     * @param sender {@link VoltraxisGroggyGauge}.
     * @param e      Empty event argument.
     */
    private void groggyGauge_onGroggyReachedMax(Object sender, Void e) {
        groggySkill();
    }

    /**
     * Called from {@link VoltraxisGroggyGauge#onGroggyToDeployPowerCore}
     * when Voltraxis' groggy gauge surpasses the minimum amount to deploy
     * {@link PowerCore}s. This value is indicated through
     * {@link VoltraxisData#MIN_GROGGY_ON_POWER_CORE_DEPLOY}.
     *
     * @param sender {@link VoltraxisGroggyGauge}.
     * @param e      Empty event argument.
     */
    private void groggyGauge_onGroggyToDeployPowerCore(Object sender, Void e) {
        spawnPowerCores();
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
            electricBall.onPaddleHit.removeListener(this::electricBall_onPaddleHit);
            // TODO: damage player
        }
    }

}