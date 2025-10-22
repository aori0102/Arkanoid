package game.Voltraxis.Object;

import game.GameObject.Paddle;
import game.Obstacle.ICanDamagePlayer;
import game.Player.Player;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;
import utils.Time;
import utils.Vector2;

/**
 * Voltraxis' electric ball that deals damage and stun the player.
 */
public class ElectricBall extends MonoBehaviour implements ICanDamagePlayer {

    private static final double MOVEMENT_SPEED = 412.423;
    private static final double BALL_LIFESPAN = 12.0;

    private Vector2 direction = new Vector2();
    private int damage = 0;

    /**
     * Fired when this electric ball hits the {@link Paddle}.
     */
    public EventHandler<Void> onPaddleHit = new EventHandler<>(ElectricBall.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ElectricBall(GameObject owner) {
        super(owner);
        addComponent(BoxCollider.class).setOnCollisionEnterCallback(this::onCollided);
    }

    @Override
    public void awake() {
        Time.addCoroutine(this::onBallLifespanReached, Time.time + BALL_LIFESPAN);
    }

    @Override
    protected void destroyComponent() {
        direction = null;
    }

    @Override
    public void update() {
        getTransform().translate(direction.normalize().multiply(MOVEMENT_SPEED * Time.deltaTime));
    }

    /**
     * Handle collision with {@link Paddle} and deal damage
     * to {@link game.Player.Player} accordingly.
     *
     * @param data The collision data.
     */
    private void onCollided(CollisionData data) {
        var paddle = data.otherCollider.getComponent(Paddle.class);
        if (paddle != null) {
            onPaddleHit.invoke(this, null);
            GameObjectManager.destroy(gameObject);
        }
    }

    /**
     * Self-destruct when this electric ball reaches its maximum
     * life span.
     */
    private void onBallLifespanReached() {
        GameObjectManager.destroy(gameObject);
    }

    /**
     * Set this electric ball direction.
     *
     * @param direction The direction of this electric ball.
     */
    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    @Override
    public void damagePlayer() {
        Player.getInstance().damage(damage);
    }
}
