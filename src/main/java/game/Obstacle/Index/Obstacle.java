package game.Obstacle.Index;

import game.GameObject.Ball;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;

/**
 * Base class of call obstacles appearing in game.
 */
public abstract class Obstacle extends MonoBehaviour {

    /**
     * The event happens when the paddle collides with this.
     */
    public EventHandler<Void> onObstacleCollided = new EventHandler<>(Obstacle.class);
    public EventHandler<Void> onObstacleDestroyed = new EventHandler<>(Obstacle.class);

    protected BoxCollider collider;

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
    @Override
    public void awake() {
        collider = getComponent(BoxCollider.class);
        collider.setOnCollisionEnterCallback(e -> {
            if (e.otherCollider.getComponent(Ball.class) != null) {
                handleInteraction();
            }
        });
    }

    protected void handleInteraction() {
        onObstacleCollided.invoke(this, null);
    }

    protected abstract void handleMovement();

    @Override
    protected void onDestroy() {
        onObstacleDestroyed.invoke(this, null);
    }

}