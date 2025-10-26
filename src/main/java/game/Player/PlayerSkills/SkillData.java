package game.Player.PlayerSkills;

import java.util.HashMap;

public class SkillData {

    public static final HashMap<Class<? extends Skill>, Integer> skillCostMap = new HashMap<>();
    static {
        skillCostMap.put(LaserBeam.class, 5);
    }

}
