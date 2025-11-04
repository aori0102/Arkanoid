package game.Voltraxis;

import game.Voltraxis.Object.ElectricBall.ElectricBall;
import game.Voltraxis.Prefab.ElectricBallPrefab;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.MathUtils;
import utils.Random;
import utils.Time;
import utils.Vector2;

public class VoltraxisNormalAttackBrain extends MonoBehaviour {

    private static final double MAX_BALL_ANGLE = 40.0;
    private static final int BALL_PER_SHOT = 2;
    private static final int MAX_SHOT_SEQUENCE = 3;
    private static final double SEQUENCE_DELAY = 0.35;
    private static final double SHOOTING_DELAY = 0.4;

    private Time.CoroutineID normalAttackCoroutineID = null;

    public EventHandler<Void> onBasicAttackCommenced = new EventHandler<>(VoltraxisNormalAttackBrain.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisNormalAttackBrain(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        normalAttackCoroutineID = Time.addCoroutine(
                this::basicSkill,
                Time.getTime() + Voltraxis.getInstance().getVoltraxisStatManager().getBasicSkillCooldown()
        );

        Voltraxis.getInstance().getVoltraxisCharging().onChargingEntered
                .addListener(this::voltraxisCharging_onChargingEntered);
        Voltraxis.getInstance().getVoltraxisCharging().onChargingTerminated
                .addListener(this::voltraxisCharging_onChargingTerminated);
    }

    /**
     * Called when {@link VoltraxisCharging#onChargingTerminated} is invoked.<br><br>
     * This function re-enable normal attack function of Voltraxis after its EX.
     */
    private void voltraxisCharging_onChargingTerminated(Object sender, Void e) {
        normalAttackCoroutineID = Time.addCoroutine(
                this::basicSkill,
                Time.getTime() + Voltraxis.getInstance().getVoltraxisStatManager().getBasicSkillCooldown()
        );
    }

    /**
     * Called when {@link VoltraxisCharging#onChargingEntered} is invoked.<br><br>
     * This function disable normal attack function of Voltraxis when entering its charging phase.
     */
    private void voltraxisCharging_onChargingEntered(Object sender, Void e) {
        if (normalAttackCoroutineID != null) {
            Time.removeCoroutine(normalAttackCoroutineID);
            normalAttackCoroutineID = null;
        }
    }

    /**
     * <b>Voltraxis' basic skill: Arc Discharge</b><br><br>
     * Fires up to <b>5</b> electric orbs at varying angles
     * toward the player, each dealing <b>63.2%</b> ATK. If hit,
     * the player is stunned for <b>3s</b>.
     */
    private void basicSkill() {

        for (int i = 0; i < MAX_SHOT_SEQUENCE; i++) {
            var delay = SEQUENCE_DELAY * i + SHOOTING_DELAY;
            Time.addCoroutine(this::shootBalls, Time.getTime() + delay);
        }
        onBasicAttackCommenced.invoke(this, null);
        normalAttackCoroutineID = Time.addCoroutine(
                this::basicSkill,
                Time.getTime() + Voltraxis.getInstance().getVoltraxisStatManager().getBasicSkillCooldown()
        );

    }

    /**
     * Shoot 2 {@link ElectricBall} towards random position
     */
    private void shootBalls() {

        for (int i = 0; i < BALL_PER_SHOT; i++) {

            // Construct direction
            var angle = MathUtils.deg2rad(Random.range(-MAX_BALL_ANGLE, MAX_BALL_ANGLE));
            var direction = Vector2.down().rotateBy(angle);

            // Instantiate electric ball
            var electricBall = new ElectricBallPrefab().instantiatePrefab()
                    .getComponent(ElectricBall.class);
            electricBall.getTransform().setGlobalPosition(getTransform().getGlobalPosition());
            electricBall.setDirection(direction);

        }

    }

}