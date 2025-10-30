package game.Damagable;

import utils.Vector2;

/**
 * Contains information about the damage taken, including the amount of damage ,the
 * {@link DamageType} of damage and the position where damaging occurs.
 */
public final class OnDamageTakenEventArgs {
    public DamageInfo damageInfo;
    public Vector2 position;
}