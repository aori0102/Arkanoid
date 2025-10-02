package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.Player.Player;
import game.Player.PlayerPowerUpHandler;
import game.PowerUp.DuplicateBall;
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

        var kita = GameObjectManager.instantiate("Kita");
        kita.addComponent(Kita.class);

        var borderLeft = GameObjectManager.instantiate("Border_Left");
        borderLeft.getTransform().setLocalPosition(new Vector2(5.0, 400.0));
        borderLeft.addComponent(BoxCollider.class).setLocalSize(new Vector2(1.0, 2000.0));
        var borderLeftVisual = GameObjectManager.instantiate();
        borderLeftVisual.getTransform().setParent(borderLeft.getTransform());

        var borderRight = GameObjectManager.instantiate("Border_Right");
        borderRight.getTransform().setLocalPosition(new Vector2(900.0, 400.0));
        borderRight.addComponent(BoxCollider.class).setLocalSize(new Vector2(1.0, 2000.0));
        var borderRightVisual = GameObjectManager.instantiate();
        borderRightVisual.getTransform().setParent(borderRight.getTransform());

        var borderTop = GameObjectManager.instantiate("Border_Top");
        borderTop.getTransform().setLocalPosition(new Vector2(600.0, 5.0));
        borderTop.addComponent(BoxCollider.class).setLocalSize(new Vector2(2000.0, 1.0));
        var borderTopVisual = GameObjectManager.instantiate();
        borderTopVisual.getTransform().setParent(borderTop.getTransform());

        var borderBottom = GameObjectManager.instantiate("Border_Bottom");
        borderBottom.getTransform().setLocalPosition(new Vector2(600.0, 500.0));
        var collider = borderBottom.addComponent(BoxCollider.class);
        collider.setLocalSize(new Vector2(2000.0, 1.0));
        collider.setIncludeLayer(Layer._17.getUnderlyingValue());
        var borderBottomVisual = GameObjectManager.instantiate();
        borderBottomVisual.getTransform().setParent(borderBottom.getTransform());

    }

}
