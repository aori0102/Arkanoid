package game.Perks.Object;

import game.Perks.Index.Perk;
import javafx.scene.input.MouseEvent;
import org.Animation.AnimationClipData;
import org.GameObject.GameObject;
import utils.Random;

public class HealthPerk extends Perk {

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
    protected void setUpVisual() {
        textUI.setText("Increase health!");
        perkKey = AnimationClipData.Health_Perk;

    }

    @Override
    protected void perk_onPointerClicked(Object sender, MouseEvent e) {
        System.out.println("Increase health!");
    }

}
