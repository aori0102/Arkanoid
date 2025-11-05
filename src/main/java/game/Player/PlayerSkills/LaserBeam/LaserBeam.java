package game.Player.PlayerSkills.LaserBeam;

import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
import game.Player.Player;
import game.Player.PlayerSkills.Skill;
import game.Player.PlayerSkills.SkillIndex;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;
import utils.Time;
import utils.Vector2;

public class LaserBeam extends Skill {

    private static final double LASER_SPEED = 1000;
    private final Layer[] colliderLayers = {Layer.Boss, Layer.Brick};

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public LaserBeam(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        setSkillIndex(SkillIndex.LaserBeam);
        getComponent(BoxCollider.class).setIncludeLayer(Layer.combineLayerMask(colliderLayers));
        getComponent(BoxCollider.class).setOnCollisionEnterCallback(this::handleCollision);
    }

    @Override
    public void start() {
        getTransform().setGlobalPosition(
                Player.getInstance().getPlayerPaddle().getTransform().getGlobalPosition()
        );
    }

    @Override
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
    public void invoke() {
        //TODO: Add event
    }

}
