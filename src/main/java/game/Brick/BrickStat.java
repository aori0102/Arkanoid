package game.Brick;

import game.Entity.EntityStat;
import org.GameObject.GameObject;

public final class BrickStat extends EntityStat {

    private double damageTakenMultiplier = 1.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BrickStat(GameObject owner) {
        super(owner);
    }

    @Override
    public int getAttack() {
        return 0;
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
        return damageTakenMultiplier;
    }

    @Override
    public double getRegenerationMultiplier() {
        return 1;
    }

    @Override
    public double getCriticalChange() {
        return 0;
    }

    @Override
    public double getCriticalDamage() {
        return 0;
    }

    public void setDamageTakenMultiplier(double damageTakenMultiplier) {
        this.damageTakenMultiplier = damageTakenMultiplier;
    }
}