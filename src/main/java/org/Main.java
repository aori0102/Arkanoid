package org;

import game.Init;
import org.GameObject.GameObjectManager;
import org.Rendering.RendererManager;
import org.Scene.SceneManager;
import org.Utils.EditorView;
import utils.Random;
import utils.Time;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class Main extends Application {

    public static final double STAGE_WIDTH = 1200;
    public static final double STAGE_HEIGHT = 800;


    @Override
    public void start(Stage stage) throws Exception {

        Random.init();
        SceneManager.init(stage);
        var defaultScene = SceneManager.createScenes();
        RendererManager.initializeRenderSystem(stage, defaultScene);
        EditorView.init();
        Init.initManager();
        Init.initMenu();
        Init.initGame();
        Init.initRecord();
        Init.Init_Aori();
        Init.Init_Duc();
        Init.Init_Dui();
        Init.Init_Kine();

        EditorView.wakeHierarchy();

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
        Time.update();
        GameObjectManager.runCycle();
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