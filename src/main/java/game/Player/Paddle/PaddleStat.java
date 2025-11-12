package game.Player.Paddle;

import game.Entity.EntityStat;
import game.Player.PlayerAttributes;
import game.Player.PlayerSkills.SkillIndex;
import org.Event.EventHandler;
import org.GameObject.GameObject;

import java.util.EnumMap;

public final class PaddleStat extends EntityStat {

    private double attackMultiplier = 1;
    private double defenceMultiplier = 1;
    private double damageTakenMultiplier = 1;
    private double maxHealthMultiplier = 1;
    private double regenerationMultiplier = 1;
    private double movementSpeedMultiplier = 1;

    private final EnumMap<SkillIndex, Double> skillCooldownMultiplierMap = new EnumMap<>(SkillIndex.class);

    public EventHandler<Void> onStatChanged = new EventHandler<>(PaddleStat.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PaddleStat(GameObject owner) {
        super(owner);

        skillCooldownMultiplierMap.put(SkillIndex.LaserBeam, 1.0);
        skillCooldownMultiplierMap.put(SkillIndex.Dash, 1.0);
        skillCooldownMultiplierMap.put(SkillIndex.Invincible, 1.0);
        skillCooldownMultiplierMap.put(SkillIndex.Updraft, 1.0);
    }

    @Override
    public int getBaseAttack() {
        return PlayerAttributes.BASE_ATTACK;
    }

    @Override
    public int getBaseDefense() {
        return PlayerAttributes.BASE_DEFENSE;
    }

    @Override
    public double getAttackMultiplier() {
        return attackMultiplier;
    }

    @Override
    public double getDefenceMultiplier() {
        return defenceMultiplier;
    }

    @Override
    public double getDamageTakenMultiplier() {
        return damageTakenMultiplier;
    }

    @Override
    public double getRegenerationMultiplier() {
        return regenerationMultiplier;
    }

    @Override
    public double getCriticalChance() {
        return PlayerAttributes.CRITICAL_CHANGE;
    }

    @Override
    public double getCriticalDamage() {
        return PlayerAttributes.CRITICAL_DAMAGE;
    }

    @Override
    public int getBaseMaxHealth() {
        return PlayerAttributes.MAX_HEALTH;
    }

    @Override
    public double getMaxHealthMultiplier() {
        return maxHealthMultiplier;
    }

    @Override
    public double getBaseMovementSpeed() {
        return PlayerAttributes.BASE_MOVEMENT_SPEED;
    }

    @Override
    public double getMovementSpeedMultiplier() {
        return movementSpeedMultiplier;
    }

    public double getSkillCooldownMultiplier(SkillIndex skillIndex) {
        return skillCooldownMultiplierMap.get(skillIndex);
    }

    public double getSkillCooldown(SkillIndex skillIndex) {
        return PlayerAttributes.BASE_SKILL_COOLDOWN_MAP.get(skillIndex)
                * skillCooldownMultiplierMap.get(skillIndex);
    }

    public void setDamageTakenMultiplier(double value) {
        damageTakenMultiplier = value;
        onStatChanged.invoke(this, null);
    }

    public void setRegenerationMultiplier(double value) {
        regenerationMultiplier = value;
        onStatChanged.invoke(this, null);
    }

    public void setMovementSpeedMultiplier(double value) {
        movementSpeedMultiplier = value;
        onStatChanged.invoke(this, null);
    }

    public void setAttackMultiplier(double value) {
        attackMultiplier = value;
        onStatChanged.invoke(this, null);
    }

    public void setDefenceMultiplier(double value) {
        defenceMultiplier = value;
        onStatChanged.invoke(this, null);
    }

    public void setMaxHealthMultiplier(double maxHealthMultiplier) {
        this.maxHealthMultiplier = maxHealthMultiplier;
        onStatChanged.invoke(this, null);
    }

    public void setSkillCooldownMultiplier(SkillIndex skillIndex, double invincibilityMultiplier) {
        skillCooldownMultiplierMap.put(skillIndex, invincibilityMultiplier);
        onStatChanged.invoke(this, null);
    }

}
