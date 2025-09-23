package org;

import ecs.GameObjectManager;
import ecs.SpriteRenderer;
import test.Test;

public class Init {

    public static void Init_Kine() {

    }

    public static void Init_Dui() {

    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {
        var obj = GameObjectManager.instantiate("test");
        obj.addComponent(Test.class);
        obj.addComponent(SpriteRenderer.class);
    }

}
