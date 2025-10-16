package game.Perks.Object;

import game.Perks.Index.Perk;
import javafx.scene.input.MouseEvent;
import org.Animation.AnimationClipData;
import org.GameObject.GameObject;

public class SpeedPerk extends Perk {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public SpeedPerk(GameObject owner) {
        super(owner);
    }

    @Override
    protected void setUpVisual() {
        textUI.setText("Increase speed!");

        perkKey = AnimationClipData.Speed_Perk;
    }

    @Override
    protected void perk_onPointerClicked(Object sender, MouseEvent e) {
        System.out.println("Increase speed!");
    }
}
