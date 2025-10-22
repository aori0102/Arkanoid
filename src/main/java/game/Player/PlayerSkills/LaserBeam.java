package game.Player.PlayerSkills;

import game.Voltraxis.Interface.IBossTarget;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;
import utils.Time;
import utils.Vector2;

public class LaserBeam extends Skill{

    private static final double LASER_SPEED = 1000;
    private static final int LASER_DAMAGE = 36;

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
    }

    public void handleMovement() {
        getTransform().translate(Vector2.up().multiply(LASER_SPEED * Time.deltaTime));
    }

    public void handleCollision(CollisionData collisionData) {
        var boss = collisionData.otherCollider.getComponent(IBossTarget.class);
        if (boss != null) {
            boss.takeDamage(LASER_DAMAGE);
            GameObjectManager.destroy(gameObject);
        }
    }

    @Override
    public void assignColliderInfo() {
        var collider = getComponent(BoxCollider.class);
        collider.setLocalCenter(new Vector2(0, 0));
        collider.setLocalSize(new Vector2(32, 256));

        collider.setOnCollisionEnterCallback(e -> {
            handleCollision(e);
        });
    }


}
