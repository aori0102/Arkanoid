package game.LaserBeam;

import game.Entity.EntityStat;
import game.Player.Paddle.PlayerStat;
import game.Player.Player;
import org.GameObject.GameObject;

public final class LaserBeamStat extends EntityStat {

    private static final double LASER_DAMAGE_MULTIPLIER = 3.87;

    private PlayerStat playerStat = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public LaserBeamStat(GameObject owner) {
        super(owner);
    }

    @Override
    public void start() {
        playerStat = Player.getInstance().getPlayerPaddle().getPaddleStat();
    }

    @Override
    public int getBaseAttack() {
        return playerStat.getBaseAttack();
    }

    @Override
    public int getBaseDefense() {
        return 0;
    }

    @Override
    public double getAttackMultiplier() {
        return playerStat.getAttackMultiplier() * LASER_DAMAGE_MULTIPLIER;
    }

    @Override
    public double getDefenceMultiplier() {
        return 0;
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
    public double getCriticalChance() {
        return playerStat.getCriticalChance();
    }

    @Override
    public double getCriticalDamage() {
        return playerStat.getCriticalDamage();
    }

    @Override
    public int getBaseMaxHealth() {
        return 0;
    }

    @Override
    public double getMaxHealthMultiplier() {
        return 0;
    }

    @Override
    public double getBaseMovementSpeed() {
        return 0;
    }

    @Override
    public double getMovementSpeedMultiplier() {
        return 0;
    }

}