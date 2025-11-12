package game.Player.PlayerSkills.Skills;

import game.Player.Paddle.PlayerStat;
import game.Player.Player;
import game.Player.PlayerSkills.SkillIndex;
import org.GameObject.GameObject;
import org.Layer.Layer;
import utils.Time;

/**
 * DashSkill is a player skill that allows the player to dash forward quickly
 * for a short duration. While dashing, the player paddle is invulnerable to damage.
 */
public class DashSkill extends Skill {

    private static final double DASH_SPEED_MULTIPLIER_INCREMENT = 1.3;
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

    /**
     * Activates the dash skill:
     * <ul>
     *     <li>Increase player's movement speed by a certain amount</li>
     *     <li>Starts dash particle effect</li>
     *     <li>Makes paddle invulnerable during dash</li>
     *     <li>Starts a coroutine to reset speed and invulnerability after certain time</li>
     * </ul>
     */
    @Override
    public void invoke() {
        var paddle = Player.getInstance().getPlayerPaddle();
        paddle.getPaddleStat().setStatMultiplier(
                PlayerStat.PlayerStatIndex.MovementSpeed,
                paddle.getPaddleStat().getMovementSpeedMultiplier() + DASH_SPEED_MULTIPLIER_INCREMENT
        );
        paddle.getPaddleDashParticle().startEmit();
        paddle.getGameObject().setLayer(Layer.Paddle_IgnoreDamage);
        dashCoroutineID = Time.addCoroutine(this::resetDashSpeed, DASH_TIME);
    }

    @Override
    protected void onDestroy() {
        Time.removeCoroutine(dashCoroutineID);
    }

    @Override
    protected SkillIndex getSkillIndex() {
        return SkillIndex.Dash;
    }

    /**
     * Resets the player's speed and invulnerability after dash duration ends.
     * Stops the dash particle effect and removes the coroutine.
     */
    private void resetDashSpeed() {
        var paddle = Player.getInstance().getPlayerPaddle();
        paddle.getPaddleStat().setStatMultiplier(
                PlayerStat.PlayerStatIndex.MovementSpeed,
                paddle.getPaddleStat().getMovementSpeedMultiplier() - DASH_SPEED_MULTIPLIER_INCREMENT
        );
        paddle.getGameObject().setLayer(Layer.Paddle);
        paddle.getPaddleDashParticle().stopEmit();
        Time.removeCoroutine(dashCoroutineID);
    }
}
