package org;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class GameObjectManager {

    private static final HashSet<GameObject> gameObjectSet = new HashSet<>();

    /**
     * Run all update for the current frame in the
     * order Awake - Start - Update - Late Update - Clean Up.
     */
    protected static void runUpdate() {

        awake();
        start();
        update();
        lateUpdate();
        cleanUp();

    }

    /**
     * Call all game objects upon Awake state.
     */
    private static void awake() {

        for (var object : gameObjectSet) {

            if (!object.isDestroyed() && object.isActive()) {
                object.handleAwake();
            }

        }

    }

    /**
     * Call all game objects upon Start state.
     */
    private static void start() {

        for (var object : gameObjectSet) {

            if (!object.isDestroyed() && object.isActive()) {
                object.handleStart();
            }

        }

    }

    /**
     * Call all game objects upon Update state.
     */
    private static void update() {

        for (var object : gameObjectSet) {

            if (!object.isDestroyed() && object.isActive()) {
                object.handleUpdate();
            }

        }

    }

    /**
     * Call all game objects upon Late Update state.
     */
    private static void lateUpdate() {

        for (var object : gameObjectSet) {

            if (!object.isDestroyed() && object.isActive()) {
                object.handleLateUpdate();
            }

        }

    }

    /**
     * Clean any destroyed object from scene.
     */
    private static void cleanUp() {

        Queue<GameObject> destroyedQueue = new LinkedList<>();
        for (var object : gameObjectSet) {
            if (object.isDestroyed()) {
                destroyedQueue.add(object);
            }
        }

        while (!destroyedQueue.isEmpty()) {

            var destroyed = destroyedQueue.poll();
            gameObjectSet.remove(destroyed);

        }

    }

    /**
     * Register a game object to the query list.
     *
     * @param gameObject The game object to register.
     */
    private static void RegisterGameObject(GameObject gameObject) {
        gameObjectSet.add(gameObject);
    }

    /**
     * Unregister a game object from the query list.
     *
     * @param gameObject The game object to unregister.
     */
    private static void UnregisterGameObject(GameObject gameObject) {
        gameObjectSet.remove(gameObject);
    }

    /**
     * Create an empty game object.
     *
     * @return An empty game object.
     */
    public static GameObject instantiate() {
        var newGameObject = new GameObject();
        RegisterGameObject(newGameObject);
        SceneManager.addGameObjectToScene(newGameObject, SceneManager.getCurrentSceneKey());

        return newGameObject;
    }

    /**
     * Create an empty game object.
     *
     * @param name The name for the game object.
     * @return An empty game object.
     */
    public static GameObject instantiate(String name) {
        var newGameObject = new GameObject(name);
        RegisterGameObject(newGameObject);
        SceneManager.addGameObjectToScene(newGameObject, SceneManager.getCurrentSceneKey());

        return newGameObject;
    }

    /**
     * Copy a game object along with all of its component.
     *
     * @param source The source game object to copy from.
     * @return The copied game object.
     */
    public static GameObject instantiate(GameObject source) {
        var newGameObject = new GameObject(source);
        RegisterGameObject(newGameObject);
        SceneManager.addGameObjectToScene(newGameObject, SceneManager.getCurrentSceneKey());

        return newGameObject;
    }

    /**
     * Create a copy of the game object that the MonoBehaviour
     * is attached to. All the MonoBehaviour attached to the
     * source will be cloned.
     *
     * @param monoBehaviour The source MonoBehaviour.
     * @param type          The type of the MonoBehaviour.
     * @param <T>           The type of the MonoBehaviour.
     * @return The copied game object.
     */
    public static <T extends MonoBehaviour> T instantiate(MonoBehaviour monoBehaviour, Class<T> type) {
        var newGameObject = new GameObject(monoBehaviour.gameObject);
        RegisterGameObject(newGameObject);
        SceneManager.addGameObjectToScene(newGameObject, SceneManager.getCurrentSceneKey());

        return newGameObject.getComponent(type);
    }

    /**
     * Destroy a game object and all of its components.
     *
     * @param gameObject The game object to destroy.
     */
    public static void destroy(GameObject gameObject) {

        if (gameObjectSet.contains(gameObject)) {
            gameObject.destroyObject();
            SceneManager.removeGameObjectFromScene(gameObject, gameObject.getRegisteredSceneKey());
        }

    }

}
