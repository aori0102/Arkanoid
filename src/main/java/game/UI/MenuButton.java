package game.UI;

import javafx.scene.input.MouseEvent;
import org.AnimationClipData;
import org.GameObject;

public class MenuButton extends BaseButton{
    public MenuButton(GameObject owner) {
        super(owner);
    }

    @Override
    protected void setupButtonAppearance() {
        idleKey = AnimationClipData.Menu_Button_Idle;
        hoverKey = AnimationClipData.Menu_Button_Hovered;
        pressedKey = AnimationClipData.Menu_Button_Pressed;
        releasedKey = AnimationClipData.Menu_Button_Released;
        clickedKey = AnimationClipData.Menu_Button_Clicked;
    }

    @Override
    protected void baseButton_onPointerClicked(Object sender, MouseEvent e) {

    }
}
