package game.Voltraxis;

import org.Rendering.ImageAsset;

public final class VoltraxisData {

    /// Stat
    static final int BASE_ATTACK = 18;
    static final int BASE_MAX_HEALTH = 3125;
    static final int BASE_DEFENSE = 36;
    static final int DEFENSE_STRENGTH_SCALE = 67;

    /// Basic skill
    static final double BASIC_SKILL_COOLDOWN = 7.0;
    static final double ELECTRIC_BALL_ATTACK_PROPORTION = 0.632;

    /// Enhance skill
    static final double ENHANCE_SKILL_COOLDOWN = 30;
    static final double ENHANCE_SKILL_DURATION = 20;
    static final double ENHANCE_ATTACK_INCREMENT = 0.1;

    /// Groggy skill
    static final double GROGGY_BASIC_COOLDOWN_REDUCTION = 0.35;
    static final double GROGGY_TO_EX_CHARGE_TIME = 15.0;
    static final double GROGGY_ATTACK_INCREMENT = 0.2;
    static final double GROGGY_DELTA = 0.03;
    static final double GROGGY_DURATION = 18.0;

    /// Power core
    static final double POWER_CORE_PROPORTIONAL_HEALTH = 0.36;
    static final double POWER_CORE_DAMAGE_TAKEN_REDUCTION = 0.1;
    static final double MIN_GROGGY_ON_POWER_CORE_DEPLOY = 0.5;

    /// Effects
    enum EffectIndex {

        AttackIncrement(ImageAsset.ImageIndex.Voltraxis_UI_EffectIcon_AttackIncrement),
        DamageTakenIncrement(ImageAsset.ImageIndex.Voltraxis_UI_EffectIcon_DamageTakenIncrement),
        DefenceReduction(ImageAsset.ImageIndex.Voltraxis_UI_EffectIcon_DefenseIncrement),
        DamageTakenDecrement(ImageAsset.ImageIndex.Voltraxis_UI_EffectIcon_DamageTakenDecrement),
        SkillCooldownDecrement(ImageAsset.ImageIndex.Voltraxis_UI_EffectIcon_SkillCooldownDecrement),
        PowerCore(ImageAsset.ImageIndex.Voltraxis_UI_EffectIcon_PowerCore),
        ChargingEX(ImageAsset.ImageIndex.Voltraxis_UI_EffectIcon_Charging);

        private final ImageAsset.ImageIndex imageIndex;

        EffectIndex(ImageAsset.ImageIndex imageIndex) {
            this.imageIndex = imageIndex;
        }

        public ImageAsset.ImageIndex getImageIndex() {
            return imageIndex;
        }

    }

}