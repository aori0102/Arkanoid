package game.Voltraxis;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public class VoltraxisEffectIcon extends MonoBehaviour {

    private SpriteRenderer visualRenderer = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisEffectIcon(GameObject owner) {
        super(owner);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {
        visualRenderer = null;
    }

    void setTargetPosition(Vector2 targetPosition) {
        getTransform().setLocalPosition(targetPosition);
    }

    void setPulse() {

    }

    void setEntry(Vector2 position) {
        getTransform().setLocalPosition(position);
    }

    void setVisualRenderer(SpriteRenderer renderer) {
        visualRenderer = renderer;
    }

}
