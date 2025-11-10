package game.Voltraxis;

import game.Entity.EntityStat;
import org.GameObject.GameObject;

public final class VoltraxisStat extends EntityStat {

    private double attackMultiplier = 1.0;
    private double defenseMultiplier = 1.0;
    private double basicCooldownMultiplier = 1.0;
    private double damageTakenMultiplier = 1.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisStat(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        Voltraxis.getInstance().getVoltraxisEffectManager().onEffectAdded
                .addListener(this::voltraxisEffectManager_onEffectAdded);
        Voltraxis.getInstance().getVoltraxisEffectManager().onEffectRemoved
                .addListener(this::voltraxisEffectManager_onEffectRemoved);
    }

    /**
     * Get the overall ATK after applying all modifiers.
     *
     * @return The overall ATK.
     */
    @Override
    public double getAttackMultiplier() {
        return attackMultiplier;
    }

    @Override
    public int getAttack() {
        return VoltraxisData.BASE_ATTACK;
    }

    @Override
    public int getDefence() {
        return VoltraxisData.BASE_DEFENSE;
    }

    @Override
    public double getDefenceMultiplier() {
        return defenseMultiplier;
    }

    @Override
    public double getDamageTakenMultiplier() {
        return damageTakenMultiplier;
    }

    @Override
    public double getRegenerationMultiplier() {
        return 0;
    }

    @Override
    public double getCriticalChance() {
        return VoltraxisData.CRITICAL_CHANCE;
    }

    @Override
    public double getCriticalDamage() {
        return VoltraxisData.CRITICAL_DAMAGE;
    }

    @Override
    public int getMaxHealth() {
        return VoltraxisData.BASE_MAX_HEALTH;
    }

    @Override
    public double getMovementSpeed() {
        return 0;
    }

    @Override
    public double getMovementSpeedMultiplier() {
        return 0;
    }

    /**
     * Called when {@link VoltraxisEffectManager#onEffectAdded} is invoked.<br><br>
     * This function handles Voltraxis' stat modifiers when a new effect is added.
     *
     * @param sender Event caller, {@link VoltraxisEffectManager}.
     * @param e      Event argument containing effect info.
     */
    private void voltraxisEffectManager_onEffectAdded(Object sender, VoltraxisEffectManager.EffectInputInfo e) {

        switch (e.index) {

            case VoltraxisData.EffectIndex.AttackIncrement:
                attackMultiplier += e.value;
                break;

            case VoltraxisData.EffectIndex.DefenceReduction:
                defenseMultiplier -= e.value;
                break;

            case VoltraxisData.EffectIndex.DamageTakenDecrement:
                damageTakenMultiplier -= e.value;
                break;

            case VoltraxisData.EffectIndex.DamageTakenIncrement:
                damageTakenMultiplier += e.value;
                break;

            case VoltraxisData.EffectIndex.SkillCooldownDecrement:
                basicCooldownMultiplier -= e.value;
                break;

            case VoltraxisData.EffectIndex.Frostbite:
                damageTakenMultiplier += VoltraxisData.FROST_BITE_DAMAGE_TAKEN_INCREMENT;
                break;

        }

    }

    /**
     * Called when {@link VoltraxisEffectManager#onEffectRemoved} is invoked.<br><br>
     * This function handles Voltraxis' stat modifiers when an effect is removed.
     *
     * @param sender Event caller, {@link VoltraxisEffectManager}
     * @param e      Event argument containing effect info.
     */
    private void voltraxisEffectManager_onEffectRemoved(Object sender, VoltraxisEffectManager.EffectInputInfo e) {

        switch (e.index) {

            case VoltraxisData.EffectIndex.AttackIncrement:
                attackMultiplier -= e.value;
                break;

            case VoltraxisData.EffectIndex.DefenceReduction:
                defenseMultiplier += e.value;
                break;

            case VoltraxisData.EffectIndex.DamageTakenDecrement:
                damageTakenMultiplier += e.value;
                break;

            case VoltraxisData.EffectIndex.DamageTakenIncrement:
                damageTakenMultiplier -= e.value;
                break;

            case VoltraxisData.EffectIndex.SkillCooldownDecrement:
                basicCooldownMultiplier += e.value;
                break;

            case VoltraxisData.EffectIndex.Frostbite:
                damageTakenMultiplier -= VoltraxisData.FROST_BITE_DAMAGE_TAKEN_INCREMENT;

        }

    }

    /**
     * Get the overall cooldown of Voltraxis' basic skill
     * after applying all modifiers.
     *
     * @return The overall basic skill cooldown of Voltraxis.
     */
    public double getBasicCooldownMultiplier() {
        return basicCooldownMultiplier;
    }

    /**
     * Get Voltraxis' basic skill cooldown, with modifier included.
     *
     * @return Voltraxis' basic skill cooldown.
     */
    public double getBasicSkillCooldown() {
        return VoltraxisData.BASIC_SKILL_COOLDOWN * getBasicCooldownMultiplier();
    }

}