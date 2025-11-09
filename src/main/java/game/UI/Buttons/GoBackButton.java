package game.UI.Buttons;

import org.Animation.AnimationClipData;
import org.GameObject.GameObject;

public class GoBackButton extends BaseButton {
    public GoBackButton(GameObject owner) {
        super(owner);
    }

    @Override
    protected void setupButtonAppearance() {
        idleKey = AnimationClipData.GoBack_Button_Idle;
        hoverKey = AnimationClipData.GoBack_Button_Hovered;
        pressedKey = AnimationClipData.GoBack_Button_Pressed;
        releasedKey = AnimationClipData.GoBack_Button_Released;
        clickedKey = AnimationClipData.GoBack_Button_Clicked;
    }
}
