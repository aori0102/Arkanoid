package game.Player.PlayerSkills;

import game.Player.PlayerSkills.PlayerSkillsPrefab.LaserBeamPrefab;
import game.Player.PlayerSkills.PlayerSkillsPrefab.SkillPrefab;

import java.util.HashMap;

public final class SkillPrefabGenerator {
    public static final HashMap<Class<? extends Skill>, SkillPrefab> skillPrefabSet = new HashMap<>();

    static {
        skillPrefabSet.put(LaserBeam.class, new LaserBeamPrefab());

    }

}
