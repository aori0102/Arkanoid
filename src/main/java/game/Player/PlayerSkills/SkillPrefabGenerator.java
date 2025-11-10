package game.Player.PlayerSkills;

import game.Player.PlayerSkills.Skills.Invincible;
import game.Player.PlayerSkills.Skills.LaserBeam.LaserBeam;
import game.Player.PlayerSkills.Skills.Dash;
import game.Player.PlayerSkills.Skills.Updraft;
import game.Player.PlayerSkills.Skills.Skill;
import org.Prefab.PrefabIndex;

import java.util.HashMap;

public class SkillPrefabGenerator {
    public static final HashMap<Class<? extends Skill>, SkillData> skillDataMap = new HashMap<>();
    public static final HashMap<Class<? extends Skill>, PrefabIndex> skillPrefabSet = new HashMap<>();

    static {
        skillPrefabSet.put(LaserBeam.class, PrefabIndex.LaserBeam);
        skillPrefabSet.put(Dash.class, PrefabIndex.Dash);
        skillPrefabSet.put(Updraft.class, PrefabIndex.Updraft);
        skillPrefabSet.put(Invincible.class, PrefabIndex.Invincible);

        skillDataMap.put(LaserBeam.class, new SkillData(5, 5.0, 5, 5.0));
        skillDataMap.put(Dash.class, new SkillData(2, 0.2, 2, 0.5));
        skillDataMap.put(Updraft.class, new SkillData(1, 5.0, 1, 5.0));
        skillDataMap.put(Invincible.class, new SkillData(1, 0, 1, 10));

    }

    public static class SkillData {
        public int skillCharge;
        public double skillCooldownTime;
        public final int maxSkillCharge;
        public final double baseSkillCooldown;

        public SkillData(int skillCharge, double skillCooldownTime, int maxSkillCharge, double baseSkillCooldown) {
            this.skillCharge = skillCharge;
            this.skillCooldownTime = skillCooldownTime;
            this.maxSkillCharge = maxSkillCharge;
            this.baseSkillCooldown = baseSkillCooldown;
        }
    }
}
