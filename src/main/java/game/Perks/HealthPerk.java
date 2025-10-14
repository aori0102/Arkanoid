package game.Perks;

import org.GameObject;
import org.TextUI;
import org.TextVerticalAlignment;

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
    protected void setUpVisual() {
        textUI.setText("Increase health!");
        textUI.setVerticalAlignment(TextVerticalAlignment.Middle);

    }

    @Override
    protected void applyEffect() {

    }
}
