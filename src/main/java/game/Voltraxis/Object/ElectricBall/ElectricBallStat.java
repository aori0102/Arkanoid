package game.Voltraxis.Object.ElectricBall;

import game.Entity.EntityStat;
import game.Voltraxis.Voltraxis;
import game.Voltraxis.VoltraxisData;
import org.GameObject.GameObject;

public final class ElectricBallStat extends EntityStat {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ElectricBallStat(GameObject owner) {
        super(owner);
    }

    @Override
    public int getAttack() {
        return Voltraxis.getInstance().getVoltraxisStatManager().getActualAttack();
    }

    @Override
    public int getDefence() {
        return 0;
    }

    @Override
    public double getAttackMultiplier() {
        return VoltraxisData.ELECTRIC_BALL_ATTACK_PROPORTION;
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
        return VoltraxisData.CRITICAL_CHANCE;
    }

    @Override
    public double getCriticalDamage() {
        return VoltraxisData.CRITICAL_DAMAGE;
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