package game.Voltraxis.Object;

import game.GameObject.Paddle;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Time;
import utils.Vector2;

public class ElectricBall extends MonoBehaviour {

    private static final double MOVEMENT_SPEED = 412.423;
    private static final Vector2 BALL_SIZE = new Vector2(64.0, 64.0);
    private static final double BALL_LIFESPAN = 12.0;

    private Vector2 direction;
    private int damage;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ElectricBall(GameObject owner) {

        super(owner);

        var boxCollider = addComponent(BoxCollider.class);
        boxCollider.setLocalSize(BALL_SIZE);
        boxCollider.setIncludeLayer(Layer.Player.getUnderlyingValue());
        boxCollider.setOnCollisionEnterCallback(this::onCollided);

        var spriteRenderer = addComponent(SpriteRenderer.class);
        spriteRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_ElectricBall.getImage());
        spriteRenderer.setSize(BALL_SIZE);
        spriteRenderer.setPivot(new Vector2(0.5, 0.5));

        Time.addCoroutine(this::onBallLifespanReached, Time.time + BALL_LIFESPAN);

        direction = new Vector2();

    }

    private void onBallLifespanReached() {
        GameObjectManager.destroy(gameObject);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new ElectricBall(newOwner);
    }

    private void onCollided(CollisionData data) {
        var paddle = data.otherCollider.getComponent(Paddle.class);
        if (paddle != null) {
            System.out.println("Damage " + paddle);
        }
    }

    @Override
    protected void destroyComponent() {
        direction = null;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public void update() {
        getTransform().translate(direction.normalize().multiply(MOVEMENT_SPEED * Time.deltaTime));
    }

}
