package game.GameObject.Border;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import utils.Vector2;

/**
 * Represents a border collider in the game world.
 * <p>
 * Borders are invisible walls that prevent objects (like the player or ball)
 * from going outside the playable area. Each border has a {@link BorderType}
 * (top, bottom, left, or right) that determines its orientation and collider size.
 * </p>
 */
public class Border extends MonoBehaviour {

    private BorderType borderType;

    /**
     * Constructs a Border component and adds a {@link BoxCollider}.
     *
     * @param owner The GameObject that owns this component.
     */
    public Border(GameObject owner) {
        super(owner);
        addComponent(BoxCollider.class).setLocalCenter(new Vector2(0, 0));
    }

    /**
     * Called when the object awakens in the scene.
     * Initializes the collider size based on its border type.
     */
    @Override
    public void awake() {
        var boxCollider = getComponent(BoxCollider.class);
        assignColliderSize(boxCollider);
    }

    /**
     * Assigns a collider size appropriate for the border type.
     *
     * @param boxCollider The collider to configure.
     */
    private void assignColliderSize(BoxCollider boxCollider) {
        switch (borderType) {
            case BorderLeft, BorderRight ->
                    boxCollider.setLocalSize(new Vector2(1.0, 20000.0));
            case BorderTop, BorderBottom ->
                    boxCollider.setLocalSize(new Vector2(20000.0, 1.0));
        }
    }

    /**
     * Returns the current border type.
     *
     * @return The {@link BorderType} of this border.
     */
    public BorderType getBorderType() {
        return borderType;
    }

    /**
     * Sets the border type and updates its collider accordingly.
     *
     * @param borderType The {@link BorderType} to assign.
     */
    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }
}
