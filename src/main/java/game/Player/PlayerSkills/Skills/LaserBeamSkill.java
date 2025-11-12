package game.Player.PlayerSkills.Skills;

import game.Player.Player;
import game.Player.PlayerSkills.SkillIndex;
import org.GameObject.GameObject;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;

/**
 * LaserBeamSkill spawns a laser beam at the player's paddle position.
 * This skill allows the player to shoot a laser straight up.
 */
public final class LaserBeamSkill extends Skill {

    /**
     * Create this LaserBeamSkill.
     *
     * @param owner The owner GameObject of this skill (typically the player).
     */
    public LaserBeamSkill(GameObject owner) {
        super(owner);
    }

    /**
     * Returns the skill index representing this skill.
     *
     * @return SkillIndex.LaserBeam
     */
    @Override
    protected SkillIndex getSkillIndex() {
        return SkillIndex.LaserBeam;
    }

    /**
     * Activates the LaserBeam skill:
     * - Instantiates a LaserBeam prefab
     * - Positions it at the player's paddle current position
     */
    @Override
    public void invoke() {
        PrefabManager.instantiatePrefab(PrefabIndex.LaserBeam)
                .getTransform().setGlobalPosition(
                        Player.getInstance().getPlayerPaddle().getTransform().getGlobalPosition()
                );
    }

}
