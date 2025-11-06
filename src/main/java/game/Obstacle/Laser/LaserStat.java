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
    public int getAttack() {
        return 20;
    }

    @Override
    public int getDefence() {
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
    public double getCriticalChange() {
        return 0.3;
    }

    @Override
    public double getCriticalDamage() {
        return 2;
    }
}
