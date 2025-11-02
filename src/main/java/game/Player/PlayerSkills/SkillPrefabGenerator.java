package game.Player.PlayerSkills;

import game.Player.PlayerSkills.PlayerSkillsPrefab.Dash;
import game.Player.PlayerSkills.PlayerSkillsPrefab.LaserBeamPrefab;
import game.Player.PlayerSkills.PlayerSkillsPrefab.SkillPrefab;

import java.util.HashMap;

public class SkillPrefabGenerator {
    public static final HashMap<Class<? extends Skill>, SkillData> skillDataMap = new HashMap<>();
    public static final HashMap<Class<? extends Skill>, SkillPrefab> skillPrefabSet = new HashMap<>();

    static {
        skillPrefabSet.put(LaserBeam.class, new LaserBeamPrefab());



        skillDataMap.put(LaserBeam.class, new SkillData(5, 5.0, 5, 5.0));
        skillDataMap.put(Dash.class, new SkillData(2, 0.2, 2, 0.2));
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
