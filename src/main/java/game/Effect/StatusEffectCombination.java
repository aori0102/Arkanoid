package game.Effect;

public enum StatusEffectCombination {

    BurnMelt(StatusEffect.Burn, StatusEffect.FrostBite, 2.2),
    FrostMelt(StatusEffect.FrostBite, StatusEffect.Burn, 1.8),
    Overloaded_1(StatusEffect.Burn, StatusEffect.Electrified, 2.0),
    Overloaded_2(StatusEffect.Electrified, StatusEffect.Burn, 2.0),
    SuperConduct_1(StatusEffect.FrostBite, StatusEffect.Electrified, 1.2),
    SuperConduct_2(StatusEffect.Electrified, StatusEffect.FrostBite, 1.2);

    public final StatusEffect hitEffect;
    public final StatusEffect bearEffect;
    public final double damageMultiplier;

    StatusEffectCombination(StatusEffect hitEffect, StatusEffect bearEffect, double damageMultiplier) {
        this.hitEffect = hitEffect;
        this.bearEffect = bearEffect;
        this.damageMultiplier = damageMultiplier;
    }

}