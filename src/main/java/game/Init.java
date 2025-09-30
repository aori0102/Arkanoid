package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.*;
import utils.Vector2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Init {

    public static void Init_Kine() {

    }

    public static void Init_Dui() {
//        GameObject ui2 = GameObjectManager.instantiate("TextUI");
//        Text text = new Text("Hiiii!");
//        text.setStyle("-fx-font-size: 100px; -fx-fill: blue;");
//
//        var textUI = ui2.addComponent(TextUI.class);
//        textUI.setText(text);
//
//        textUI.transform().setPosition(new Vector2(0,0));
//        Text testText = new Text("mao");
//        testText.setFont(Font.font(100));
//        testText.setTextOrigin(VPos.TOP);  ;
//
//        Bounds bounds = testText.getBoundsInParent();
//        Rectangle outline = new Rectangle(
//                bounds.getMinX(),
//                bounds.getMinY(),
//                bounds.getWidth(),
//                bounds.getHeight()
//        );
//        outline.setFill(Color.TRANSPARENT);
//        outline.setStroke(Color.RED);
//        Group g = new Group(outline, testText);
//
//        g.setTranslateX(0);
//        g.setTranslateY(0);
//
//        RendererManager.RegisterNode(g);

        GameObject ui = GameObjectManager.instantiate("UI");
        var buttonUI = ui.addComponent(ButtonUI.class);

        buttonUI.setImage("/TabBackgroundSelected.png");
        buttonUI.getTextUI().setText("Button");
        buttonUI.getTextUI().getText().setFont(Font.font("Arial", FontWeight.BOLD, 20));

        ui.getTransform().setGlobalPosition(new Vector2(0,0));


        buttonUI.setOnClick(()->{
            System.out.println("Bocchi clicked!");
        });
        buttonUI.setOnUp(()->{
            System.out.println("Bocchi up!");
        });
        buttonUI.setOnDown(()->{
            System.out.println("Bocchi down!");
        });
        buttonUI.setOnExit(()->{
            System.out.println("Bocchi exited!");
        });
        buttonUI.setOnEnter(()->{
            System.out.println("Bocchi entered!");
        });
    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {


    }

}
