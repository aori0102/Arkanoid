package ecs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

public class GameObject {

    private static final String DEFAULT_NAME = "GameObject";

    private final Queue<MonoBehaviour> preAwakeMonoBehaviourQueue = new LinkedList<>();
    private final Queue<MonoBehaviour> preStartMonoBehaviourQueue = new LinkedList<>();
    private final HashSet<MonoBehaviour> monoBehaviourSet = new HashSet<>();

    private boolean isActive;

    public Transform transform;
    public String name;
    public Function<Void, Void> Initialize = null;

    /**
     * Return the activeness of this game object.
     *
     * @return {@code true} if this object is enabled, otherwise {@code false}.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Set the activeness of this game object.
     *
     * @param active Enable or disable.
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Handle Awake state. Should only be called within {@link GameObjectManager}.
     */
    protected void handleAwake() {

        while (!preAwakeMonoBehaviourQueue.isEmpty()) {

            var mono = preAwakeMonoBehaviourQueue.poll();
            mono.awake();
            preStartMonoBehaviourQueue.offer(mono);

        }

    }

    /**
     * Handle Start state. Should only be called within {@link GameObjectManager}.
     */
    protected void handleStart() {

        while (!preStartMonoBehaviourQueue.isEmpty()) {

            var mono = preStartMonoBehaviourQueue.poll();
            mono.start();

        }

    }

    /**
     * Handle Update state. Should only be called within {@link GameObjectManager}.
     */
    protected void handleUpdate() {

        for (var mono : monoBehaviourSet) {
            mono.update();
        }

    }

    /**
     * Handle Late Update state. Should only be called within {@link GameObjectManager}.
     */
    protected void handleLateUpdate() {

        for (var mono : monoBehaviourSet) {
            mono.lateUpdate();
        }

    }

    /**
     * Create a game object. This object has a {@link Transform}.
     */
    protected GameObject() {
        name = DEFAULT_NAME;
        addComponent(Transform.class);
    }

    /**
     * Create a game object. This object has a {@link Transform}.
     *
     * @param name The name of the object.
     */
    protected GameObject(String name) {
        this.name = name;
        addComponent(Transform.class);
    }

    /**
     * Copy a game object.
     *
     * @param gameObject The copied game object.
     */
    protected GameObject(GameObject gameObject) {
        this.name = gameObject.name;
        this.transform = gameObject.transform;
        this.isActive = gameObject.isActive;
    }

    /**
     * Wipe clean this game object's components.
     */
    protected void clearComponents() {

        for (var monoBehaviour : monoBehaviourSet) {
            monoBehaviour.clear();
        }

        monoBehaviourSet.clear();
        preStartMonoBehaviourQueue.clear();
        preAwakeMonoBehaviourQueue.clear();

    }

    /**
     * Get a component of type {@linkplain T} from this game object.
     *
     * @param type Class type of {@linkplain T}. Use .class().
     * @param <T>  Component type, must derive from {@link MonoBehaviour}.
     * @return A component, or {@code null} if not found any.
     */
    public <T extends MonoBehaviour> T getComponent(Class<T> type) {

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

        var comp = getComponent(type);
        if (comp == null) {
            try {
                comp = type.getDeclaredConstructor().newInstance();
                comp.gameObject = this;
                preAwakeMonoBehaviourQueue.offer(comp);
                monoBehaviourSet.add(comp);
            } catch (Exception e) {
                throw new RuntimeException("Cannot create component of type " + type, e);
            }
        }

        return comp;

    }

}
