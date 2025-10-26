package org.GameObject;

import org.Event.EventActionID;
import org.Event.EventHandler;
import org.Layer.Layer;
import org.Scene.SceneKey;
import org.Scene.SceneManager;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Class that contains components ({@link MonoBehaviour}) that
 * makes up a game object.
 */
public class GameObject {

    private static final String DEFAULT_NAME = "GameObject";

    private final Queue<MonoBehaviour> preAwakeMonoBehaviourQueue = new LinkedList<>();
    private final Queue<MonoBehaviour> preStartMonoBehaviourQueue = new LinkedList<>();
    private final Queue<GameObject> childRemovalQueue = new LinkedList<>();
    private final Queue<GameObject> childAdditionQueue = new LinkedList<>();
    private final HashSet<MonoBehaviour> monoBehaviourSet = new HashSet<>();
    private final Transform transform;
    private final SceneKey registeredSceneKey;

    private boolean isActive = true;
    /**
     * This field should only be written to as {@code true} via
     * {@link #markDestroyed()}. <b>DO NOT</b> write to this field
     * anywhere else.
     */
    private boolean isDestroyed = false;
    private String name = DEFAULT_NAME;
    private Layer layer = Layer.Default;
    private EventActionID parentOnActivenessChangedActionID = null;
    private EventActionID parentOnParentChangedActionID = null;

    /**
     * Only access within {@link GameObject} or {@link GameObjectManager}.
     */
    protected LinkedHashSet<GameObject> childSet = new LinkedHashSet<>();
    private GameObject parent = null;

    public EventHandler<Void> onObjectActivenessChanged = new EventHandler<>(GameObject.class);
    public EventHandler<OnParentChangedEventArgs> onParentChanged = new EventHandler<>(GameObject.class);

    public static class OnParentChangedEventArgs {
        public GameObject previousParent;
        public GameObject newParent;
    }

    /**
     * Create an empty game object with a {@link Transform} attached.
     */
    protected GameObject() {
        transform = addComponent(Transform.class);
        registeredSceneKey = SceneManager.getCurrentSceneKey();
    }

    /**
     * Create an empty game object with a {@link Transform} attached.
     * Set its name as given.
     *
     * @param name The name of the object.
     */
    protected GameObject(String name) {
        this();
        this.name = name;
    }

    /**
     * Get the {@link Transform} component attached to this object.
     *
     * @return This object's transform.
     */
    public Transform getTransform() {
        validateObjectLife();
        return transform;
    }

    /**
     * Set the game object's layer
     *
     * @param layer The layer to set, must be single bit.
     * @throws IllegalArgumentException if the layer is invalid
     *                                  (either {@link Layer#None} or multi-bit)
     */
    public void setLayer(Layer layer) {

        validateObjectLife();

        var value = layer.getUnderlyingValue();
        if (value == 0) {
            throw new IllegalArgumentException("Layer cannot be " + Layer.None + ". Game object must belong to a layer");
        }
        if ((value & (value - 1)) != 0) {
            throw new IllegalArgumentException("Layer " + value + " must be single bit (A game object cannot be in two or more layers");
        }

        this.layer = layer;

    }

    /**
     * Get this game object's layer in bitmask
     *
     * @return The layer in bitmask.
     */
    public int getLayerMask() {
        validateObjectLife();
        return layer.getUnderlyingValue();
    }

    /**
     * Get this game object's name.
     *
     * @return The game object's name.
     */
    public String getName() {
        validateObjectLife();
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
        validateObjectLife();
        this.name = name;
    }

    /**
     * Add a child to this object's addition queue.
     * The child will be added at the end of the frame
     * via {@link #processChildSet()}.
     *
     * @param child The child to be added.
     */
    protected void addChild(GameObject child) {
        childAdditionQueue.offer(child);
    }

    /**
     * Add a child to this object's removal queue.
     * The child will be removed at the end of the
     * frame via {@link #processChildSet()}.
     *
     * @param child The child to be removed.
     */
    protected void removeChild(GameObject child) {
        childRemovalQueue.offer(child);
    }

    /**
     * Return the activeness of this game object.
     *
     * @return {@code true} if this object is enabled, otherwise {@code false}.
     */
    public boolean isActive() {

        validateObjectLife();

        boolean parentActive = parent == null || (!parent.isDestroyed() && parent.isActive());
        return isActive && parentActive;

    }

