package game.GameObject.Border;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import utils.Vector2;

// TODO: Doc
public class Border extends MonoBehaviour {

    private BorderType borderType;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Border(GameObject owner) {
        super(owner);
        addComponent(BoxCollider.class).setLocalCenter(new Vector2(0, 0));
    }

    public void awake() {
        var boxCollider = getComponent(BoxCollider.class);
        assignColliderSize(boxCollider);
    }

    private void assignColliderSize(BoxCollider boxCollider) {
        switch (borderType) {
            case BorderLeft, BorderRight -> boxCollider.setLocalSize(new Vector2(1.0, 20000.0));
            case BorderTop, BorderBottom -> boxCollider.setLocalSize(new Vector2(20000.0, 1.0));
        }
    }
    
    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }
}
