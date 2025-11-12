package game.Obstacle.Laser;

import game.Entity.EntityStat;
import org.GameObject.GameObject;

public class LaserStat extends EntityStat {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public LaserStat(GameObject owner) {
        super(owner);
    }

    @Override
    public int getBaseAttack() {
        return 20;
    }

    @Override
    public double getMaxHealthMultiplier() {
        return 1;
    }

    @Override
    public int getBaseDefense() {
        return 0;
    }

    @Override
    public double getAttackMultiplier() {
        return 0;
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
        return 0.3;
    }

    @Override
    public double getCriticalDamage() {
        return 2;
    }

    @Override
    public int getBaseMaxHealth() {
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
