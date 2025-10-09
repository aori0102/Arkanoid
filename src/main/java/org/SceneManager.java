package org;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.NodeUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * SceneManager is responsible for managing multiple JavaFX scenes and their root node groups.
 * It acts similarly to Unity's SceneManager, allowing scenes to be created, loaded,
 * and populated with GameObjects that have visual components.
 *
 * <p>Each scene has a corresponding {@link Group} that holds its visual {@link Node}s.
 * GameObjects are registered into a scene by recursively collecting their nodes
 * (using {@link NodeUtils#collectNodes}) and attaching them to the scene’s Group.</p>
 *
 * <p>Only one stage (window) is managed by this class. Call {@link #initializeStage(Stage)}
 * once at application startup to set it up.</p>
 */
public class SceneManager {
    private static Stage mainStage;
    private static final HashMap<String, Scene> sceneMap = new HashMap<>();
    private static final HashMap<String, Group> sceneGroups = new HashMap<>();
    private static String currentSceneKey = null;

    /**
     * Initializes the JavaFX stage and creates the default scene.
     * <p>
     * This method should be called once at application startup.
     *
     * @param stage The primary stage provided by the JavaFX Application.
     */
    public static void initializeStage(Stage stage) {
        mainStage = stage;

        createScene("DefaultScene", 1200, 800);


        mainStage.setTitle("NigArkanoid");
        loadScene("DefaultScene");
        mainStage.show();
    }

    /**
     * Creates a new scene and its corresponding root group.
     *
     * @param sceneName The unique name (key) for this scene.
     * @param width     The width of the scene in pixels.
     * @param height    The height of the scene in pixels.
     * @return The newly created Scene.
     */
    public static Scene createScene(String sceneName, double width, double height) {
        Group root = new Group();
        Scene newScene = new Scene(root, width, height);

        sceneMap.put(sceneName, newScene);
        sceneGroups.put(sceneName, root);

        return newScene;
    }

    /**
     * Removes a scene and its associated group from memory.
     * <p>
     * If the scene is currently active, it will still be displayed
     * until another scene is loaded.
     *
     * @param sceneKey The name (key) of the scene to remove.
     * @throws IllegalArgumentException If the scene key does not exist.
     */
    public static void removeScene(String sceneKey) {
        if (!sceneMap.containsKey(sceneKey)) throw new IllegalArgumentException("Scene " + sceneKey + " not found!");

        sceneMap.remove(sceneKey);
        sceneGroups.remove(sceneKey);
    }

    /**
     * Loads and displays the specified scene on the main stage.
     *
     * @param sceneKey The name (key) of the scene to load.
     * @throws IllegalStateException    If the stage was not initialized.
     * @throws IllegalArgumentException If the scene key does not exist.
     */
    public static void loadScene(String sceneKey) {
        if (mainStage == null) throw new IllegalStateException("SceneManager not initialized!");
        if (!sceneMap.containsKey(sceneKey)) throw new IllegalArgumentException("Scene " + sceneKey + " not found!0");

        mainStage.setScene(sceneMap.get(sceneKey));
        currentSceneKey = sceneKey;
    }

    /**
     * Adds a GameObject (and all its visual children) to a given scene.
     * <p>
     * All {@link Node}s associated with the GameObject’s visual components
     * are collected using {@link NodeUtils#collectNodes}, and added to
     * the target scene’s root {@link Group}.
     *
     * @param gameObject The GameObject to add.
     * @param sceneKey   The key of the target scene.
     * @throws IllegalArgumentException If the scene does not exist.
     */
    public static void addGameObjectToScene(GameObject gameObject, String sceneKey) {
        if (!sceneMap.containsKey(sceneKey)) throw new IllegalArgumentException("Scene " + sceneKey + " not found!");
        if (gameObject.getRegisteredSceneKey() != null && !gameObject.getRegisteredSceneKey().equals(sceneKey)) {
            removeGameObjectFromScene(gameObject, gameObject.getRegisteredSceneKey());
        }
        List<Node> nodeList = NodeUtils.collectNodes(gameObject.getTransform());

        sceneGroups.get(sceneKey).getChildren().addAll(nodeList);
        gameObject.setRegisteredSceneKey(sceneKey);
    }

    /**
     * Removes a GameObject (and all its visual children) from the specified scene.
     *
     * @param gameObject The GameObject to remove.
     * @param sceneKey   The key of the scene to remove from.
     * @throws IllegalArgumentException If the scene does not exist.
     */
    public static void removeGameObjectFromScene(GameObject gameObject, String sceneKey) {
        if (!sceneMap.containsKey(sceneKey)) throw new IllegalArgumentException("Scene " + sceneKey + " not found!2");

        List<Node> nodeList = NodeUtils.collectNodes(gameObject.getTransform());

        sceneGroups.get(sceneKey).getChildren().removeAll(nodeList);
        gameObject.setRegisteredSceneKey(null);
    }

    /**
     * Adds a single {@link Node} (visual component) directly to the specified scene.
     *
     * @param node     The node to add.
     * @param sceneKey The key of the target scene.
     * @throws IllegalArgumentException If the scene does not exist.
     */
    public static void addNodeToScene(Node node, String sceneKey) {
        if (!sceneMap.containsKey(sceneKey)) throw new IllegalArgumentException("Scene " + sceneKey + " not found!3");

        sceneGroups.get(sceneKey).getChildren().add(node);
    }

    /**
     * Removes a single {@link Node} (visual component) from the specified scene.
     *
     * @param node     The node to remove.
     * @param sceneKey The key of the scene to remove from.
     * @throws IllegalArgumentException If the scene does not exist.
     */
    public static void removeNodeFromScene(Node node, String sceneKey) {
        if (!sceneMap.containsKey(sceneKey)) throw new IllegalArgumentException("Scene " + sceneKey + " not found!4");

        sceneGroups.get(sceneKey).getChildren().remove(node);
    }

    /**
     * Returns the key of the currently active scene.
     *
     * @return The current scene’s key, or {@code null} if none is loaded.
     */
    public static String getCurrentSceneKey() {
        return currentSceneKey;
    }

    /**
     * Returns the {@link Group} root node associated with a given scene.
     *
     * @param sceneKey The scene key.
     * @return The root {@link Group} for the scene, or {@code null} if not found.
     */
    public static Group getSceneGroup(String sceneKey) {
        return sceneGroups.get(sceneKey);
    }
}
