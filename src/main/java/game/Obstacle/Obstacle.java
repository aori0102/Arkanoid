package game.Obstacle;

import game.object.Ball;
import org.*;
import utils.Time;
import utils.Vector2;

/**
 * Base class of call obstacles appearing in game.
 */
public abstract class Obstacle extends MonoBehaviour {

    /**
     * Warning time before the obstacle appears.
     */
    private final float warningTime = 3f;

    /**
     * The event happens when the paddle collides with this.
     */
    public EventHandler<Void> onObstacleCollided = new EventHandler(this);

    protected BoxCollider collider;

    protected boolean isDestroyed = false;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Obstacle(GameObject owner) {
        super(owner);
    }

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

    public void update() {
    }

    protected void handleInteraction() {
        onObstacleCollided.invoke(this, null );
    }

    public BoxCollider getCollider() {
        return collider;
    }


    protected abstract void handleMovement();

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }
}
