package game;

import org.*;

public class Init {

    public static void Init_Kine() {
        var bocchiObject = GameObjectManager.instantiate("Bocchi");
        bocchiObject.addComponent(Test.class);
    }

    public static void Init_Dui() {

    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {

    }

}
