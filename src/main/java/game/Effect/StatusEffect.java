package game.Effect;

import game.Entity.EntityHealthAlterType;

public enum StatusEffect {
    Burn(0, 1.3, EntityHealthAlterType.BurnDamage),
    FrostBite(0, 2.2, EntityHealthAlterType.FrostDamage),
    Stunned(0, 3.0, EntityHealthAlterType.NormalDamage),
    Electrified(0, 0.5, EntityHealthAlterType.ElectrifiedDamage);

    public final int baseDamageOverTime;
    public final double damageDelay;
    public final EntityHealthAlterType damageType;

    StatusEffect(int baseDamageOverTime, double damageDelay, EntityHealthAlterType damageType) {
        this.baseDamageOverTime = baseDamageOverTime;
        this.damageDelay = damageDelay;
        this.damageType = damageType;
    }
}
