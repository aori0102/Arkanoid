package game.Damagable;

import game.Effect.StatusEffect;

public interface ICanDealDamage {
    /**
     * Return the amount of damage by this object.
     *
     * @return This object's damage.
     */
    DamageInfo getDamageInfo();

    StatusEffect getEffect();

    /**
     * Handle whatever happens to the object after dealing damage.
     */
    void onDamaged();

    /**
     * Whether the damage acceptor provided is this object's target for damage.
     *
     * @return The {@link DamageAcceptor} to check.
     */
    boolean isDamageTarget(DamageAcceptor damageAcceptor);

}