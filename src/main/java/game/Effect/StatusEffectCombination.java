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

    /**
     * Constructs a {@code StatusEffectCombination} that defines the interaction
     * between two {@link StatusEffect} types and the resulting damage multiplier.
     *
     * @param hitEffect the {@link StatusEffect} applied by the attacking source (the one being inflicted)
     * @param bearEffect the {@link StatusEffect} already present on the target (the one being affected)
     * @param damageMultiplier the multiplier applied to the resulting damage when both effects react
     * This constructor initializes a specific elemental or status reaction that occurs when
     * one effect interacts with another. For example, applying {@code Burn} to a target already
     * affected by {@code FrostBite} can trigger a reaction (e.g., "Melt") with a defined
     * {@code damageMultiplier} to amplify or modify the damage outcome.
     */
    StatusEffectCombination(StatusEffect hitEffect, StatusEffect bearEffect, double damageMultiplier) {
        this.hitEffect = hitEffect;
        this.bearEffect = bearEffect;
        this.damageMultiplier = damageMultiplier;
    }


}