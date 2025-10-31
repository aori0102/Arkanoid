package game.UI.Buttons;

import org.Animation.AnimationClipData;
import org.GameObject.GameObject;
import org.Rendering.SpriteRenderer;

public class StartButton extends BaseButton {

    public StartButton(GameObject owner) {
        super(owner);
    }

    @Override
    protected void setupButtonAppearance() {
        idleKey = AnimationClipData.Start_Button_Idle;
        hoverKey = AnimationClipData.Start_Button_Hovered;
        pressedKey = AnimationClipData.Start_Button_Pressed;
        releasedKey = AnimationClipData.Start_Button_Released;
        clickedKey = AnimationClipData.Start_Button_Clicked;
    }

}
