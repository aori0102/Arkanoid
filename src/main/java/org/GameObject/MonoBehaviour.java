package org.GameObject;

/**
 * Base class of every game component. Each component is
 * derived through this class and is responsible for defining
 * a {@link GameObject}'s attribute.
 */
public abstract class MonoBehaviour {

    /**
     * The {@link GameObject} this component is attached to.
     */
    protected final GameObject gameObject;

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
     * Wipe clean this MonoBehaviours data.<br><br>
     * This function is called at the end of each frame when a
     * game object is destroyed (after the entire frame cycle)
     */
    protected abstract void destroyComponent();

    /**
     * Called first right after this object is instantiated
     * and is active.
     */
    public void awake() {
    }

    /**
     * Called at the first frame of update.
     */
    public void start() {
    }

    /**
     * Called every frame after {@link #start}.
     */
    public void update() {
    }

    /**
     * Called late every frame after {@link #update}.
     */
    public void lateUpdate() {
    }

    /**
     * Get the {@link GameObject} this {@link MonoBehaviour} is attached to.
     *
     * @return the game object this MonoBehaviour is attached to.
     */
    public GameObject getGameObject() {
        return gameObject;
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