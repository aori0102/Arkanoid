package game.Voltraxis.Object.ElectricBall;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import utils.Time;
import utils.Vector2;

/**
 * Voltraxis' electric ball that deals damage and stun the player.
 */
public class ElectricBall extends MonoBehaviour {

    private static final double MOVEMENT_SPEED = 412.423;
    private static final double BALL_LIFESPAN = 12.0;

    private Vector2 direction = new Vector2();

    private Time.CoroutineID onBallLifespanReached_coroutineID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ElectricBall(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        onBallLifespanReached_coroutineID
                = Time.addCoroutine(this::onBallLifespanReached, Time.getTime() + BALL_LIFESPAN);
    }

    @Override
    public void update() {
        getTransform().translate(direction.normalize().multiply(MOVEMENT_SPEED * Time.getDeltaTime()));
    }

    @Override
    public void onDestroy() {
        Time.removeCoroutine(onBallLifespanReached_coroutineID);
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

}
