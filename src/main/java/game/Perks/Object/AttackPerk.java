package game.Perks.Object;

import game.Perks.Index.Perk;
import javafx.scene.input.MouseEvent;
import org.AnimationClipData;
import org.GameObject;

public class AttackPerk extends Perk {
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
        this.onPointerClicked.addListener(this::perk_onPointerClicked);
    }

    @Override
    protected void setUpVisual() {
        textUI.setText("Increase attack!");
        perkKey = AnimationClipData.Attach_Perk;

    }

    @Override
    protected void perk_onPointerClicked(Object sender, MouseEvent e) {
        System.out.println("Increase attack!");
    }
}
