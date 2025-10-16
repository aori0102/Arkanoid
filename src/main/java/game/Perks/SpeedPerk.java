package game.Perks;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.AnimationClipData;
import org.GameObject;
import org.TextHorizontalAlignment;
import org.TextVerticalAlignment;
import utils.Vector2;

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
