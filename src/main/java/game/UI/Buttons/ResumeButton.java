package game.UI.Buttons;

import org.Animation.AnimationClipData;
import org.GameObject.GameObject;

public class ResumeButton extends BaseButton {
    public ResumeButton(GameObject owner) {
        super(owner);
    }

    @Override
    protected void setupButtonAppearance() {
        idleKey = AnimationClipData.Resume_Button_Idle;
        hoverKey = AnimationClipData.Resume_Button_Hovered;
        pressedKey = AnimationClipData.Resume_Button_Pressed;
        releasedKey = AnimationClipData.Resume_Button_Released;
        clickedKey = AnimationClipData.Resume_Button_Clicked;
    }
}
