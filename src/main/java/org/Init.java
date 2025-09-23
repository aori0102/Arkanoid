package org;

import ecs.GameObjectManager;
import ecs.SpriteRenderer;
import test.Bocchi;

public class Init {

    public static void Init_Kine() {

    }

    public static void Init_Dui() {

    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {

        // Init bocchi
        var bocchi = GameObjectManager.instantiate("Bocchi");
        bocchi.addComponent(Bocchi.class);
        bocchi.addComponent(SpriteRenderer.class);

    }

}
