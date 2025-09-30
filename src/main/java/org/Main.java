package org;

import game.Init;
import utils.Random;
import javafx.scene.Group;
import javafx.scene.Scene;
import utils.Time;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class Main extends Application {

    protected static Scene scene;
    protected static Group root;

    @Override
    public void start(Stage stage) throws Exception {

        Random.init();
        RendererManager.initializeMain(stage, scene, root);
        root = new Group();
        scene = new Scene(root, 1200, 800);

        RendererManager.initializeMain(stage, scene, root);
        Init.Init_Aori();
        Init.Init_Duc();
        Init.Init_Dui();
        Init.Init_Kine();

        // --- Game/render loop ---
        AnimationTimer loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // `now` = nanoseconds timestamp
                // Update game logic
                update();

            }
        };

        loop.start(); // 🚀 kick it off

    }

    /**
     * Update of every frame
     */
    private void update() {
        Time.updateTime();
        GameObjectManager.runUpdate();
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