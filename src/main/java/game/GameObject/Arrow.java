package game.GameObject;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

// TODO: Doc
public class Arrow extends MonoBehaviour {

    private final SpriteRenderer spriteRenderer=addComponent(SpriteRenderer.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Arrow(GameObject owner) {
        super(owner);
        spriteRenderer.setPivot(new Vector2(0, 0.5));
    }

    @Override
    public void awake() {
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