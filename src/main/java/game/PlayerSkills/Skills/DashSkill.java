package game.PlayerSkills.Skills;

import game.Player.Player;
import game.PlayerSkills.SkillIndex;
import org.GameObject.GameObject;
import utils.Time;

/**
 * DashSkill is a player skill that allows the player to dash forward quickly
 * for a short duration. While dashing, the player paddle is invulnerable to damage.
 */
public class DashSkill extends Skill {

    /** Dash speed in units per second */
    private static final int DASH_SPEED = 1500;
    /** Duration of the dash in seconds */
    private static final double DASH_TIME = 0.2;

    /** Coroutine ID used to reset dash after DASH_TIME */
    private Time.CoroutineID dashCoroutineID;

    /**
     * Create this DashSkill.
     *
     * @param owner The owner GameObject of this skill (typically the player).
     */
    public DashSkill(GameObject owner) {
        super(owner);
    }

    /**
     * Activates the dash skill:
     * - Sets player speed to DASH_SPEED
     * - Starts dash particle effect
     * - Makes paddle invulnerable during dash
     * - Starts a coroutine to reset speed and invulnerability after DASH_TIME
     */
    @Override
    public void invoke() {
        Player.getInstance().setCurrentSpeed(DASH_SPEED);
        Player.getInstance().getPlayerPaddle().getPaddleDashParticle().startEmit();
        Player.getInstance().getPlayerPaddle().setCanBeDamaged(false);
        Player.getInstance().getPlayerPaddle().getPaddleStat().setDamageTakenMultiplier(0);
        dashCoroutineID = Time.addCoroutine(this::resetDashSpeed, Time.getTime() + DASH_TIME);
    }

    /**
     * Returns the skill index representing this skill.
     *
     * @return SkillIndex.Dash
     */
    @Override
    protected SkillIndex getSkillIndex() {
        return SkillIndex.Dash;
    }

    /**
     * Resets the player's speed and invulnerability after dash duration ends.
     * Stops the dash particle effect and removes the coroutine.
     */
    private void resetDashSpeed() {
        Player.getInstance().setCurrentSpeed(Player.getInstance().getBaseSpeed());
        Player.getInstance().getPlayerPaddle().setCanBeDamaged(true);
        Player.getInstance().getPlayerPaddle().getPaddleStat().setDamageTakenMultiplier(1);
        Player.getInstance().getPlayerPaddle().getPaddleDashParticle().stopEmit();
        Time.removeCoroutine(dashCoroutineID);
    }
}
