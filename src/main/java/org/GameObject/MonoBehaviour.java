package org.GameObject;

public abstract class MonoBehaviour {

    protected transient GameObject gameObject = null;

    /**
     * Get the {@link Transform} for this game object.
     *
     * @return The {@link Transform} for this game object.
     */
    public Transform getTransform() {
        return gameObject.getTransform();
    }

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public MonoBehaviour(GameObject owner) {
        gameObject = owner;
    }

    /**
     * Wipe clean this MonoBehaviours data.
     */
    protected abstract void destroyComponent();

    /**
     * Called when an object is instantiated and is active.
     */
    public void awake() {
    }

    /**
     * Called in the first frame of update.
     */
    public void start() {
    }

    /**
     * Called every frame.
     */
    public void update() {
    }

    /**
     * Called late every frame after all Update().
     */
    public void lateUpdate() {
    }

    /**
     * Get the game object this MonoBehaviour is attached to.
     *
     * @return the game object this MonoBehaviour is attached to.
     */
    public GameObject getGameObject() {
        return gameObject;
    }

    /**
     * Safely cast this MonoBehaviour to a specific type.
     *
     * @param type the type under MonoBehaviour.
     * @param <T>  the type under MonoBehaviour.
     * @return a valid class derive from MonoBehaviour, or {@code null} if error.
     */
    public <T extends MonoBehaviour> T as(Class<T> type) {
        try {
            return type.cast(this);
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Get the component of type {@link T} of this object.
     *
     * @param type The type of the component.
     * @param <T>  The type of the component.
     * @return The component, or {@code null} if not found.
     */
    public <T> T getComponent(Class<T> type) {
        return gameObject.getComponent(type);
    }

    /**
     * Add component of type {@code type} to this game object.
     *
     * @param type The type of the component.
     * @param <T>  The type of the component.
     * @return The added component. If the game object already has a component
     * of type {@code type}, return that.
     */
    public <T extends MonoBehaviour> T addComponent(Class<T> type) {
        return gameObject.addComponent(type);
    }

}