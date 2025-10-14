package game;

import org.*;
import org.UI.ContinueButton;
import org.UI.StartButton;
import utils.Vector2;


import java.sql.Statement;

public class Init {

    public static void Init_Kine() {

    }

    public static void Init_Dui() {
        var startButton = GameObjectManager.instantiate("startButton");
        startButton.addComponent(StartButton.class);
        startButton.getTransform().setGlobalPosition(new Vector2(200, 200));
    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {

    }

}
