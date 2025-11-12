package game.Effect;

import game.Entity.EntityHealthAlterType;

public enum StatusEffect {
    Burn(12, 1.3, EntityHealthAlterType.BurnDamage),
    FrostBite(5, 2.2, EntityHealthAlterType.FrostDamage),
    Stunned(0, 9999999.0, EntityHealthAlterType.NormalDamage),
    Electrified(9, 0.5, EntityHealthAlterType.ElectrifiedDamage);

    public final int baseDamageOverTime;
    public final double damageDelay;
    public final EntityHealthAlterType damageType;

    /**
     * Constructs a {@code StatusEffect} with the specified parameters.
     *
     * @param baseDamageOverTime the base amount of damage this status effect inflicts
     *                           over time (per tick or interval)
     * @param damageDelay the delay between each damage application, in seconds
     * @param damageType the type of damage associated with this effect, defined in
     *                   {@link EntityHealthAlterType}
     *
     * This constructor initializes a specific status effect type with its corresponding
     * base damage value, delay between damage applications, and the categorized damage type.
     */
    StatusEffect(int baseDamageOverTime, double damageDelay, EntityHealthAlterType damageType) {
        this.baseDamageOverTime = baseDamageOverTime;
        this.damageDelay = damageDelay;
        this.damageType = damageType;
    }

}
