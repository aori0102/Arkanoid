package game.object;

import org.*;

/**
 * Base class of call obstacles appearing in game.
 */
public class Obstacle extends MonoBehaviour {

    /**
     * Warning time before the obstacle appears.
     */
    private final float warningTime = 3f;

    /**
     * The event happens when the paddle collides with this.
     */
    public EventHandler onObstacleCollided = new EventHandler();

    private BoxCollider collider;

    /**
     * Assign the collider and fire the event when collide
     */
    public void awake() {
        collider = getComponent(BoxCollider.class);
        collider.setOnCollisionEnterCallback(e -> {
            if (e.otherCollider.getComponent(Ball.class) != null) {
                handleInteraction();
            }
        });
    }

    public void handleInteraction() {
        onObstacleCollided.invoke(this);
    }

    public BoxCollider getCollider() {
        return collider;
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }
}
