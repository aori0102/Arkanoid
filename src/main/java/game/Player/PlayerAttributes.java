package game.Player;

import game.Player.PlayerSkills.SkillIndex;

import java.util.EnumMap;

public final class PlayerAttributes {

    public static final int MAX_HEALTH = 100;
    public static final int MAX_LIVES = 3;

    public static final int BASE_ATTACK = 20;
    public static final int BASE_DEFENSE = 20;
    public static final double CRITICAL_CHANGE = 0.1;
    public static final double CRITICAL_DAMAGE = 1.5;
    public static final double BASE_MOVEMENT_SPEED = 800;

    public static final EnumMap<SkillIndex, Double> BASE_SKILL_COOLDOWN_MAP = new EnumMap<>(SkillIndex.class);

    static {
        BASE_SKILL_COOLDOWN_MAP.put(SkillIndex.LaserBeam, 5.0);
        BASE_SKILL_COOLDOWN_MAP.put(SkillIndex.Dash, 0.2);
        BASE_SKILL_COOLDOWN_MAP.put(SkillIndex.Updraft, 5.0);
        BASE_SKILL_COOLDOWN_MAP.put(SkillIndex.Invincible, 10.0);
    }

}
