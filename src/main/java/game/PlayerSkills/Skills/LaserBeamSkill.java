package game.PlayerSkills.Skills;

import game.Player.Player;
import game.PlayerSkills.SkillIndex;
import org.GameObject.GameObject;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;

public final class LaserBeamSkill extends Skill {
    @Override
    protected SkillIndex getSkillIndex() {
        return SkillIndex.LaserBeam;
    }

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public LaserBeamSkill(GameObject owner) {
        super(owner);
    }

    @Override
    public void invoke() {
        PrefabManager.instantiatePrefab(PrefabIndex.LaserBeam)
                .getTransform().setGlobalPosition(
                        Player.getInstance().getPlayerPaddle().getTransform().getGlobalPosition()
                );
    }

}