package game.Voltraxis;

import org.GameObject;
import org.GameObjectManager;
import org.MonoBehaviour;
import utils.Time;
import utils.Vector2;

public class Voltraxis extends MonoBehaviour {

    private double basicCooldownMultiplier;
    private GameObject electricBallPrefab;
    private PowerCore leftCore;
    private PowerCore rightCore;

    /// Stats
    private int health;
    private double attackMultiplier;
    private double defenseMultiplier;
    private double damageTakenProportion;

    public Voltraxis(GameObject owner) {

        super(owner);

        basicCooldownMultiplier = 1.0;

        Time.addCoroutine(this::enhanceSkill, Data.ENHANCE_SKILL_COOLDOWN);
        Time.addCoroutine(this::basicSkill, Data.BASIC_SKILL_COOLDOWN * basicCooldownMultiplier);

        electricBallPrefab = GameObjectManager.instantiate("Electric ball");
        electricBallPrefab.addComponent(ElectricBall.class);
        electricBallPrefab.setActive(false);

        health = Data.BASE_MAX_HEALTH;
        attackMultiplier = 1.0;
        defenseMultiplier = 1.0;
        damageTakenProportion = 1.0;

        leftCore = null;
        rightCore = null;

    }

    /**
     * <b>Voltraxis' basic skill: Arc Discharge</b><br>
     * Fires up to <b>5</b> electric orbs at varying angles
     * toward the player, each dealing <b>63.2%</b> ATK. If hit,
     * the player is stunned for <b>3s</b>.
     */
    private void basicSkill() {

        Vector2[] directionArray = {
                new Vector2(0.0, 1.0),
                new Vector2(-0.2, 1.0),
                new Vector2(-0.4, 1.0),
                new Vector2(0.2, 1.0),
                new Vector2(0.4, 1.0),
        };
        for (Vector2 direction : directionArray) {
            var electricBallObject = GameObjectManager.instantiate(electricBallPrefab);
            var electricBall = electricBallObject.getComponent(ElectricBall.class);
            electricBall.setDamage(
                    (int) (Data.BASE_ATTACK * attackMultiplier * Data.ELECTRIC_BALL_ATTACK_PROPORTION)
            );
            electricBall.setDirection(direction);
        }

        Time.addCoroutine(this::basicSkill, Data.BASIC_SKILL_COOLDOWN * basicCooldownMultiplier);

    }

    /**
     * <b>Voltraxis' enhance skill: Power Surge</b><br>
     * Increases ATK by <b>10%</b> every <b>30s</b> for <b>20s</b>.
     */
    private void enhanceSkill() {

        attackMultiplier += Data.ENHANCE_ATTACK_INCREMENT;

        Time.addCoroutine(this::onEnhanceSkillEnd, Time.time + Data.ENHANCE_SKILL_DURATION);
        Time.addCoroutine(this::enhanceSkill, Time.time + Data.ENHANCE_SKILL_COOLDOWN);

    }

    /**
     * Called upon enhance skill duration ends.
     */
    private void onEnhanceSkillEnd() {
        attackMultiplier -= Data.ENHANCE_ATTACK_INCREMENT;
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

        basicCooldownMultiplier -= Data.GROGGY_BASIC_COOLDOWN_REDUCTION;
        attackMultiplier += Data.GROGGY_ATTACK_INCREMENT;

        Time.addCoroutine(this::exSkill, Data.GROGGY_TO_EX_CHARGE_TIME);

    }

    /**
     * <b>Voltraxis' EX skill: Sword of Light, Super Nova</b><br>
     * Voltraxis unleashes a devastating electric slash, dealing <b>247%</b> ATK.
     */
    protected void exSkill() {

        /*
        EX Skill - Sword of Light, Super Nova

        */

        // Check if EX is disrupted (both core destroyed)
        if (leftCore != null || rightCore != null) {
            System.out.println("Slash!!!!");
        }

    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new Voltraxis(newOwner);
    }

    @Override
    protected void destroyComponent() {
        GameObjectManager.destroy(electricBallPrefab);
        electricBallPrefab = null;
        GameObjectManager.destroy(leftCore.getGameObject());
        leftCore = null;
        GameObjectManager.destroy(rightCore.getGameObject());
        rightCore = null;
    }

    /**
     * Spawn two power cores on either side of the board.
     */
    private void spawnPowerCores() {

        // Left
        var leftCoreObject = GameObjectManager.instantiate("LeftCore");
        var leftCore = leftCoreObject.addComponent(PowerCore.class);
        leftCore.onPowerCoreDestroyed.addListener(this::powerCore_onPowerCoreDestroyed);
        leftCore.setHealth((int) (Data.BASE_MAX_HEALTH * Data.POWER_CORE_PROPORTIONAL_HEALTH));
        damageTakenProportion -= Data.POWER_CORE_DAMAGE_TAKEN_REDUCTION;
        this.leftCore = leftCore;

        // Right
        var rightCoreObject = GameObjectManager.instantiate("RightCore");
        var rightCore = rightCoreObject.addComponent(PowerCore.class);
        rightCore.onPowerCoreDestroyed.addListener(this::powerCore_onPowerCoreDestroyed);
        rightCore.setHealth((int) (Data.BASE_MAX_HEALTH * Data.POWER_CORE_PROPORTIONAL_HEALTH));
        damageTakenProportion -= Data.POWER_CORE_DAMAGE_TAKEN_REDUCTION;
        this.rightCore = rightCore;

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
            damageTakenProportion += Data.POWER_CORE_DAMAGE_TAKEN_REDUCTION;

        }

    }

}
