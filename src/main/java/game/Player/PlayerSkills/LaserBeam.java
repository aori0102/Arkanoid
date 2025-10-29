package game.Player.PlayerSkills;

import game.Brick.Brick;
import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
import game.Voltraxis.Interface.ITakePlayerDamage;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;
import utils.Time;
import utils.Vector2;

public class LaserBeam extends Skill{

    private static final double LASER_SPEED = 1000;
    private static final int LASER_DAMAGE = 36;
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

        var target = collisionData.otherCollider.getComponent(ITakePlayerDamage.class);
        if (target != null) {
            target.takeDamage(LASER_DAMAGE);
            GameObjectManager.destroy(gameObject);
            return;
        }

        var border = collisionData.otherCollider.getComponent(Border.class);
        if (border != null && border.getBorderType() == BorderType.BorderTop) {
            GameObjectManager.destroy(gameObject);
            return;
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


}
