package game.Perks.Object;

import game.Perks.Index.Perk;
import game.Player.Paddle.PaddleStat;
import game.Player.Player;
import javafx.scene.input.MouseEvent;
import org.Animation.AnimationClipData;
import org.GameObject.GameObject;

public final class AttackPerk extends Perk {

    private static final double ATTACK_MULTIPLIER = 0.1;

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
    protected void setUpVisual() {
        textUI.setText("Increase attack!");
        perkKey = AnimationClipData.Attack_Perk;
    }

    @Override
    protected void perk_onPointerClicked(Object sender, MouseEvent e) {
        super.perk_onPointerClicked(sender, e);
        PaddleStat paddleStat = Player.getInstance().getPlayerPaddle().getPaddleStat();

        paddleStat.setAttackMultiplier(paddleStat.getAttackMultiplier() + ATTACK_MULTIPLIER);
    }

}
