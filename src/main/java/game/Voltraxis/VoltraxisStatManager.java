package game.Voltraxis;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public final class VoltraxisStatManager extends MonoBehaviour {

    private double attackMultiplier = 1.0;
    private double defenseMultiplier = 1.0;
    private double basicCooldownMultiplier = 1.0;
    private double damageTakenProportion = 1.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisStatManager(GameObject owner) {
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
                damageTakenProportion -= e.value;
                break;

            case VoltraxisData.EffectIndex.DamageTakenIncrement:
                damageTakenProportion += e.value;
                break;

            case VoltraxisData.EffectIndex.SkillCooldownDecrement:
                basicCooldownMultiplier -= e.value;
                break;

            case VoltraxisData.EffectIndex.Frostbite:
                damageTakenProportion += VoltraxisData.FROST_BITE_DAMAGE_TAKEN_INCREMENT;
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
                damageTakenProportion += e.value;
                break;

            case VoltraxisData.EffectIndex.DamageTakenIncrement:
                damageTakenProportion -= e.value;
                break;

            case VoltraxisData.EffectIndex.SkillCooldownDecrement:
                basicCooldownMultiplier += e.value;
                break;

            case VoltraxisData.EffectIndex.Frostbite:
                damageTakenProportion -= VoltraxisData.FROST_BITE_DAMAGE_TAKEN_INCREMENT;

        }

    }

    /**
     * Get the overall ATK after applying all modifiers.
     *
     * @return The overall ATK.
     */
    public double getAttackMultiplier() {
        return attackMultiplier;
    }

    /**
     * Get the overall DEF after applying all modifiers.
     *
     * @return The overall DEF.
     */
    public double getDefenseMultiplier() {
        return defenseMultiplier;
    }

    /**
     * Get the overall DMG taken proportion after applying
     * all modifiers.
     *
     * @return The overall DMG taken proportion.
     */
    public double getDamageTakenProportion() {
        return damageTakenProportion;
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
     * Get Voltraxis' current DEF, with modifier included.
     *
     * @return Voltraxis' current DEF.
     */
    public int getDefence() {
        return (int) (getDefenseMultiplier() * VoltraxisData.BASE_DEFENSE);
    }

    /**
     * Get Voltraxis' current ATK, with modifier included.
     *
     * @return Voltraxis' current DEF.
     */
    public int getAttack() {
        return (int) (getAttackMultiplier() * VoltraxisData.BASE_ATTACK);
    }

    /**
     * Get Voltraxis' basic skill cooldown, with modifier included.
     *
     * @return Voltraxis' basic skill cooldown.
     */
    public double getBasicSkillCooldown() {
        return VoltraxisData.BASIC_SKILL_COOLDOWN * getBasicCooldownMultiplier();
    }

    public void takeDamage(int amount){
    }

}