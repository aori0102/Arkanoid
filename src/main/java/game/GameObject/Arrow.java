package game.GameObject;

import game.BrickObj.Brick;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public class Arrow extends MonoBehaviour {

    // TODO: bruh it turns into a fucking green brick

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

}
