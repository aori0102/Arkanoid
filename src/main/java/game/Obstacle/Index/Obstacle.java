package game.Obstacle.Index;

import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
import game.Player.PlayerPaddle;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;

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
        collider.setIncludeLayer(Layer.Paddle.getUnderlyingValue());
        collider.setOnCollisionEnterCallback(this::handleInteraction);
    }

    protected void handleInteraction(CollisionData collisionData) {
        onObstacleCollided.invoke(this, null);
        if (collisionData.otherCollider.getComponent(PlayerPaddle.class) != null
            || (collisionData.otherCollider.getComponent(Border.class) != null
               && collisionData.otherCollider.getComponent(Border.class).getBorderType() == BorderType.BorderBottom)) {
            GameObjectManager.destroy(gameObject);
        }
    }

    protected abstract void handleMovement();

    @Override
    protected void onDestroy() {
        onObstacleDestroyed.invoke(this, null);
    }

}