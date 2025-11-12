package game.Obstacle.Index;

import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
import game.Player.Paddle.PlayerPaddle;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;

/**
 * Base class for all obstacles appearing in the game.
 * <p>
 * Responsibilities:
 * - Handle collision detection with the player paddle.
 * - Provide events for obstacle collision and destruction.
 * - Enforce a template for obstacle movement via {@link #handleMovement()}.
 * </p>
 */
public abstract class Obstacle extends MonoBehaviour {

    /**
     * Event triggered when the paddle collides with this obstacle.
     */
    public EventHandler<Void> onObstacleCollided = new EventHandler<>(Obstacle.class);

    /**
     * Event triggered when this obstacle is destroyed.
     */
    public EventHandler<Void> onObstacleDestroyed = new EventHandler<>(Obstacle.class);

    /**
     * The collider component used to detect interactions with paddle or borders.
     */
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
     * Initialize the collider and setup collision handling.
     * <p>
     * The collider will detect collisions with the Paddle layer and invoke {@link #onObstacleCollided}.
     * It will also destroy the obstacle if it collides with the bottom border.
     * </p>
     */
    @Override
    public void awake() {
        collider = getComponent(BoxCollider.class);
        collider.setIncludeLayer(Layer.Paddle.getUnderlyingValue());
        collider.setOnCollisionEnterCallback(this::handleInteraction);
    }

    /**
     * Handles collision interactions.
     *
     * @param collisionData The collision information.
     */
    protected void handleInteraction(CollisionData collisionData) {
        // Fire event for paddle collision
        onObstacleCollided.invoke(this, null);

        // Destroy if hits the bottom border
        if ((collisionData.otherCollider.getComponent(Border.class) != null
                && collisionData.otherCollider.getComponent(Border.class).getBorderType() == BorderType.BorderBottom)) {
            GameObjectManager.destroy(gameObject);
        }
    }

    /**
     * Template method for obstacle movement.
     * <p>
     * All subclasses must implement this to define their own movement behavior.
     * </p>
     */
    protected abstract void handleMovement();

    /**
     * Called when this obstacle is destroyed.
     * Fires the {@link #onObstacleDestroyed} event.
     */
    @Override
    protected void onDestroy() {
        onObstacleDestroyed.invoke(this, null);
    }

}
