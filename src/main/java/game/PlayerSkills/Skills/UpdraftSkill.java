package game.PlayerSkills.Skills;

import game.Ball.BallsManager;
import game.PlayerSkills.SkillIndex;
import org.GameObject.GameObject;
import utils.Vector2;

public class UpdraftSkill extends Skill {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public UpdraftSkill(GameObject owner) {
        super(owner);
    }

    @Override
    public void invoke() {
        for (var ball : BallsManager.getInstance().getBallSet()) {
            Vector2 ballDirection = ball.getDirection();
            if (ballDirection.y > 0) {
                Vector2 newDirection = new Vector2(ballDirection.x, ballDirection.y * (-1));
                ball.setDirection(newDirection.normalize());
            }
        }
    }

    @Override
    protected SkillIndex getSkillIndex() {
        return SkillIndex.Updraft;
    }

}