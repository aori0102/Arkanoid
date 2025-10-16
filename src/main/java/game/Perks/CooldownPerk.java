package game.Perks;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.AnimationClipData;
import org.GameObject;
import org.TextHorizontalAlignment;
import org.TextVerticalAlignment;
import utils.Vector2;

public class CooldownPerk extends Perk {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public CooldownPerk(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        super.awake();
        this.onPointerClicked.addListener(this::perk_onPointerClicked);
    }

    @Override
    protected void setUpVisual() {
        textUI.setText("Decrease cooldown");
        perkKey = AnimationClipData.Cooldown_Perk;

    }

    @Override
    protected void perk_onPointerClicked(Object sender, MouseEvent e) {
        System.out.println("Decrease cooldown!");
    }
}
