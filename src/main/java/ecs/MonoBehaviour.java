package ecs;

public abstract class MonoBehaviour {

    protected GameObject gameObject = null;

    /**
     * Clone this MonoBehaviour.
     *
     * @param source The source MonoBehaviour.
     * @return The cloned version of this MonoBehaviour from source.
     */
    protected abstract MonoBehaviour clone(MonoBehaviour source);

    /**
     * Wipe clean this MonoBehaviours data.
     */
    protected abstract void clear();

    /**
     * Called when initializing object
     */
    public void init() {
    }

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

}