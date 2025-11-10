package game.Voltraxis.Object.PowerCore;

import game.Entity.EntityStat;
import game.Voltraxis.VoltraxisData;
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
    public double getCriticalChance() {
        return 0;
    }

    @Override
    public double getCriticalDamage() {
        return 0;
    }

    @Override
    public int getMaxHealth() {
        return (int) (VoltraxisData.BASE_MAX_HEALTH * VoltraxisData.POWER_CORE_PROPORTIONAL_HEALTH);
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