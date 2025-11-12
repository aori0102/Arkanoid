package game.Entity;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public abstract class EntityStat extends MonoBehaviour {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public EntityStat(GameObject owner) {
        super(owner);
    }

    public final int getActualAttack() {
        return (int) (getBaseAttack() * getAttackMultiplier());
    }

    public final int getActualDefense() {
        return (int) (getBaseDefense() * getDefenceMultiplier());
    }

    public final int getActualMaxHealth() {
        return (int) (getBaseMaxHealth() * getMaxHealthMultiplier());
    }

    public final double getActualMovementSpeed() {
        return getBaseMovementSpeed() * getMovementSpeedMultiplier();
    }

    public abstract int getBaseAttack();

    public abstract int getBaseDefense();

    public abstract double getAttackMultiplier();

    public abstract double getDefenceMultiplier();

    public abstract double getDamageTakenMultiplier();

    public abstract double getRegenerationMultiplier();

    public abstract double getCriticalChance();

    public abstract double getCriticalDamage();

    public abstract int getBaseMaxHealth();

    public abstract double getMaxHealthMultiplier();

    public abstract double getBaseMovementSpeed();

    public abstract double getMovementSpeedMultiplier();

}
