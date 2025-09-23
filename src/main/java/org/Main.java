package org;

import ecs.EditorManager;
import ecs.RendererManager;
import javafx.scene.Group;
import javafx.scene.Scene;
import utils.Time;
import ecs.GameObjectManager;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        RendererManager.initializeMain(stage);
        Init.Init_Aori();
        Init.Init_Duc();
        Init.Init_Dui();
        Init.Init_Kine();
        GameObjectManager.initializeAll();

        var params = getParameters().getRaw();
        if (params.contains("--editor_enabled")) {
            EditorManager.initializeEditor();
        }

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
        GameObjectManager.awake();
        GameObjectManager.start();
        GameObjectManager.update();
        GameObjectManager.lateUpdate();
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