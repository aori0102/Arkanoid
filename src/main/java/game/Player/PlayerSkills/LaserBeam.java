package game.Player.PlayerSkills;

import game.Brick.BrickDamageAcceptor;
import game.Damagable.DamageAcceptor;
import game.Damagable.DamageInfo;
import game.Damagable.DamageType;
import game.Damagable.ICanDealDamage;
import game.Effect.StatusEffect;
import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
import game.Voltraxis.Object.PowerCore.PowerCoreDamageAcceptor;
import game.Voltraxis.VoltraxisDamageAcceptor;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;
import utils.Time;
import utils.Vector2;

public class LaserBeam extends Skill implements ICanDealDamage {

    private static final double LASER_SPEED = 1000;
    private static final int LASER_DAMAGE = 500;
    private final Layer[] colliderLayers = {Layer.Boss, Layer.Brick};

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public LaserBeam(GameObject owner) {
        super(owner);
    }

    public void awake() {
        setSkillIndex(SkillIndex.LaserBeam);
        assignColliderInfo();
    }

    public void update() {
        handleMovement();
    }

    public void handleMovement() {
        getTransform().translate(Vector2.up().multiply(LASER_SPEED * Time.getDeltaTime()));
    }

    public void handleCollision(CollisionData collisionData) {

        var border = collisionData.otherCollider.getComponent(Border.class);
        if (border != null && border.getBorderType() == BorderType.BorderTop) {
            GameObjectManager.destroy(gameObject);
        }
    }

    @Override
    public void assignColliderInfo() {
        var collider = getComponent(BoxCollider.class);
        collider.setIncludeLayer(Layer.combineLayerMask(colliderLayers));
        collider.setLocalCenter(new Vector2(0, 0));
        collider.setLocalSize(new Vector2(32, 256));

        collider.setOnCollisionEnterCallback(this::handleCollision);
    }

    @Override
    public void invoke() {
        //TODO: Add event
    }


    @Override
    public DamageInfo getDamageInfo() {
        var damageInfo = new DamageInfo();
        damageInfo.amount = LASER_DAMAGE;
        damageInfo.type = DamageType.Normal;
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
        return damageAcceptor instanceof VoltraxisDamageAcceptor
                || damageAcceptor instanceof PowerCoreDamageAcceptor
                || damageAcceptor instanceof BrickDamageAcceptor;
    }

}
