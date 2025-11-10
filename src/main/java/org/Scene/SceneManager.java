package org.Scene;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.Event.EventHandler;
import org.GameObject.GameObjectManager;
import org.Main;
import org.Scene.SceneBuilder.SceneBuilderManager;
import utils.NodeUtils;

import java.util.HashMap;

/**
 * SceneManager is responsible for managing multiple JavaFX scenes and their root node groups.
 * It acts similarly to Unity's SceneManager, allowing scenes to be created, loaded,
 * and populated with GameObjects that have visual components.
 *
 * <p>Each scene has a corresponding {@link Group} that holds its visual {@link Node}s.
 * GameObjects are registered into a scene by recursively collecting their nodes
 * (using {@link NodeUtils#collectNodes}) and attaching them to the scene’s Group.</p>
 *
 * <p>Only one stage (window) is managed by this class. Call {@link #initialize}
 * once at application startup to set up.</p>
 */
public class SceneManager {

    public static final SceneKey DEFAULT_SCENE_KEY = SceneKey.Menu;

    private static Stage mainStage;
    private static Scene scene;
    private static SceneKey currentSceneKey = SceneKey.Menu;
    private static SceneKey toBeLoadedKey = null;

    public static EventHandler<SceneKey> onSceneChanged = new EventHandler<>(SceneManager.class);

    public static void handleLoadScene() {
        if (toBeLoadedKey == null) {
            return;
        }
        try {
            System.out.println("[SceneManager] Loading Scene " + toBeLoadedKey);
            GameObjectManager.clearCurrentScene();
            currentSceneKey = toBeLoadedKey;
            SceneBuilderManager.buildScene(toBeLoadedKey);

            System.out.println("[SceneManager] Scene " + toBeLoadedKey + " loaded!");
            onSceneChanged.invoke(SceneManager.class, currentSceneKey);
        } catch (Exception e) {
            System.err.println("[SceneManager] Error while checking for loading scene: " + e.getMessage());
        }
        toBeLoadedKey = null;
    }

    /**
     * Initialize all scenes available within {@link SceneKey}.
     * Each scene will be provided with a {@code root} for per-scene
     * updating.
     *
     * @return The default scene.
     */
    public static Scene initialize(Stage stage) {

        var root = new Group();
        scene = new Scene(root, Main.STAGE_WIDTH, Main.STAGE_HEIGHT);
        scene.getStylesheets().add(
                SceneManager.class.getResource("/CSS/neon_style_font.css").toExternalForm()
        );
        mainStage = stage;

        SceneBuilderManager.buildScene(SceneKey.DoNotDestroyOnLoad);

        return scene;

    }

    /**
     * Loads and displays the specified scene on the main stage.
     *
     * @param sceneKey The name (key) of the scene to load.
     * @throws IllegalStateException    If the stage was not initialized.
     * @throws IllegalArgumentException If the scene key does not exist.
     */
    public static void loadScene(SceneKey sceneKey) {

        System.out.println("[SceneManager] Registering " + sceneKey + " for loading...");

        try {

            if (sceneKey == SceneKey.DoNotDestroyOnLoad) {
                throw new Exception("[SceneManager] Cannot load scene " + sceneKey + " because it is internal!");
            }

            if (mainStage == null) {
                throw new IllegalStateException("[SceneManager] SceneManager not initialized!");
            }

            toBeLoadedKey = sceneKey;

        } catch (Exception e) {
            System.err.println("[SceneManager] Error while checking for loading scene: " + e.getMessage());
        }

    }

    /**
     * Returns the key of the currently active scene.
     *
     * @return The current scene’s key, or {@code null} if none is loaded.
     */
    public static SceneKey getCurrentSceneKey() {
        return currentSceneKey;
    }

    public static Scene getScene() {
        return scene;
    }

}
