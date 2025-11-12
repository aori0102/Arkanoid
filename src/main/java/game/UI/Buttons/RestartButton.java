package game.UI.Buttons;

import org.Animation.AnimationClipData;
import org.GameObject.GameObject;

public class RestartButton extends BaseButton{
    public RestartButton(GameObject owner) {
        super(owner);
    }

    @Override
    protected void setupButtonAppearance() {
        idleKey = AnimationClipData.Restart_Button_Idle;
        hoverKey = AnimationClipData.Restart_Button_Hovered;
        pressedKey = AnimationClipData.Restart_Button_Pressed;
        releasedKey = AnimationClipData.Restart_Button_Released;
        clickedKey = AnimationClipData.Restart_Button_Clicked;
    }
}
