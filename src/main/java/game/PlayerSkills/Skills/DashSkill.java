package game.PlayerSkills.Skills;

import game.Player.Player;
import game.PlayerSkills.SkillIndex;
import org.GameObject.GameObject;
import utils.Time;

public class DashSkill extends Skill {

    private static final int DASH_SPEED = 1500;
    private static final double DASH_TIME = 0.2;

    private Time.CoroutineID dashCoroutineID;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public DashSkill(GameObject owner) {
        super(owner);
    }

    @Override
    public void invoke() {
        Player.getInstance().setCurrentSpeed(DASH_SPEED);
        Player.getInstance().getPlayerPaddle().getPaddleDashParticle().startEmit();
        Player.getInstance().getPlayerPaddle().setCanBeDamaged(false);
        Player.getInstance().getPlayerPaddle().getPaddleStat().setDamageTakenMultiplier(0);
        dashCoroutineID = Time.addCoroutine(this::resetDashSpeed, Time.getTime() + DASH_TIME);
    }

    @Override
    protected SkillIndex getSkillIndex() {
        return SkillIndex.Dash;
    }

    private void resetDashSpeed() {
        Player.getInstance().setCurrentSpeed(Player.getInstance().getBaseSpeed());
        Player.getInstance().getPlayerPaddle().setCanBeDamaged(true);
        Player.getInstance().getPlayerPaddle().getPaddleStat().setDamageTakenMultiplier(1);
        Player.getInstance().getPlayerPaddle().getPaddleDashParticle().stopEmit();
        Time.removeCoroutine(dashCoroutineID);
    }
}
