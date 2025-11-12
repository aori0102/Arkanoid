package game.LaserBeam;

import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
import game.Player.Player;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;
import utils.Time;
import utils.Vector2;

/**
 * Class representing a Laser Beam shot by the player.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Move upward automatically.</li>
 *     <li>Check collision with borders, bricks, and bosses.</li>
 *     <li>Destroy itself when hitting the top border.</li>
 * </ul>
 * </p>
 */
public class LaserBeam extends MonoBehaviour {

    private static final double LASER_SPEED = 1000;
    private final Layer[] colliderLayers = {Layer.Boss, Layer.Brick};
    public static EventHandler<Void> onAnyLaserBeamSpawned = new EventHandler<Void>(LaserBeam.class);
    /**
     * Constructor.
     *
     * @param owner The owner GameObject of this MonoBehaviour.
     */
    public LaserBeam(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        // Configure collider to only include specified layers
        getComponent(BoxCollider.class).setIncludeLayer(Layer.combineLayerMask(colliderLayers));
        // Set collision callback
        getComponent(BoxCollider.class).setOnCollisionEnterCallback(this::handleCollision);
    }

    @Override
    public void start() {
        // Spawn laser at the player's paddle position
        getTransform().setGlobalPosition(
                Player.getInstance().getPlayerPaddle().getTransform().getGlobalPosition()
        );
        onAnyLaserBeamSpawned.invoke(this, null);
    }

    @Override
    public void update() {
        handleMovement();
    }

    /**
     * Move the laser upward at constant speed.
     */
    public void handleMovement() {
        getTransform().translate(Vector2.up().multiply(LASER_SPEED * Time.getDeltaTime()));
    }

    /**
     * Handle collision with other objects.
     * Destroys laser if it hits the top border.
     *
     * @param collisionData The collision data.
     */
    public void handleCollision(CollisionData collisionData) {
        var border = collisionData.otherCollider.getComponent(Border.class);
        if (border != null && border.getBorderType() == BorderType.BorderTop) {
            GameObjectManager.destroy(gameObject);
        }
    }

}
