package org;

import ecs.BoxCollider;
import ecs.GameObjectManager;
import ecs.SpriteRenderer;
import test.Bocchi;
import utils.Vector2;

public class Init {

    public static void Init_Kine() {

    }

    public static void Init_Dui() {

    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {
        //InitBocchi("Bocchi_1", new Vector2(100.0, 100.0), new Vector2(1.0, 0.0));
        //InitBocchi("Bocchi_2", new Vector2(500.0, 100.0), new Vector2(-1.0, 0.0));

        // Init bocchi
        InitBocchi("Bocchi_1", new Vector2(168.39, 242.65), new Vector2(-0.95, -0.21));
        InitBocchi("Bocchi_2", new Vector2(231.35, 145.84), new Vector2(0.03, -0.78));
        InitBocchi("Bocchi_3", new Vector2(152.72, 25.79), new Vector2(0.58, -0.57));
        InitBocchi("Bocchi_4", new Vector2(15.8, 143.32), new Vector2(-0.35, 0.54));
        InitBocchi("Bocchi_5", new Vector2(259.2, 299.83), new Vector2(-0.29, -0.88));

        var borderLeft = GameObjectManager.instantiate("Border_Left");
        borderLeft.transform.setLocalPosition(new Vector2(5.0, 400.0));
        borderLeft.addComponent(BoxCollider.class).setLocalSize(new Vector2(1.0, 2000.0));
        var borderLeftVisual = GameObjectManager.instantiate();
        borderLeftVisual.transform.setParent(borderLeft.transform);
        //borderLeftVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

        var borderRight = GameObjectManager.instantiate("Border_Right");
        borderRight.transform.setLocalPosition(new Vector2(900.0, 400.0));
        borderRight.addComponent(BoxCollider.class).setLocalSize(new Vector2(1.0, 2000.0));
        var borderRightVisual = GameObjectManager.instantiate();
        borderRightVisual.transform.setParent(borderRight.transform);
        //borderRightVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

        var borderTop = GameObjectManager.instantiate("Border_Top");
        borderTop.transform.setLocalPosition(new Vector2(600.0, 5.0));
        borderTop.addComponent(BoxCollider.class).setLocalSize(new Vector2(2000.0, 1.0));
        var borderTopVisual = GameObjectManager.instantiate();
        borderTopVisual.transform.setParent(borderTop.transform);
        //borderTopVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

        var borderBottom = GameObjectManager.instantiate("Border_Bottom");
        borderBottom.transform.setLocalPosition(new Vector2(600.0, 500.0));
        borderBottom.addComponent(BoxCollider.class).setLocalSize(new Vector2(2000.0, 1.0));
        var borderBottomVisual = GameObjectManager.instantiate();
        borderBottomVisual.transform.setParent(borderBottom.transform);
        //borderBottomVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

    }

    private static void InitBocchi(String name, Vector2 position, Vector2 movement) {

        var bocchi = GameObjectManager.instantiate(name);

        var bocchiVisual = GameObjectManager.instantiate(name);
        bocchiVisual.transform.setParent(bocchi.transform);

        bocchi.addComponent(Bocchi.class).setMovement(movement);

        bocchiVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");
        bocchiVisual.transform.setLocalScale(new Vector2(0.068, 0.068));

        bocchi.transform.setLocalPosition(position);

    }

}
