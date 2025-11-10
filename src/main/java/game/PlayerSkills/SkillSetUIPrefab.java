package game.PlayerSkills;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.Vector2;

import java.util.EnumMap;

public final class SkillSetUIPrefab extends Prefab {

    private static final EnumMap<SkillIndex, Vector2> skillPositionMap = new EnumMap<>(SkillIndex.class);

    static {
        skillPositionMap.put(SkillIndex.LaserBeam, new Vector2(1000.0, 750.0));
        skillPositionMap.put(SkillIndex.Updraft, new Vector2(1080.0, 750.0));
        skillPositionMap.put(SkillIndex.Invincible, new Vector2(1150.0, 750.0));
        skillPositionMap.put(SkillIndex.Dash, new Vector2(1150.0, 680.0));
    }

    @Override
    public GameObject instantiatePrefab() {

        var skillSetUIObject = GameObjectManager.instantiate("SkillSetUI");

        // Laser beam
        var laserBeamSkillUI = PrefabManager.instantiatePrefab(PrefabIndex.SkillUI)
                .getComponent(SkillUI.class);
        laserBeamSkillUI.setSkillIndex(SkillIndex.LaserBeam);
        laserBeamSkillUI.getTransform().setLocalPosition(skillPositionMap.get(SkillIndex.LaserBeam));
        laserBeamSkillUI.getGameObject().setParent(skillSetUIObject);

        // Invincible
        var invincibleSkillUI = PrefabManager.instantiatePrefab(PrefabIndex.SkillUI)
                .getComponent(SkillUI.class);
        invincibleSkillUI.setSkillIndex(SkillIndex.Invincible);
        invincibleSkillUI.getTransform().setLocalPosition(skillPositionMap.get(SkillIndex.Invincible));
        invincibleSkillUI.getGameObject().setParent(skillSetUIObject);

        // Updraft
        var updraftSkillUI = PrefabManager.instantiatePrefab(PrefabIndex.SkillUI)
                .getComponent(SkillUI.class);
        updraftSkillUI.setSkillIndex(SkillIndex.Updraft);
        updraftSkillUI.getTransform().setLocalPosition(skillPositionMap.get(SkillIndex.Updraft));
        updraftSkillUI.getGameObject().setParent(skillSetUIObject);

        // Dash
        var dashSkillUI = PrefabManager.instantiatePrefab(PrefabIndex.SkillUI)
                .getComponent(SkillUI.class);
        dashSkillUI.setSkillIndex(SkillIndex.Dash);
        dashSkillUI.getTransform().setLocalPosition(skillPositionMap.get(SkillIndex.Dash));
        dashSkillUI.getGameObject().setParent(skillSetUIObject);

        return skillSetUIObject;

    }
}
