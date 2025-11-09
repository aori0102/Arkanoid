package org.Scene;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.Event.EventHandler;
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
 * <p>Only one stage (window) is managed by this class. Call {@link #createScenes}
 * once at application startup to set up.</p>
 */
public class SceneManager {

    public static final SceneKey DEFAULT_SCENE_KEY = SceneKey.Menu;

    private static Stage mainStage;
    private static final HashMap<SceneKey, Scene> sceneMap = new HashMap<>();
    private static SceneKey currentSceneKey = SceneKey.Menu;

    public static EventHandler<SceneKey> onSceneChanged = new EventHandler<>(SceneManager.class);

    /**
     * Initialize all scenes available within {@link SceneKey}.
     * Each scene will be provided with a {@code root} for per-scene
     * updating.
     *
     * @return The default scene.
     */
    public static Scene createScenes(Stage stage) {

        var sceneKeyArray = SceneKey.values();
        for (var sceneKey : sceneKeyArray) {
            var root = new Group();
            var scene = new Scene(root, Main.STAGE_WIDTH, Main.STAGE_HEIGHT);
            scene.getStylesheets().add(
                    SceneManager.class.getResource("/CSS/neon_style_font.css").toExternalForm()
            );
            sceneMap.put(sceneKey, scene);
        }
        mainStage = stage;

        SceneBuilderManager.buildScene(SceneKey.DoNotDestroyOnLoad);

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

            if (sceneKey == SceneKey.DoNotDestroyOnLoad) {
                throw new Exception("Cannot load scene " + sceneKey + " because it is internal!");
            }

            if (mainStage == null) {
                throw new IllegalStateException("SceneManager not initialized!");
            }

            if (!sceneMap.containsKey(sceneKey)) {
                throw new IllegalArgumentException("Scene " + sceneKey + " not found!");
            }

            mainStage.setScene(sceneMap.get(sceneKey));
            currentSceneKey = sceneKey;
            SceneBuilderManager.buildScene(sceneKey);

            System.out.println("[Scene Manager] Scene " + sceneKey + " loaded!");
            onSceneChanged.invoke(SceneManager.class, currentSceneKey);

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
     * Get a copy of the scene map which contains {@link Scene} from JavaFX mapping to their respective
     * {@link SceneKey}.
     *
     * @return A copy of the scene map.
     */
    public static HashMap<SceneKey, Scene> getSceneMap() {
        return sceneMap;
    }

}
