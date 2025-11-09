package game.UI.Options.Text;

import javafx.scene.paint.Color;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Vector2;

public class SFXVolumeText extends MonoBehaviour {
    private TextUI textUI;
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public SFXVolumeText(GameObject owner) {
        super(owner);

        textUI = owner.addComponent(TextUI.class);
        textUI.setVerticalAlignment(TextVerticalAlignment.Middle);
        textUI.setHorizontalAlignment(TextHorizontalAlignment.Center);
        textUI.setFontSize(50);
        textUI.setText("SFX Volume");
        textUI.setTextColor(Color.YELLOW);
        textUI.setFont(FontDataIndex.Jersey_25);
        textUI.getText().getStyleClass().add("neon-text");
    }
}
