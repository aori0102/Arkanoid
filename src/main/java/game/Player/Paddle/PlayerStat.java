package game.Player.Paddle;

import game.Entity.EntityStat;
import game.Player.PlayerAttributes;
import game.Player.PlayerData.DataManager;
import game.Player.PlayerData.IPlayerProgressHolder;
import game.Player.PlayerSkills.SkillIndex;
import org.Event.EventHandler;
import org.GameObject.GameObject;

import java.util.EnumMap;

public final class PlayerStat extends EntityStat implements
        IPlayerProgressHolder {

    public enum PlayerStatIndex {
        Attack,
        Defense,
        DamageTaken,
        MaxHealth,
        MovementSpeed,
    }

    private final EnumMap<PlayerStatIndex, Double> statMultiplierMap = new EnumMap<>(PlayerStatIndex.class);
    private final EnumMap<SkillIndex, Double> skillCooldownMultiplierMap = new EnumMap<>(SkillIndex.class);

    public EventHandler<PlayerStatIndex> onStatChanged = new EventHandler<>(PlayerStat.class);
    public EventHandler<SkillIndex> onSkillCooldownChanged = new EventHandler<>(PlayerStat.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerStat(GameObject owner) {
        super(owner);

        statMultiplierMap.put(PlayerStatIndex.Attack, 1.0);
        statMultiplierMap.put(PlayerStatIndex.Defense, 1.0);
        statMultiplierMap.put(PlayerStatIndex.DamageTaken, 1.0);
        statMultiplierMap.put(PlayerStatIndex.MaxHealth, 1.0);
        statMultiplierMap.put(PlayerStatIndex.MovementSpeed, 1.0);

        skillCooldownMultiplierMap.put(SkillIndex.LaserBeam, 1.0);
        skillCooldownMultiplierMap.put(SkillIndex.Dash, 1.0);
        skillCooldownMultiplierMap.put(SkillIndex.Invincible, 1.0);
        skillCooldownMultiplierMap.put(SkillIndex.Updraft, 1.0);
    }

    @Override
    public void awake() {
        loadProgress();
    }

    @Override
    public void loadProgress() {

        var progress = DataManager.getInstance().getProgress();

        var skillCooldownMultiplier = progress.getSkillCooldownMultiplierArray();
        for (var index : SkillIndex.values()) {
            setSkillCooldownMultiplier(index, skillCooldownMultiplier[index.ordinal()]);
        }

        var statMultiplier = progress.getStatMultiplierArray();
        for (var index : PlayerStatIndex.values()) {
            setStatMultiplier(index, statMultiplier[index.ordinal()]);
        }

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
        return statMultiplierMap.get(PlayerStatIndex.Attack);
    }

    @Override
    public double getDefenceMultiplier() {
        return statMultiplierMap.get(PlayerStatIndex.Defense);
    }

    @Override
    public double getDamageTakenMultiplier() {
        return statMultiplierMap.get(PlayerStatIndex.DamageTaken);
    }

    @Override
    public double getRegenerationMultiplier() {
        return 1;
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
        return statMultiplierMap.get(PlayerStatIndex.MaxHealth);
    }

    @Override
    public double getBaseMovementSpeed() {
        return PlayerAttributes.BASE_MOVEMENT_SPEED;
    }

    @Override
    public double getMovementSpeedMultiplier() {
        return statMultiplierMap.get(PlayerStatIndex.MovementSpeed);
    }

    public double getSkillCooldownMultiplier(SkillIndex skillIndex) {
        return skillCooldownMultiplierMap.get(skillIndex);
    }

    public double getSkillCooldown(SkillIndex skillIndex) {
        return PlayerAttributes.BASE_SKILL_COOLDOWN_MAP.get(skillIndex)
                * skillCooldownMultiplierMap.get(skillIndex);
    }

    public double getStatMultiplier(PlayerStatIndex playerStatIndex) {
        return statMultiplierMap.get(playerStatIndex);
    }

    public void setStatMultiplier(PlayerStatIndex playerStatIndex, double statMultiplier) {
        statMultiplierMap.put(playerStatIndex, statMultiplier);
        onStatChanged.invoke(this, null);
    }

    public void setSkillCooldownMultiplier(SkillIndex skillIndex, double invincibilityMultiplier) {
        skillCooldownMultiplierMap.put(skillIndex, invincibilityMultiplier);
        onSkillCooldownChanged.invoke(this, skillIndex);
    }

}
