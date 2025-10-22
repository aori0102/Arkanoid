package game.Perks.Object;

import game.Perks.Index.Perk;
import javafx.scene.input.MouseEvent;
import org.Animation.AnimationClipData;
import org.GameObject.GameObject;
import utils.Random;

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
        randomTime = Random.range(0, 3);
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
