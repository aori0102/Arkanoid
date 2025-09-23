package org;

import ecs.ButtonUI;
import ecs.GameObject;
import ecs.GameObjectManager;
import ecs.SpriteRenderer;
import utils.Vector2;

public class Init {

    public static void Init_Kine() {

    }

    public static void Init_Dui() {
        GameObject ui = GameObjectManager.instantiate("UI");
        var buttonUI=ui.addComponent(ButtonUI.class);
        buttonUI.setImage("/bocchi.png");
        ui.transform.setPosition(new Vector2(100.0,100.0));
        ui.transform.setScale(new Vector2(0.3,0.3));
        buttonUI.setOnClick(()->{
            System.out.println("Bocchi clicked!");
        });

    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {

    }

}
