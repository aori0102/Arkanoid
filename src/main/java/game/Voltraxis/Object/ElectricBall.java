package game.Voltraxis.Object;

import game.Player.PlayerPaddle;
import game.Obstacle.ICanDamagePlayer;
import game.Player.Player;
import game.Voltraxis.Voltraxis;
import game.Voltraxis.VoltraxisData;
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
        Time.addCoroutine(this::onBallLifespanReached, Time.getTime() + BALL_LIFESPAN);
    }

    @Override
    public void update() {
        getTransform().translate(direction.normalize().multiply(MOVEMENT_SPEED * Time.getDeltaTime()));
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
    public int getDamage() {
        return (int) (VoltraxisData.ELECTRIC_BALL_ATTACK_PROPORTION * Voltraxis.getInstance().getVoltraxisStatManager().getAttack());
    }

    @Override
    public void onDamagedPlayer() {
        GameObjectManager.destroy(gameObject);
    }

}
