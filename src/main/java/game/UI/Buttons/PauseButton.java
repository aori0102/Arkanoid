package game.UI.Buttons;

import org.Animation.AnimationClipData;
import org.GameObject.GameObject;

public class PauseButton extends BaseButton {
    public PauseButton(GameObject owner) {
        super(owner);
    }

    @Override
    protected void setupButtonAppearance() {
        idleKey = AnimationClipData.Pause_Button_Idle;
        hoverKey = AnimationClipData.Pause_Button_Hovered;
        pressedKey = AnimationClipData.Pause_Button_Pressed;
        releasedKey = AnimationClipData.Pause_Button_Released;
        clickedKey = AnimationClipData.Pause_Button_Clicked;
    }
}
