package game.UI;

import javafx.scene.input.MouseEvent;
import org.Animation.AnimationClipData;
import org.GameObject.GameObject;

public class OptionsButton extends BaseButton {
    public OptionsButton(GameObject owner) {
        super(owner);
    }

    @Override
    protected void setupButtonAppearance() {
        idleKey = AnimationClipData.Options_Button_Idle;
        hoverKey = AnimationClipData.Options_Button_Hovered;
        pressedKey = AnimationClipData.Options_Button_Pressed;
        releasedKey = AnimationClipData.Options_Button_Released;
        clickedKey = AnimationClipData.Options_Button_Clicked;
    }

    @Override
    protected void baseButton_onPointerClicked(Object sender, MouseEvent e) {

    }
}