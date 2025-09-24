package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.*;
import utils.Vector2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
        InitBocchi("Bocchi_2", new Vector2(45.12, 133.77), new Vector2(0.42, -0.87));
        InitBocchi("Bocchi_3", new Vector2(199.88, 12.34), new Vector2(-0.63, 0.58));
        InitBocchi("Bocchi_4", new Vector2(87.56, 175.44), new Vector2(0.91, -0.15));
        InitBocchi("Bocchi_5", new Vector2(12.78, 54.21), new Vector2(-0.32, -0.74));
        InitBocchi("Bocchi_6", new Vector2(166.09, 99.65), new Vector2(0.11, 0.95));
        InitBocchi("Bocchi_7", new Vector2(53.43, 187.20), new Vector2(-0.77, 0.23));
        InitBocchi("Bocchi_8", new Vector2(34.56, 68.19), new Vector2(0.64, -0.52));
        InitBocchi("Bocchi_9", new Vector2(142.87, 23.45), new Vector2(-0.05, 0.88));
        InitBocchi("Bocchi_10", new Vector2(19.99, 141.73), new Vector2(0.83, -0.39));
        InitBocchi("Bocchi_11", new Vector2(178.22, 65.31), new Vector2(-0.91, 0.14));

        var borderLeft = GameObjectManager.instantiate("Border_Left");
        borderLeft.transform.setLocalPosition(new Vector2(5.0, 400.0));
        borderLeft.addComponent(BoxCollider.class).setLocalSize(new Vector2(1.0, 2000.0));
        var borderLeftVisual = GameObjectManager.instantiate();
        borderLeftVisual.transform.setParent(borderLeft.transform);

        var borderRight = GameObjectManager.instantiate("Border_Right");
        borderRight.transform.setLocalPosition(new Vector2(900.0, 400.0));
        borderRight.addComponent(BoxCollider.class).setLocalSize(new Vector2(1.0, 2000.0));
        var borderRightVisual = GameObjectManager.instantiate();
        borderRightVisual.transform.setParent(borderRight.transform);

        var borderTop = GameObjectManager.instantiate("Border_Top");
        borderTop.transform.setLocalPosition(new Vector2(600.0, 5.0));
        borderTop.addComponent(BoxCollider.class).setLocalSize(new Vector2(2000.0, 1.0));
        var borderTopVisual = GameObjectManager.instantiate();
        borderTopVisual.transform.setParent(borderTop.transform);

        var borderBottom = GameObjectManager.instantiate("Border_Bottom");
        borderBottom.transform.setLocalPosition(new Vector2(600.0, 500.0));
        borderBottom.addComponent(BoxCollider.class).setLocalSize(new Vector2(2000.0, 1.0));
        var borderBottomVisual = GameObjectManager.instantiate();
        borderBottomVisual.transform.setParent(borderBottom.transform);

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
