package game.Perks.Object;

import game.Perks.Index.Perk;
import game.Player.Paddle.PaddleStat;
import game.Player.Player;
import javafx.scene.input.MouseEvent;
import org.Animation.AnimationClipData;
import org.GameObject.GameObject;
import utils.Random;

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
    protected void setUpVisual() {
        textUI.setText("Increase speed!");

        perkKey = AnimationClipData.Speed_Perk;
    }

    @Override
    protected void perk_onPointerClicked(Object sender, MouseEvent e) {
        super.perk_onPointerClicked(sender, e);
        System.out.println("Increase speed!");
        PaddleStat paddleStat = Player.getInstance().getPlayerPaddle().getPaddleStat();
        paddleStat.setMovementSpeed(paddleStat.getMovementSpeed() + 50);
    }
}
