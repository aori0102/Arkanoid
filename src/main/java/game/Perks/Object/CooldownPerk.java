package game.Perks.Object;

import game.Perks.Index.Perk;
import game.Player.Paddle.PaddleStat;
import game.Player.PlayerSkills.SkillIndex;
import org.Animation.AnimationClipData;
import org.GameObject.GameObject;
import utils.Random;

public final class CooldownPerk extends Perk {

    private record Range(double min, double max) {
    }

    private static final SkillIndex[] APPLICABLE_SKILL_INDICES = {
            SkillIndex.LaserBeam, SkillIndex.Updraft, SkillIndex.Invincible
    };
    private final SkillIndex skillIndex
            = APPLICABLE_SKILL_INDICES[Random.range(0, APPLICABLE_SKILL_INDICES.length)];

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public CooldownPerk(GameObject owner) {
        super(owner);
    }

    @Override
    protected String getPerkDescription(double amount) {
        return "Reduces " + getSkillName() + " cooldown by " + String.format("%.1f", amount * 100.0) + "%";
    }

    @Override
    protected double getMinModifierValue() {
        return switch (skillIndex) {
            case LaserBeam -> 0.03;
            case Updraft -> 0.06;
            case Invincible -> 0.05;
            default -> 0;
        };
    }

    @Override
    protected double getMaxModifierValue() {
        return switch (skillIndex) {
            case LaserBeam -> 0.17;
            case Updraft -> 0.19;
            case Invincible -> 0.12;
            default -> 0;
        };
    }

    @Override
    protected AnimationClipData getPerkAnimationKey() {
        return AnimationClipData.Cooldown_Perk;
    }

    @Override
    protected void applyPerk(PaddleStat paddleStat) {
        paddleStat.setSkillCooldownMultiplier(
                skillIndex,
                paddleStat.getSkillCooldownMultiplier(skillIndex) - getModifierValue()
        );
    }

    private String getSkillName() {
        return switch (skillIndex) {
            case LaserBeam -> "Laser Beam";
            case Updraft -> "Updraft";
            case Invincible -> "Invincible";
            default -> "";
        };
    }

}