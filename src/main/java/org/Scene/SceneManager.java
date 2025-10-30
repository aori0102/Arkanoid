package org.Scene;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.GameObject.GameObject;
import org.Main;
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
 * <p>Only one stage (window) is managed by this class. Call {@link #createScenes()}
 * once at application startup to set up.</p>
 */
public class SceneManager {

    public static final SceneKey DEFAULT_SCENE_KEY = SceneKey.Menu;

    private static Stage mainStage;
    private static final HashMap<SceneKey, Scene> sceneMap = new HashMap<>();
    private static SceneKey currentSceneKey = SceneKey.Menu;


    public static void init(Stage stage) {
        mainStage = stage;
    }
    /**
     * Initialize all scenes available within {@link SceneKey}.
     * Each scene will be provided with a {@code root} for per-scene
     * updating.
     *
     * @return The default scene.
     */
    public static Scene createScenes() {

        var sceneKeyArray = SceneKey.values();
        for (var sceneKey : sceneKeyArray) {
            var root = new Group();
            sceneMap.put(sceneKey, new Scene(root, Main.STAGE_WIDTH, Main.STAGE_HEIGHT));
        }

        return sceneMap.get(DEFAULT_SCENE_KEY);

    }

    /**
     * Loads and displays the specified scene on the main stage.
     *
     * @param sceneKey The name (key) of the scene to load.
     * @throws IllegalStateException    If the stage was not initialized.
     * @throws IllegalArgumentException If the scene key does not exist.
     */
    public static void loadScene(SceneKey sceneKey) {

        try {

            if (mainStage == null) {
                throw new IllegalStateException("SceneManager not initialized!");
            }

            if (!sceneMap.containsKey(sceneKey)) {
                throw new IllegalArgumentException("Scene " + sceneKey + " not found!");
            }

            mainStage.setScene(sceneMap.get(sceneKey));
            currentSceneKey = sceneKey;

        } catch (Exception e) {
            System.err.println(SceneManager.class.getSimpleName() + " | Error while loading scene: " + e.getMessage());
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

    /**
     * Returns the {@link Group} root node associated with a given scene.
     *
     * @param sceneKey The scene key.
     * @return The root {@link Group} for the scene, or {@code null} if not found.
     */
    public static Group getSceneRoot(SceneKey sceneKey) {
        return (Group) sceneMap.get(sceneKey).getRoot();
    }

    public static boolean isObjectInActiveScene(GameObject gameobject) {
        return gameobject.getRegisteredSceneKey() == currentSceneKey;
    }

    public static HashMap<SceneKey, Scene> getSceneMap() {
        return sceneMap;
    }

}
