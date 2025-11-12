package game.GameObject;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

/**
 * Class representing an Arrow visual object.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Control arrow visibility.</li>
 *     <li>Rotate arrow to indicate a direction.</li>
 * </ul>
 * </p>
 */
public class Arrow extends MonoBehaviour {

    private final SpriteRenderer spriteRenderer = addComponent(SpriteRenderer.class); // Arrow visual component

    /**
     * Constructor.
     *
     * @param owner The owner GameObject of this MonoBehaviour.
     */
    public Arrow(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        gameObject.setActive(false); // Arrow starts inactive
    }

    @Override
    public void update() {
        // Debug: print current pivot
        System.out.println(spriteRenderer.getPivot());
    }

    /**
     * Set the arrow rotation to match a given angle.
     *
     * @param angle The angle in degrees.
     */
    public void handleArrowDirection(double angle) {
        spriteRenderer.setImageRotation(-angle);
    }

    /**
     * Activate (show) the arrow.
     */
    public void turnOn() {
        gameObject.setActive(true);
    }

    /**
     * Deactivate (hide) the arrow.
     */
    public void turnOff() {
        gameObject.setActive(false);
    }

}
