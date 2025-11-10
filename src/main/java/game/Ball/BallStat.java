package game.Ball;

import game.Entity.EntityStat;
import game.Player.Player;
import org.GameObject.GameObject;

public final class BallStat extends EntityStat {

    private static final double BALL_CRITICAL_CHANCE = 0.27;
    private static final double BALL_CRITICAL_AMOUNT = 0.59;
    private static final int BALL_DAMAGE = 160;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BallStat(GameObject owner) {
        super(owner);
    }

    @Override
    public int getAttack() {
        return BALL_DAMAGE;
    }

    @Override
    public int getDefence() {
        return 0;
    }

    @Override
    public double getAttackMultiplier() {
        return 1;
    }

    @Override
    public double getDefenceMultiplier() {
        return 1;
    }

    @Override
    public double getDamageTakenMultiplier() {
        return 0;
    }

    @Override
    public double getRegenerationMultiplier() {
        return 0;
    }

    @Override
    public double getCriticalChange() {
        return BALL_CRITICAL_CHANCE;
    }

    @Override
    public double getCriticalDamage() {
        return BALL_CRITICAL_AMOUNT;
    }

    @Override
    public int getMaxHealth() {
        return 0;
    }

    @Override
    public double getMovementSpeed() {
        return 0;
    }

    @Override
    public double getMovementSpeedMultiplier() {
        return 0;
    }

}