    /**
     * Set the activeness of this game object.
     *
     * @param active Enable or disable.
     */
    public void setActive(boolean active) {

        validateObjectLife();

        isActive = active;
        onObjectActivenessChanged.invoke(this, null);

    }

    /**
     * Get the object's current parent.
     *
     * @return The object's parent.
     */
    public GameObject getParent() {
        return parent;
    }

    /**
     * Handle Awake state.<br><br>
     * <b><i><u>NOTE</u> : Should only be called within
     * {@link GameObjectManager}.</i></b>
     */
    protected void handleAwake() {

        validateObjectLife();

        while (!preAwakeMonoBehaviourQueue.isEmpty()) {

            var mono = preAwakeMonoBehaviourQueue.poll();
            mono.awake();
            if (isDestroyed) {
                return;
            }
            preStartMonoBehaviourQueue.offer(mono);

        }

    }

    /**
     * Handle Start state.<br><br>
     * <b><i><u>NOTE</u> : Should only be called within
     * {@link GameObjectManager}.</i></b>
     */
    protected void handleStart() {

        validateObjectLife();

        while (!preStartMonoBehaviourQueue.isEmpty()) {

            var mono = preStartMonoBehaviourQueue.poll();
            mono.start();
            if (isDestroyed) {
                return;
            }

        }

    }

    /**
     * Handle Update state.<br><br>
     * <b><i><u>NOTE</u> : Should only be called within
     * {@link GameObjectManager}.</i></b>
     */
    protected void handleUpdate() {

        validateObjectLife();

        for (var mono : monoBehaviourSet) {

            mono.update();
            if (isDestroyed) {
                return;
            }

        }

    }

    /**
     * Handle Late Update state.<br><br>
     * <b><i><u>NOTE</u> : Should only be called within
     * {@link GameObjectManager}.</i></b>
     */
    protected void handleLateUpdate() {

        validateObjectLife();

        for (var mono : monoBehaviourSet) {

            mono.lateUpdate();
            if (isDestroyed) {
                return;
            }

        }

    }

    /**
     * Process child addition and removal of current frame.<br><br>
     * <b><i><u>NOTE</u> : Should only be called within
     * {@link GameObjectManager}.</i></b>
     */
    protected void processChildSet() {
        while (!childRemovalQueue.isEmpty()) {
            childSet.remove(childRemovalQueue.poll());
        }
        while (!childAdditionQueue.isEmpty()) {
            childSet.add(childAdditionQueue.poll());
        }
    }

    /**
     * Set a new parent for this object.<br><br>
     * <b><i><u>NOTE</u></i></b> : This object will become the specified new
     * parent's child on <b>THE NEXT FRAME</b>, along with the removal
     * of this object's old parent.
     *
     * @param parent The new parent to set.
     * @throws IllegalArgumentException if {@code parent} is invalid,
     *                                  i.e. {@code parent} is the same as this object.
     */
    public void setParent(GameObject parent) {

        if (parent == this) {
            throw new IllegalArgumentException(name + " cannot be its own parent!");
        }

        var eventArgs = new OnParentChangedEventArgs();
        eventArgs.previousParent = this.parent;
        eventArgs.newParent = parent;
        onParentChanged.invoke(this, eventArgs);

        if (this.parent != null) {
            this.parent.removeChild(this);
            this.parent.onParentChanged.removeListener(parentOnParentChangedActionID);
            this.parent.onObjectActivenessChanged.removeListener(parentOnActivenessChangedActionID);
        }

        this.parent = parent;

        if (parent != null) {
            parent.addChild(this);
            parentOnParentChangedActionID = parent.onParentChanged.addListener(this::gameObject_onParentChanged);
            parentOnActivenessChangedActionID = parent.onObjectActivenessChanged.addListener(this::gameObject_onObjectActivenessChanged);
        } else {
            parentOnActivenessChangedActionID = null;
            parentOnParentChangedActionID = null;
        }

    }

    /**
     * Mark this game object as destroyed.<br><br>
     * <b><i><u>NOTE</u> : Only call within {@link GameObjectManager}.</i></b>
     */
    protected void markDestroyed() {
        for (var mono : monoBehaviourSet) {
            mono.onDestroy();
        }
        isDestroyed = true;
    }

