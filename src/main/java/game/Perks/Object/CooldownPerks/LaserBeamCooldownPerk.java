package game.Perks.Object.CooldownPerks;

import game.Perks.Index.Perk;
import game.Player.Paddle.PaddleStat;
import game.Player.Player;
import javafx.scene.input.MouseEvent;
import org.Animation.AnimationClipData;
import org.GameObject.GameObject;

public class LaserBeamCooldownPerk extends Perk {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public LaserBeamCooldownPerk(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        super.awake();
    }

    @Override
    protected void setUpVisual() {
        textUI.setText("Decrease laser beam cooldown!");
        perkKey = AnimationClipData.Cooldown_Perk;
    }

    @Override
    protected void perk_onPointerClicked(Object sender, MouseEvent e) {
        super.perk_onPointerClicked(sender, e);
        System.out.println("Decrease cooldown!");
        PaddleStat paddleStat = Player.getInstance().getPlayerPaddle().getPaddleStat();
        if (paddleStat.getLaserBeamCooldown() > 0.5) {
            paddleStat.setLaserBeamCooldown(paddleStat.getLaserBeamCooldown() - 0.5);
        }
    }


}
