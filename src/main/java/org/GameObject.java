package org;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class GameObject {

    private static final String DEFAULT_NAME = "GameObject";

    private final Queue<MonoBehaviour> preAwakeMonoBehaviourQueue = new LinkedList<>();
    private final Queue<MonoBehaviour> preStartMonoBehaviourQueue = new LinkedList<>();
    private final HashSet<MonoBehaviour> monoBehaviourSet = new HashSet<>();

    private boolean isActive;
    private boolean isDestroyed;

    private final Transform transform;
    private String name;
    private Layer layer;

    private String registeredSceneKey = null;

    /**
     * Only access within {@link GameObject} or {@link GameObjectManager}.
     */
    protected HashSet<GameObject> childSet;

    public Transform getTransform() {

        ValidateObjectLife();

        return transform;

    }

    /**
     * Set the game object's layer
     *
     * @param layer The layer to set, must be single bit.
     */
    public void setLayer(Layer layer) {

        ValidateObjectLife();

        var value = layer.getUnderlyingValue();
        if (value == 0 || (value & (value - 1)) != 0) {
            throw new IllegalStateException("Layer " + value + " must be single bit (A game object cannot be in two or more layers");
        }

        this.layer = layer;

    }

    /**
     * Get this game object's layer in bitmask
     *
     * @return The layer in bitmask.
     */
    public int getLayerMask() {

        ValidateObjectLife();

        return layer.getUnderlyingValue();

    }

    /**
     * Get this game object's name.
     *
     * @return The game object's name.
     */
    public String getName() {

        ValidateObjectLife();

        return name;

    }

    /**
     * Check if this game object is destroyed.
     *
     * @return {@code true} if is destroyed, otherwise {@code false}.
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Set the name for this game object's.
     *
     * @param name The name to set.
     */
    public void setName(String name) {

        ValidateObjectLife();

        this.name = name;

    }

    /**
     * Add a child to this object.
     * Only access within {@link Transform}.
     *
     * @param child The child to add.
     */
    protected void addChild(GameObject child) {
        childSet.add(child);
    }

    /**
     * Remove a child from this object.
     * Only access within {@link Transform}.
     *
     * @param child The child to remove.
     */
    protected void removeChild(GameObject child) {
        childSet.remove(child);
    }

    /**
     * Return the activeness of this game object.
     *
     * @return {@code true} if this object is enabled, otherwise {@code false}.
     */
    public boolean isActive() {

        ValidateObjectLife();

        var parent = transform.getParent();
        boolean parentActive = parent == null || parent.gameObject.isActive;
        return isActive && parentActive;

    }

    /**
     * Set the activeness of this game object.
     *
     * @param active Enable or disable.
     */
    public void setActive(boolean active) {

        ValidateObjectLife();

        isActive = active;

    }

    /**
     * Handle Awake state. Should only be called within {@link GameObjectManager}.
     */
    protected void handleAwake() {

        ValidateObjectLife();

        while (!preAwakeMonoBehaviourQueue.isEmpty()) {

            var mono = preAwakeMonoBehaviourQueue.poll();
            mono.awake();
            try {
                ValidateObjectLife();
            } catch (Exception e) {
                return;
            }
            preStartMonoBehaviourQueue.offer(mono);

        }

    }

    /**
     * Handle Start state. Should only be called within {@link GameObjectManager}.
     */
    protected void handleStart() {

        ValidateObjectLife();

        while (!preStartMonoBehaviourQueue.isEmpty()) {

            var mono = preStartMonoBehaviourQueue.poll();
            mono.start();

            try {
                ValidateObjectLife();
            } catch (Exception e) {
                return;
            }

        }

    }

    /**
     * Handle Update state. Should only be called within {@link GameObjectManager}.
     */
    protected void handleUpdate() {

        ValidateObjectLife();

        for (var mono : monoBehaviourSet) {

            mono.update();

            try {
                ValidateObjectLife();
            } catch (Exception e) {
                return;
            }

        }

    }

    /**
     * Handle Late Update state. Should only be called within {@link GameObjectManager}.
     */
    protected void handleLateUpdate() {

        ValidateObjectLife();

        for (var mono : monoBehaviourSet) {

            mono.lateUpdate();
            try {
                ValidateObjectLife();
            } catch (Exception e) {
                return;
            }

        }

    }

    /**
     * Create a game object. This object has a {@link Transform}.
     */
    protected GameObject() {
        name = DEFAULT_NAME;
        isActive = true;
        isDestroyed = false;
        childSet = new HashSet<>();
        transform = addComponent(Transform.class);
        layer = Layer.Default;
    }

    /**
     * Create a game object. This object has a {@link Transform}.
     *
     * @param name The name of the object.
     */
    protected GameObject(String name) {
        this.name = name;
        isActive = true;
        isDestroyed = false;
        childSet = new HashSet<>();
        transform = addComponent(Transform.class);
        layer = Layer.Default;
    }

    /**
     * Copy a game object.
     *
     * @param gameObject The copied game object.
     */
    protected GameObject(GameObject gameObject) {
        isActive = gameObject.isActive;
        isDestroyed = gameObject.isDestroyed;
        name = gameObject.name;
        childSet = new HashSet<>();
        for (var child : gameObject.childSet) {
            childSet.add(new GameObject(child));
        }
        transform = addComponent(Transform.class);
        layer = Layer.Default;
    }

    /**
     * Wipe clean this game object's data.
     */
    protected void destroyObject() {

        ValidateObjectLife();

        isDestroyed = true;

        for (var monoBehaviour : monoBehaviourSet) {
            monoBehaviour.destroyComponent();
        }

        monoBehaviourSet.clear();
        preStartMonoBehaviourQueue.clear();
        preAwakeMonoBehaviourQueue.clear();

        for (var child : childSet) {
            GameObjectManager.destroy(child);
        }
        childSet = null;

    }

    /**
     * Get a component of type {@linkplain T} from this game object.
     *
     * @param type Class type of {@linkplain T}. Use .class().
     * @param <T>  Component type, must derive from {@link MonoBehaviour}.
     * @return A component, or {@code null} if not found any.
     */
    public <T extends MonoBehaviour> T getComponent(Class<T> type) {

        ValidateObjectLife();

        for (var component : monoBehaviourSet) {

            if (type.isInstance(component)) {
                return type.cast(component);
            }

        }

        return null;

    }

    /**
     * Add a component of type {@linkplain T} to this game object.
     *
     * @param type Class type of {@linkplain T}. Use .class().
     * @param <T>  Component type, must derive from {@linkplain MonoBehaviour}.
     * @return A valid component. If the component already exists, return that version.
     */
    public <T extends MonoBehaviour> T addComponent(Class<T> type) {

        ValidateObjectLife();

        var comp = getComponent(type);
        if (comp == null) {
            try {
                comp = type.getDeclaredConstructor(GameObject.class).newInstance(this);
                preAwakeMonoBehaviourQueue.offer(comp);
                monoBehaviourSet.add(comp);

                if (comp instanceof Renderable hasNode && registeredSceneKey != null) {
                    SceneManager.addNodeToScene(hasNode.getNode(), registeredSceneKey);
                }
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("No such method");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Illegal access");
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Illegal argument");
            } catch (InstantiationException e) {
                throw new RuntimeException("InstantiationException");
            } catch (InvocationTargetException e) {
                throw new RuntimeException("InvocationTargetException");
            } catch (ExceptionInInitializerError e) {
                throw new RuntimeException("ExceptionInInitializerError");
            } catch (SecurityException e) {
                throw new RuntimeException("SecurityException");
            } catch (Exception e) {
                throw new RuntimeException("Cannot create component of type " + type, e);
            }
        }

        return comp;

    }

    /**
     * Validate this object life for accessing.
     *
     * @throws IllegalStateException if this object is destroyed.
     */
    private void ValidateObjectLife() {

        if (isDestroyed) {
            throw new IllegalStateException("You are trying to access a destroyed game object!");
        }

    }

    /**
     * Get all components attached to this GameObject.
     *
     * @return A copy of all components.
     */
    public HashSet<MonoBehaviour> getAllComponents() {
        ValidateObjectLife();
        return new HashSet<>(monoBehaviourSet); // return a copy to avoid external modification
    }

    /**
     * Get all children of this GameObject.
     *
     * @return A copy of all children GameObjects.
     */
    public HashSet<GameObject> getChildren() {
        ValidateObjectLife();
        return new HashSet<>(childSet); // return a copy
    }

    public void setRegisteredSceneKey(String registeredSceneKey) {
        this.registeredSceneKey = registeredSceneKey;
    }
    public String getRegisteredSceneKey() {
        return registeredSceneKey;
    }
}
