package game.UI;

import javafx.scene.input.MouseEvent;
import org.Animation.AnimationClipData;
import org.GameObject.GameObject;

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

    @Override
    protected void baseButton_onPointerClicked(Object sender, MouseEvent e) {

    }
}