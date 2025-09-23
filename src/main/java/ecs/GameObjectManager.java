package ecs;

import test.Test;

import java.util.HashSet;

public class GameObjectManager {

    private static final HashSet<GameObject> gameObjectSet = new HashSet<>();

    /**
     * Call all game objects upon Awake state.
     */
    public static void awake() {

        for (var object : gameObjectSet) {

            if (object.isActive()) {
                object.handleAwake();
            }

        }

    }

    /**
     * Call all game objects upon Start state.
     */
    public static void start() {

        for (var object : gameObjectSet) {

            if (object.isActive()) {
                object.handleStart();
            }

        }

    }

    /**
     * Call all game objects upon Update state.
     */
    public static void update() {

        for (var object : gameObjectSet) {

            if (object.isActive()) {
                object.handleUpdate();
            }

        }

    }

    /**
     * Call all game objects upon Late Update state.
     */
    public static void lateUpdate() {

        for (var object : gameObjectSet) {

            if (object.isActive()) {
                object.handleLateUpdate();
            }

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
        return newGameObject.getComponent(type);
    }

}