    /**
     * Wipe clean this game object's data, including its
     * subscribed events, children, parent and components.<br><br>
     * <b><i><u>NOTE</u> : Only call within {@link GameObjectManager}.</i></b>
     */
    protected void clearData() {

        // Clear components
        monoBehaviourSet.clear();
        preStartMonoBehaviourQueue.clear();
        preAwakeMonoBehaviourQueue.clear();

        // Clear children
        for (var child : childSet) {
            GameObjectManager.destroy(child);
        }
        childSet = null;

        // Remove from parent and unsubscribe from event
        if (parent != null) {
            parent.removeChild(this);
            parent.onParentChanged.removeListener(parentOnParentChangedActionID);
            parent.onObjectActivenessChanged.removeListener(parentOnActivenessChangedActionID);
        }

    }

    /**
     * Called when {@link GameObject#onObjectActivenessChanged} is invoked.<br><br>
     * This function calls {@link #onObjectActivenessChanged} due to changes in its
     * parent's activeness.
     */
    private void gameObject_onObjectActivenessChanged(Object sender, Void e) {
        onObjectActivenessChanged.invoke(this, null);
    }

    /**
     * Called when {@link GameObject#onParentChanged} is invoked.<br><br>
     * This function calls {@link #onParentChanged} as its family-related
     * attribute can change due to changes in its parent.
     */
    private void gameObject_onParentChanged(Object sender, OnParentChangedEventArgs e) {
        var eventArgs = new OnParentChangedEventArgs();
        eventArgs.previousParent = this.parent;
        eventArgs.newParent = this.parent;
        onParentChanged.invoke(this, eventArgs);
    }

    /**
     * Get a component of type {@link T} from this game object.
     *
     * @param type Class type of {@link T}. Use {@code .class}.
     * @param <T>  Component type, must derive from {@link MonoBehaviour}
     *             or an {@code interface}.
     * @return A component, or {@code null} if not found any.
     */
    public <T> T getComponent(Class<T> type) {

        validateObjectLife();

        if (!type.isInterface() && !MonoBehaviour.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Cannot get component. " + type + " is not MonoBehaviour nor an interface!");
        }

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
     * @param type Class type of {@linkplain T}. Use {@code .class}.
     * @param <T>  Component type, must derive from {@linkplain MonoBehaviour}.
     * @return The already exist component, or the new one otherwise.
     */
    public <T extends MonoBehaviour> T addComponent(Class<T> type) {

        validateObjectLife();

        var comp = getComponent(type);
        if (comp == null) {

            try {
                comp = type.getDeclaredConstructor(GameObject.class).newInstance(this);
                preAwakeMonoBehaviourQueue.offer(comp);
                monoBehaviourSet.add(comp);

            } catch (NoSuchMethodException e) {
                throw new RuntimeException("No such method: " + e.getMessage());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Illegal access: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Illegal argument: " + e.getMessage());
            } catch (InstantiationException e) {
                throw new RuntimeException("InstantiationException: " + e.getMessage());
            } catch (InvocationTargetException e) {
                throw new RuntimeException("InvocationTargetException: " + e.getTargetException());
            } catch (ExceptionInInitializerError e) {
                throw new RuntimeException("ExceptionInInitializerError: " + e.getMessage());
            } catch (SecurityException e) {
                throw new RuntimeException("SecurityException: " + e.getMessage());
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
    private void validateObjectLife() {
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
        validateObjectLife();
        return new HashSet<>(monoBehaviourSet); // return a copy to avoid external modification
    }

    /**
     * Get all children of this GameObject.
     *
     * @return A copy of all children GameObjects.
     */
    public HashSet<GameObject> getChildren() {
        validateObjectLife();
        return new HashSet<>(childSet); // return a copy
    }

    /**
     * Get the {@link SceneKey} of the scene this object belongs to.
     *
     * @return The scene key of the scene this object is in.
     */
    public SceneKey getRegisteredSceneKey() {
        return registeredSceneKey;
    }

    @Override
    public String toString() {
        return "GameObject[" + Objects.hashCode(this) + "] - " + name;
    }

}