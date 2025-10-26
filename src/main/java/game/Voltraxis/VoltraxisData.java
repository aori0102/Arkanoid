package game.Voltraxis;

import org.Rendering.ImageAsset;
import utils.Vector2;

/**
 * Data for Voltraxis.
 */
public final class VoltraxisData {

    /// Stat
    public static final int BASE_ATTACK = 18;
    public static final int BASE_MAX_HEALTH = 3125;
    public static final int BASE_DEFENSE = 36;
    public static final int DEFENSE_STRENGTH_SCALE = 67;

    /// Basic skill
    public static final double BASIC_SKILL_COOLDOWN = 7.0;
    public static final double ELECTRIC_BALL_ATTACK_PROPORTION = 0.632;

    /// Enhance skill
    public static final double ENHANCE_SKILL_COOLDOWN = 30;
    public static final double ENHANCE_SKILL_DURATION = 20;
    public static final double ENHANCE_ATTACK_INCREMENT = 0.1;

    /// Groggy skill
    public static final double GROGGY_BASIC_COOLDOWN_REDUCTION = 0.35;
    public static final double GROGGY_ATTACK_INCREMENT = 0.2;
    public static final double GROGGY_DELTA = 0.08;
    public static final double GROGGY_DURATION = 18.0;

    /// EX skill
    public static final double EX_LOW_CHARGE_TIME = 7.0;
    public static final double EX_MEDIUM_CHARGE_TIME = 16.0;
    public static final double EX_HIGH_CHARGE_TIME = 20.0;

    /// Power core
    public static final double POWER_CORE_PROPORTIONAL_HEALTH = 0.36;
    public static final double POWER_CORE_DAMAGE_TAKEN_REDUCTION = 0.1;
    public static final double MIN_GROGGY_ON_POWER_CORE_DEPLOY = 0.5;
    public static final Vector2 LEFT_CORE_POSITION = new Vector2(200.0, 400.0);
    public static final Vector2 RIGHT_CORE_POSITION = new Vector2(800.0, 400.0);

    /// Charging
    public static final double CHARGING_RATE = 9.1;
    public static final double CHARGING_HALT_DELAY = 1.2;
    public static final double CHARGING_HALT_AMOUNT = 42;
    public static final double CHARGING_MAX = ChargingState.High.chargePoint;

    public enum ChargingState {
        None(0),
        Low(30),
        Medium(75),
        High(108);

        public final double chargePoint;

        ChargingState(double chargePoint) {
            this.chargePoint = chargePoint;
        }
    }

    /// Additional effects
    // TODO: can change this value - Aori to Kine
    public static final double FROST_BITE_DAMAGE_TAKEN_INCREMENT = 0.35;

    /// Effects
    public enum EffectIndex {

        // TODO: update image index - Aori to Kine
        AttackIncrement(ImageAsset.ImageIndex.Voltraxis_UI_EffectIcon_AttackIncrement),
        DamageTakenIncrement(ImageAsset.ImageIndex.Voltraxis_UI_EffectIcon_DamageTakenIncrement),
        DefenceReduction(ImageAsset.ImageIndex.Voltraxis_UI_EffectIcon_DefenseIncrement),
        DamageTakenDecrement(ImageAsset.ImageIndex.Voltraxis_UI_EffectIcon_DamageTakenDecrement),
        SkillCooldownDecrement(ImageAsset.ImageIndex.Voltraxis_UI_EffectIcon_SkillCooldownDecrement),
        PowerCore(ImageAsset.ImageIndex.Voltraxis_UI_EffectIcon_PowerCore),
        Burning(ImageAsset.ImageIndex.None),
        Frostbite(ImageAsset.ImageIndex.None),
        ChargingEX(ImageAsset.ImageIndex.Voltraxis_UI_EffectIcon_Charging);

        private final ImageAsset.ImageIndex imageIndex;

        EffectIndex(ImageAsset.ImageIndex imageIndex) {
            this.imageIndex = imageIndex;
        }

        /**
         * Get the {@link org.Rendering.ImageAsset.ImageIndex} of
         * the sprite of this effect.
         *
         * @return The image index of this effect.
         */
        public ImageAsset.ImageIndex getImageIndex() {
            return imageIndex;
        }

    }

}