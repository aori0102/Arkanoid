package org;

import ecs.*;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import utils.Vector2;

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

        ui.transform.setPosition(new Vector2(0,0));


        buttonUI.setOnClick(()->{
            System.out.println("Bocchi clicked!");
        });

    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {

    }

}
