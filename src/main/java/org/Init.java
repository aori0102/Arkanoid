package org;

import ecs.GameObjectManager;
import ecs.SpriteRenderer;
import game.object.Paddle;

public class Init {

    public static void Init_Kine() {
        var paddle = GameObjectManager.instantiate("paddle");
        paddle.addComponent(SpriteRenderer.class);
        paddle.addComponent(ActionMap.class);
        paddle.addComponent(PlayerInput.class);
        paddle.addComponent(Paddle.class);

    }

    public static void Init_Dui() {

    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {

        // Init bocchi
        var bocchi = GameObjectManager.instantiate("Bocchi");
        bocchi.addComponent(SpriteRenderer.class);

    }

}
