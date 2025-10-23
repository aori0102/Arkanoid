package org.GameObject;

import org.Event.EventHandler;

import java.util.*;

public class GameObjectManager {

    private static final LinkedHashSet<GameObject> gameObjectSet = new LinkedHashSet<>();
    private static final Queue<GameObject> addedGameObjectQueue = new LinkedList<>();
    public static EventHandler<GameObject> onGameObjectInstantiated = new EventHandler<>(GameObjectManager.class);
    public static EventHandler<GameObject> onGameObjectDestroyed = new EventHandler<>(GameObjectManager.class);

    /**
     * Run all update for the current frame in the
     * order Awake - Start - Update - Late Update - Clean Up.
     */
    public static void runUpdate() {

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
                destroyedQueue.offer(object);
            } else {
                object.processChildSet();
            }
        }

        while (!destroyedQueue.isEmpty()) {

            var destroyed = destroyedQueue.poll();
            onGameObjectDestroyed.invoke(null, destroyed);
            unregisterGameObject(destroyed);

        }

        while (!addedGameObjectQueue.isEmpty()) {

            var added = addedGameObjectQueue.poll();
            registerGameObject(added);

        }

    }

    /**
     * Register a game object to the query list.
     *
     * @param gameObject The game object to register.
     */
    private static void registerGameObject(GameObject gameObject) {
        gameObjectSet.add(gameObject);
    }

    /**
     * Unregister a game object from the query list.
     *
     * @param gameObject The game object to unregister.
     */
    private static void unregisterGameObject(GameObject gameObject) {
        gameObjectSet.remove(gameObject);
    }

    /**
     * Create an empty game object.
     *
     * @return An empty game object.
     */
    public static GameObject instantiate() {
        var newGameObject = new GameObject();
        addedGameObjectQueue.add(newGameObject);

        onGameObjectInstantiated.invoke(null, newGameObject);

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
        addedGameObjectQueue.add(newGameObject);

        onGameObjectInstantiated.invoke(null, newGameObject);

        return newGameObject;
    }

    /**
     * Destroy a game object and all of its components.
     *
     * @param gameObject The game object to destroy.
     */
    public static void destroy(GameObject gameObject) {

        if (gameObjectSet.contains(gameObject)) {
            gameObject.destroyObject();
        }

    }

    /**
     * Find the first GameObject with the given name.
     *
     * @param name The name of the GameObject to find.
     * @return The GameObject, or null if not found.
     */
    public static GameObject find(String name) {
        for (var obj : gameObjectSet) {
            if (obj.getName().equals(name)) {
                return obj;
            }
        }
        return null;
    }

}
