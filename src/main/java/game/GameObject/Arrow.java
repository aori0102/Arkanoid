package game.GameObject;

import game.Brick.Brick;
import org.GameObject;
import org.MonoBehaviour;
import org.SpriteRenderer;
import utils.Vector2;

public class Arrow extends MonoBehaviour {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */

    private SpriteRenderer spriteRenderer;

    public Arrow(GameObject owner) {
        super(owner);
        addComponent(SpriteRenderer.class);
    }

    public void awake() {
        spriteRenderer = getComponent(SpriteRenderer.class);
        spriteRenderer.setPivot(new Vector2(0, 0.5));
        gameObject.setActive(false);
        addComponent(Brick.class);
    }

    public void handleArrowDirection(double angle) {
        spriteRenderer.setImageRotation(-angle);
    }

    public void turnOn() {
        gameObject.setActive(true);
    }

    public void turnOff() {
        gameObject.setActive(false);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }
}
