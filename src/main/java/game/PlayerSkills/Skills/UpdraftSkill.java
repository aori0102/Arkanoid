package game.PlayerSkills.Skills;

import game.Ball.BallsManager;
import game.PlayerSkills.SkillIndex;
import org.GameObject.GameObject;
import utils.Vector2;

/**
 * Represents the Updraft skill for the player.
 * <p>
 * When invoked, this skill flips the vertical direction of all balls
 * moving downward (positive Y direction) to upward (negative Y direction),
 * effectively “lifting” the balls back up.
 * </p>
 */
public class UpdraftSkill extends Skill {

    /**
     * Create this skill component.
     *
     * @param owner The GameObject that owns this skill (usually the player).
     */
    public UpdraftSkill(GameObject owner) {
        super(owner);
    }

    /**
     * Invokes the Updraft skill effect.
     * <p>
     * Iterates over all active balls in the game. If a ball is moving downward,
     * its vertical direction is inverted and normalized to maintain consistent speed.
     */
    @Override
    public void invoke() {
        for (var ball : BallsManager.getInstance().getBallSet()) {
            Vector2 ballDirection = ball.getDirection();
            if (ballDirection.y > 0) { // Only affect balls moving downward
                Vector2 newDirection = new Vector2(ballDirection.x, ballDirection.y * (-1));
                ball.setDirection(newDirection.normalize());
            }
        }
    }

    /**
     * Returns the SkillIndex associated with this skill.
     *
     * @return SkillIndex.Updraft
     */
    @Override
    protected SkillIndex getSkillIndex() {
        return SkillIndex.Updraft;
    }

}
