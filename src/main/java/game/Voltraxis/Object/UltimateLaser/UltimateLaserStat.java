package game.Voltraxis.Object.UltimateLaser;

import game.Entity.EntityStat;
import game.Voltraxis.Voltraxis;
import game.Voltraxis.VoltraxisData;
import org.GameObject.GameObject;

public final class UltimateLaserStat extends EntityStat {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public UltimateLaserStat(GameObject owner) {
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
        return 1;
    }

    @Override
    public double getDefenceMultiplier() {
        return 0;
    }

    @Override
    public double getDamageTakenMultiplier() {
        return VoltraxisData.ULTIMATE_LASER_DAMAGE_PROPORTION;
    }

    @Override
    public double getRegenerationMultiplier() {
        return 0;
    }

    @Override
    public double getCriticalChange() {
        return VoltraxisData.CRITICAL_CHANCE;
    }

    @Override
    public double getCriticalDamage() {
        return VoltraxisData.CRITICAL_DAMAGE;
    }

}