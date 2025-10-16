package org.UI;

import javafx.scene.input.MouseEvent;
import org.AnimationClipData;
import org.GameObject;

public class ContinueButton extends BaseButton {
    public ContinueButton(GameObject owner) {
        super(owner);
    }

    @Override
    protected void setupButtonAppearance() {
        idleKey = AnimationClipData.Continue_Button_Idle;
        hoverKey = AnimationClipData.Continue_Button_Hovered;
        pressedKey = AnimationClipData.Continue_Button_Pressed;
        releasedKey = AnimationClipData.Continue_Button_Released;
        clickedKey = AnimationClipData.Continue_Button_Clicked;
    }

    @Override
    protected void baseButton_onPointerClicked(Object sender, MouseEvent e) {

    }
}
