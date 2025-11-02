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
        return (int) (getAttack() * getAttackMultiplier());
    }

    public final int getActualDefense() {
        return (int) (getDefence() * getDefenceMultiplier());
    }

    public abstract int getAttack();

    public abstract int getDefence();

    public abstract double getAttackMultiplier();

    public abstract double getDefenceMultiplier();

    public abstract double getDamageTakenMultiplier();

    public abstract double getRegenerationMultiplier();

    public abstract double getCriticalChange();

    public abstract double getCriticalDamage();

}
