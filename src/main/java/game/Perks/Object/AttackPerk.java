package game.Perks.Object;

import game.Perks.Index.Perk;
import game.Player.Paddle.PlayerStat;
import org.Animation.AnimationClipData;
import org.GameObject.GameObject;

public final class AttackPerk extends Perk {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public AttackPerk(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        super.awake();
    }

    @Override
    protected String getPerkDescription(double amount) {
        return "Increase attack by " + String.format("%.1f", amount * 100) + "%";
    }

    @Override
    protected double getMinModifierValue() {
        return 0.1;
    }

    @Override
    protected double getMaxModifierValue() {
        return 0.25;
    }

    @Override
    protected AnimationClipData getPerkAnimationKey() {
        return AnimationClipData.Attack_Perk;
    }

    @Override
    protected void applyPerk(PlayerStat playerStat) {
        playerStat.setStatMultiplier(
                PlayerStat.PlayerStatIndex.Attack,
                playerStat.getAttackMultiplier() + getModifierValue()
        );
    }

}
