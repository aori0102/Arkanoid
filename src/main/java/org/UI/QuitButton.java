package org.UI;

import org.AnimationClipData;
import org.GameObject;

public class QuitButton extends BaseButton {
    public QuitButton(GameObject owner) {
        super(owner);
    }

    @Override
    protected void setupButtonAppearance() {
        idleKey = AnimationClipData.Quit_Button_Idle;
        hoverKey = AnimationClipData.Quit_Button_Hovered;
        pressedKey = AnimationClipData.Quit_Button_Pressed;
        releasedKey = AnimationClipData.Quit_Button_Released;
        clickedKey = AnimationClipData.Quit_Button_Clicked;
    }
}