package game.Voltraxis;

import org.*;
import utils.Time;
import utils.Vector2;

public class ElectricBall extends MonoBehaviour {

    private static final double MOVEMENT_SPEED = 12.423;
    private static final Vector2 BALL_SIZE = new Vector2(64.0, 64.0);

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

        var spriteRenderer = addComponent(SpriteRenderer.class);
        spriteRenderer.setImage(ImageAsset.ImageIndex.ElectricBall.getImage());
        spriteRenderer.setPivot(new Vector2(0.5, 0.5));

        direction = new Vector2();

    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {
        direction = null;
    }

    protected void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    protected void setDamage(int damage) {
        this.damage = damage;
    }

    public void update() {
        getTransform().translate(direction.normalize().multiply(MOVEMENT_SPEED * Time.deltaTime));
    }

}
