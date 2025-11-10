package org.GameObject;

import org.Event.EventHandler;
import org.Main;

import java.util.*;

/**
 * Central brain for handling game object's creation, life cycle
 * and removal.
 */
public class GameObjectManager {

    private static final LinkedHashSet<GameObject> gameObjectSet = new LinkedHashSet<>();
    private static final Queue<GameObject> addedGameObjectQueue = new LinkedList<>();
    private static final Queue<GameObject> removedObjectQueue = new LinkedList<>();

    public static EventHandler<GameObject> onGameObjectInstantiated = new EventHandler<>(GameObjectManager.class);
    public static EventHandler<GameObject> onGameObjectDestroyed = new EventHandler<>(GameObjectManager.class);

    private static boolean sceneUpdateAbortion = false;

    /**
     * Run the cycle for all {@link GameObject} for the current frame in the
     * order Awake - Start - Update - Late Update - Clean Up.<br><br>
     * <b><i><u>NOTE</u> : Only call within {@link Main}.</i></b>
     */
    public static void runCycle() {

        sceneUpdateAbortion = false;

        awake();
        start();
        update();
        lateUpdate();
        cleanUpDestroyed();
        reboot();

    }

    /**
     * Call all game objects upon Awake state.
     */
    private static void awake() {

        for (var object : gameObjectSet) {

            if (sceneUpdateAbortion) {
                return;
            }

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

            if (sceneUpdateAbortion) {
                return;
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

            if (sceneUpdateAbortion) {
                return;
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

            if (sceneUpdateAbortion) {
                return;
            }

        }

    }

    /**
     * Clean up any destroyed object from scene.
     */
    private static void cleanUpDestroyed() {

        while (!removedObjectQueue.isEmpty()) {

            var destroyed = removedObjectQueue.poll();
            destroyed.clearData();

            if (sceneUpdateAbortion) {
                return;
            }

            onGameObjectDestroyed.invoke(null, destroyed);
            unregisterGameObject(destroyed);
        }

    }

    /**
     * Reboot the frame by adding the queued game objects
     * and query each object's children modification.
     */
    private static void reboot() {

        if (sceneUpdateAbortion) {
            return;
        }

        // Added queried objects
        while (!addedGameObjectQueue.isEmpty()) {

            var added = addedGameObjectQueue.poll();
            registerGameObject(added);

        }

        // Process children for each object
        for (var object : gameObjectSet) {
            object.processChildSet();
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
     * Create an empty game object with the specified name.
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

        if (gameObjectSet.contains(gameObject) && !gameObject.isDestroyed() && !removedObjectQueue.contains(gameObject)) {
            removedObjectQueue.offer(gameObject);
            gameObject.markDestroyed();

            System.out.println("Destroy:" + gameObject);
        }

    }

    /**
     * Clear all objects from the current scene, except the ones marked with
     * {@link GameObject#isDoNotDestroyOnLoad}.
     */
    public static void clearCurrentScene() {

        System.out.println("[GameObjectManager] Clearing game object scene");
        for (var object : gameObjectSet) {
            if (object.isDestroyed() || object.isDoNotDestroyOnLoad()) {
                continue;
            }
            if (removedObjectQueue.contains(object)) {
                continue;
            }
            try {
                removedObjectQueue.offer(object);
                object.markDestroyed();
                object.clearData();
            } catch (Exception e) {
                System.err.println("[GameObjectManager] Possible fatal exception: " + e.getMessage());
                System.err.println(Arrays.toString(e.getStackTrace()));
            }
        }
        addedGameObjectQueue.removeIf(gameObject -> gameObject.isDestroyed() || !gameObject.isDoNotDestroyOnLoad());
        while (!removedObjectQueue.isEmpty()) {
            gameObjectSet.remove(removedObjectQueue.poll());
        }

        sceneUpdateAbortion = true;

    }

    /**
     * Clear every game objects.
     * <p>
     * <b><i><u>NOTE</u> : Only call this function when closing the application.</i></b>
     * </p>
     */
    public static void nuke() {

        System.out.println("[GameObjectManager] Nuking every game objects");
        for (var object : gameObjectSet) {
            if (object.isDestroyed()) {
                continue;
            }
            object.markDestroyed();
            object.clearData();
        }
        gameObjectSet.clear();
        addedGameObjectQueue.clear();
        removedObjectQueue.clear();

        sceneUpdateAbortion = true;

    }

}