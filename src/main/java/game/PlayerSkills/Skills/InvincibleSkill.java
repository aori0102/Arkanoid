package game.PlayerSkills.Skills;

import game.GameObject.Shield.Shield;
import game.Player.Player;
import game.PlayerSkills.SkillIndex;
import org.GameObject.GameObject;
import utils.Time;
import utils.Vector2;

/**
 * InvincibleSkill grants temporary invincibility to the player paddle.
 * During this skill:
 * - Paddle cannot be damaged
 * - Paddle grows in size
 * - Player speed is boosted
 * - Paddle's damage taken is reduced to zero
 * - Shield visual is displayed
 */
public class InvincibleSkill extends Skill {

    /** Multiplier for player speed during invincibility */
    private static final int SPEED_BOOSTED_MULTIPLIER = 2;
    /** Duration of invincibility in seconds */
    private static final double INVINCIBLE_TIME = 5.0;

    /** Coroutine ID used to turn off invincibility after duration */
    private Time.CoroutineID invincibleCoroutineID;

    /**
     * Create this InvincibleSkill.
     *
     * @param owner The owner GameObject of this skill (typically the player).
     */
    public InvincibleSkill(GameObject owner) {
        super(owner);
    }

    /**
     * Activates the invincibility skill:
     * - Makes paddle invulnerable
     * - Increases paddle scale for visual effect
     * - Boosts player speed
     * - Sets damage taken multiplier to 0
     * - Turns on shield visual
     * - Starts coroutine to end invincibility after INVINCIBLE_TIME
     */
    @Override
    public void invoke() {
        Player.getInstance().getPlayerPaddle().setCanBeDamaged(false);
        Player.getInstance().getPlayerPaddle().getTransform().setGlobalScale(new Vector2(2.5, 1.25));
        Player.getInstance().setCurrentSpeed(Player.getInstance().getCurrentSpeed() * SPEED_BOOSTED_MULTIPLIER);
        Player.getInstance().getPlayerPaddle().getPaddleStat().setDamageTakenMultiplier(0);
        Shield.getInstance().turnOn();
        invincibleCoroutineID = Time.addCoroutine(this::turnOff, Time.getTime() + INVINCIBLE_TIME);
    }

    /**
     * Returns the skill index representing this skill.
     *
     * @return SkillIndex.Invincible
     */
    @Override
    protected SkillIndex getSkillIndex() {
        return SkillIndex.Invincible;
    }

    /**
     * Ends the invincibility effect:
     * - Restores paddle's ability to be damaged
     * - Resets paddle scale
     * - Resets player speed
     * - Resets damage taken multiplier
     * - Turns off shield visual
     * - Removes the coroutine
     */
    private void turnOff() {
        Player.getInstance().getPlayerPaddle().setCanBeDamaged(true);
        Player.getInstance().getPlayerPaddle().getTransform().setGlobalScale(new Vector2(1.25, 1.25));
        Player.getInstance().setCurrentSpeed(Player.getInstance().getBaseSpeed());
        Player.getInstance().getPlayerPaddle().getPaddleStat().setDamageTakenMultiplier(1);
        Shield.getInstance().turnOff();
        Time.removeCoroutine(invincibleCoroutineID);
    }
}
