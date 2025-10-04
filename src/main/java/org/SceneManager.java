package org;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.HashSet;

public class SceneManager {
    private static Stage mainStage;
    private static final HashMap<String, Scene> sceneMap = new HashMap<String, Scene>();
    private static final HashMap<String, HashSet<GameObject>> sceneObjects = new HashMap<>();
    private static String currentSceneKey = null;

    public static void initializeMain(Stage stage) {
        mainStage = stage;

        Group root = new Group();
        Scene defaultScene = new Scene(root, 1200, 800);
        addScene(defaultScene, "DefaultScene");

        mainStage.setTitle("NigArkanoid");
        loadScene("DefaultScene");
        mainStage.show();
    }

    public static void addScene(Scene scene, String sceneKey) {
        sceneMap.put(sceneKey, scene);
        sceneObjects.put(sceneKey, new HashSet<>());
    }

    public static void removeScene(String sceneKey) {
        sceneMap.remove(sceneKey);
    }

    public static void loadScene(String sceneKey) {
        if (mainStage == null) throw new IllegalStateException("SceneManager not initialized!");
        if (!sceneMap.containsKey(sceneKey)) throw new IllegalArgumentException("Scene " + sceneKey + " not found!");

        mainStage.setScene(sceneMap.get(sceneKey));
        currentSceneKey = sceneKey;
    }

    public static void addGameObjectToScene(GameObject gameObject, String sceneKey) {
        if(!sceneObjects.containsKey(sceneKey)) throw new IllegalArgumentException("Scene " + sceneKey + " not found!");
        sceneObjects.get(sceneKey).add(gameObject);
    }

    public static String getCurrentSceneKey() {
        return currentSceneKey;
    }
}
