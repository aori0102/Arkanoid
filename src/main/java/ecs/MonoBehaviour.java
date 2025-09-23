package ecs;

public abstract class MonoBehaviour {

    protected GameObject gameObject = null;

    /**
     * Clone this MonoBehaviour.
     *
     * @param source The source MonoBehaviour.
     * @return The cloned version of this MonoBehaviour from source.
     */
    protected abstract MonoBehaviour Clone(MonoBehaviour source);

    /**
     * Wipe clean this MonoBehaviours data.
     */
    protected abstract void Clear();

    /**
     * Called when initializing object
     */
    public void Init() {
    }

    /**
     * Called when an object is instantiated and is active.
     */
    public void Awake() {
    }

    /**
     * Called in the first frame of update.
     */
    public void Start() {
    }

    /**
     * Called every frame.
     */
    public void Update() {
    }

    /**
     * Called late every frame after all Update().
     */
    public void LateUpdate() {
    }

    /**
     * Get the game object this MonoBehaviour is attached to.
     *
     * @return the game object this MonoBehaviour is attached to.
     */
    public GameObject GetGameObject() {
        return gameObject;
    }

    /**
     * Safely cast this MonoBehaviour to a specific type.
     *
     * @param type the type under MonoBehaviour.
     * @param <T>  the type under MonoBehaviour.
     * @return a valid class derive from MonoBehaviour, or {@code null} if error.
     */
    public <T extends MonoBehaviour> T As(Class<T> type) {
        try {
            return type.cast(this);
        } catch (ClassCastException e) {
            return null;
        }
    }

}