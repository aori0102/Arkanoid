package game.UI.Buttons;

import org.Animation.AnimationClipData;
import org.GameObject.GameObject;

public class RecordButton extends BaseButton {
    public RecordButton(GameObject owner) {
        super(owner);
    }

    @Override
    protected void setupButtonAppearance() {
        idleKey = AnimationClipData.Record_Button_Idle;
        hoverKey = AnimationClipData.Record_Button_Hovered;
        pressedKey = AnimationClipData.Record_Button_Pressed;
        releasedKey = AnimationClipData.Record_Button_Released;
        clickedKey = AnimationClipData.Record_Button_Clicked;
    }


}
