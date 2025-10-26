package game.Obstacle.Object;

import game.Obstacle.ICanDamagePlayer;
import game.Obstacle.Index.Obstacle;
import game.Player.Player;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Rendering.SpriteRenderer;
import utils.Time;
import utils.Vector2;

public class Laser extends Obstacle implements ICanDamagePlayer {

    private static final double LASER_SPEED = 1000;
    private static final int LASER_DAMAGE = 10;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Laser(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        collider = getComponent(BoxCollider.class);
        collider.setLocalCenter(new Vector2(0, 0));
        collider.setLocalSize(new Vector2(30, 90));
        collider.setIncludeLayer(Layer.Paddle.getUnderlyingValue());

        collider.setOnCollisionEnterCallback(this::handleInteraction);

    }

    @Override
    public void update() {
        super.update();
        handleMovement();
    }

    @Override
    protected void handleMovement() {
        getTransform().translate(Vector2.down().multiply(LASER_SPEED * Time.deltaTime));
    }

    @Override
    public void damagePlayer() {
        Player.getInstance().getPlayerHealth().damage(LASER_DAMAGE);
    }

}

