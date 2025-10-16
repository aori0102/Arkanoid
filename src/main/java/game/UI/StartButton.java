package game.UI;

import game.GameManager.GameManager;
import javafx.scene.input.MouseEvent;
import org.AnimationClipData;
import org.GameObject;

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

    @Override
    protected void baseButton_onPointerClicked(Object sender, MouseEvent e) {
        System.out.println("BaseButton_onPointerClicked");
        //GameManager.instance.startGame();
    }


}
