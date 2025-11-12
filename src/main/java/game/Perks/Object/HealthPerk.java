package game.Perks.Object;

import game.Perks.Index.Perk;
import game.Player.Paddle.PaddleStat;
import org.Animation.AnimationClipData;
import org.GameObject.GameObject;

public final class HealthPerk extends Perk {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public HealthPerk(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        super.awake();
    }

    @Override
    protected String getPerkDescription(double amount) {
        return "Increase player's max HP by " + String.format("%.1f", amount * 100) + "%";
    }

    @Override
    protected double getMinModifierValue() {
        return 0.13;
    }

    @Override
    protected double getMaxModifierValue() {
        return 0.29;
    }

    @Override
    protected AnimationClipData getPerkAnimationKey() {
        return AnimationClipData.Health_Perk;
    }

    @Override
    protected void applyPerk(PaddleStat paddleStat) {
        paddleStat.setMaxHealthMultiplier(paddleStat.getMaxHealthMultiplier() + getModifierValue());
    }

}
