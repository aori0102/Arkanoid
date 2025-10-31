package game.Obstacle.Object;

import game.Damagable.DamageAcceptor;
import game.Damagable.DamageInfo;
import game.Damagable.DamageType;
import game.Damagable.ICanDealDamage;
import game.Effect.StatusEffect;
import game.Obstacle.Index.Obstacle;
import game.Player.PaddleDamageAcceptor;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import utils.Time;
import utils.Vector2;

public class Laser extends Obstacle implements ICanDealDamage {

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
        getTransform().translate(Vector2.down().multiply(LASER_SPEED * Time.getDeltaTime()));
    }

    @Override
    public DamageInfo getDamageInfo() {
        var damageInfo = new DamageInfo();
        damageInfo.amount = LASER_DAMAGE;
        damageInfo.type = DamageType.HitPlayer;
        return damageInfo;
    }

    @Override
    public StatusEffect getEffect() {
        return null;
    }

    @Override
    public void onDamaged() {
        GameObjectManager.destroy(gameObject);
    }

    @Override
    public boolean isDamageTarget(DamageAcceptor damageAcceptor) {
        return damageAcceptor instanceof PaddleDamageAcceptor;
    }

}