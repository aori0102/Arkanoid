package org;

import ecs.UI.ButtonUI;
import ecs.GameObjectManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class Main extends Application {

    private int x = 100;
    private int y = 100;
    private Group root = null;

    @Override
    public void start(Stage stage) throws Exception {

        // root node (empty container for now)
        root = new Group();

        // scene = like your canvas
        Scene scene = new Scene(root, 1200, 800);

        stage.setTitle("NigArkanoid");
        stage.setScene(scene);
        stage.show(); // 🚀 show the window

        // --- Game/render loop ---
        AnimationTimer loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // `now` = nanoseconds timestamp
                // Update game logic
                update();

                // Render/draw (JavaFX handles actual drawing)
                render();
            }
        };

        GameObjectManager.InitializeAll();

        loop.start(); // 🚀 kick it off

    }

    private void update() {
        GameObjectManager.Awake();
        GameObjectManager.Start();
        GameObjectManager.Update();
        GameObjectManager.LateUpdate();
    }

    private void render() {

    }

    private void updateBall(int deltaX, int deltaY) {

        x += deltaX;
        y += deltaY;

        Circle circle = new Circle(20, Color.GREEN);
        circle.setCenterX(x);
        circle.setCenterY(y);
        root.getChildren().add(circle);

    }

    /**
     * Kick off application.
     *
     * @param args Application arguments.
     */
    public static void main(String[] args) {

        launch(args);

    }

}