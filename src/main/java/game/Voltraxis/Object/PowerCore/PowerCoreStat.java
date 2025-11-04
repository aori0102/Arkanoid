package game.Voltraxis.Object.PowerCore;

import game.Entity.EntityStat;
import org.GameObject.GameObject;

public final class PowerCoreStat extends EntityStat {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerCoreStat(GameObject owner) {
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
        return 0;
    }

    @Override
    public double getDefenceMultiplier() {
        return 0;
    }

    @Override
    public double getDamageTakenMultiplier() {
        return 1;
    }

    @Override
    public double getRegenerationMultiplier() {
        return 0;
    }

    @Override
    public double getCriticalChange() {
        return 0;
    }

    @Override
    public double getCriticalDamage() {
        return 0;
    }

}