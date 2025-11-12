package game.Perks.Object;

import game.Perks.Index.Perk;
import game.Player.Paddle.PaddleStat;
import org.Animation.AnimationClipData;
import org.GameObject.GameObject;

public final class SpeedPerk extends Perk {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public SpeedPerk(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        super.awake();
    }

    @Override
    protected String getPerkDescription(double amount) {
        return "Increase paddle speed by " + String.format("%.1f", amount * 100) + "%";
    }

    @Override
    protected double getMinModifierValue() {
        return 0.04;
    }

    @Override
    protected double getMaxModifierValue() {
        return 0.16;
    }

    @Override
    protected AnimationClipData getPerkAnimationKey() {
        return AnimationClipData.Speed_Perk;
    }

    @Override
    protected void applyPerk(PaddleStat paddleStat) {
        paddleStat.setMovementSpeedMultiplier(paddleStat.getMovementSpeedMultiplier() + getModifierValue());
    }
}
