package game.Player.PlayerSkills.Skills;

import game.GameObject.Shield.Shield;
import game.Player.Paddle.PlayerStat;
import game.Player.Player;
import game.Player.PlayerSkills.SkillIndex;
import org.GameObject.GameObject;
import org.Layer.Layer;
import utils.Time;
import utils.Vector2;

public class InvincibleSkill extends Skill {

    private static final double SPEED_BOOSTED_MULTIPLIER_INCREMENT = 0.42;
    private static final double INVINCIBLE_TIME = 5.0;
    private static final double ENLARGE_SCALE = 1.75;
    private Time.CoroutineID invincibleCoroutineID;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public InvincibleSkill(GameObject owner) {
        super(owner);
    }

    @Override
    public void invoke() {
        var paddle = Player.getInstance().getPlayerPaddle();
        paddle.getTransform().setLocalScale(
                paddle.getTransform().getLocalScale().multiply(ENLARGE_SCALE)
        );
        paddle.getGameObject().setLayer(Layer.Paddle_IgnoreDamage);
        paddle.getPaddleStat().setStatMultiplier(
                PlayerStat.PlayerStatIndex.MovementSpeed,
                paddle.getPaddleStat().getMovementSpeedMultiplier() + SPEED_BOOSTED_MULTIPLIER_INCREMENT
        );
        Shield.getInstance().turnOn();
        invincibleCoroutineID = Time.addCoroutine(this::turnOff, INVINCIBLE_TIME);
    }

    @Override
    protected SkillIndex getSkillIndex() {
        return SkillIndex.Invincible;
    }

    private void turnOff() {
        var paddle = Player.getInstance().getPlayerPaddle();
        paddle.getTransform().setLocalScale(
                paddle.getTransform().getLocalScale().divide(ENLARGE_SCALE)
        );
        paddle.getGameObject().setLayer(Layer.Paddle);
        paddle.getPaddleStat().setStatMultiplier(
                PlayerStat.PlayerStatIndex.MovementSpeed,
                paddle.getPaddleStat().getMovementSpeedMultiplier() - SPEED_BOOSTED_MULTIPLIER_INCREMENT
        );
        Shield.getInstance().turnOff();
        Time.removeCoroutine(invincibleCoroutineID);
    }
}
