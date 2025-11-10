package game.Player.PlayerSkills.Skills;

import game.Player.Player;
import org.GameObject.GameObject;
import utils.Time;

public class Dash extends Skill {

    private static final int DASH_SPEED = 1500;
    private static final double DASH_TIME = 0.2;

    private Time.CoroutineID dashCoroutineID;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Dash(GameObject owner) {
        super(owner);
    }

    @Override
    public void invoke() {
        Player.getInstance().setCurrentSpeed(DASH_SPEED);
        dashCoroutineID = Time.addCoroutine(this::resetDashSpeed, Time.getTime() + DASH_TIME);
    }

    private void resetDashSpeed() {
        Player.getInstance().setCurrentSpeed(Player.getInstance().getBaseSpeed());
        Time.removeCoroutine(dashCoroutineID);
    }
}
