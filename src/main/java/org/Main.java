package org;

import org.Audio.AudioManager;
import org.GameObject.GameObjectManager;
import org.Rendering.RendererManager;
import org.Rendering.VideoAsset;
import org.Scene.SceneKey;
import org.Scene.SceneManager;
import org.Utils.EditorView;
import org.Utils.FPSCounter;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;
import utils.Random;
import utils.Time;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import utils.UITween.TweenManager;

public class Main extends Application {

    public static final double STAGE_WIDTH = 1200;
    public static final double STAGE_HEIGHT = 800;


    @Override
    public void start(Stage stage) throws Exception {
        System.setProperty("vlcj.lib", "path/to/vlc"); // points to folder containing libvlc
        System.setProperty("jna.library.path", "path/to/vlc");

        // Optional: if discovery fails, set manually
        new NativeDiscovery().discover();

        VideoAsset.initializeVideoMedia();
        AudioManager.initializeAudioManager();

        Random.init();
        var defaultScene = SceneManager.initialize(stage);
        RendererManager.initializeRenderSystem(stage, defaultScene);
        FPSCounter.init();

        SceneManager.loadScene(SceneKey.Introduction);

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

    @Override
    public void stop() {
        GameObjectManager.nuke();
        System.out.println("Closing game");
    }

    /**
     * Update of every frame
     */
    private void update() {
        SceneManager.handleLoadScene();
        Time.update();
        GameObjectManager.runCycle();
        FPSCounter.update();
        TweenManager.update();
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