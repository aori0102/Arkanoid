package game.Voltraxis;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

/**
 * Effect icon UI.
 */
public class VoltraxisEffectIconUI extends MonoBehaviour {

    private static final Vector2 ICON_SIZE = new Vector2(30.58, 23.52);

    private final SpriteRenderer visualRenderer = addComponent(SpriteRenderer.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisEffectIconUI(GameObject owner) {
        super(owner);
    }

    /**
     * Set the target local position of this icon. Used by
     * {@link VoltraxisEffectManager} to control effect
     * position in grid. The icon will linearly move towards
     * this position.
     *
     * @param targetPosition The target local position.
     */
    void setTargetPosition(Vector2 targetPosition) {
        getTransform().setLocalPosition(targetPosition);
    }

    public void setEffectIndex(VoltraxisData.EffectIndex effectIndex) {
        visualRenderer.setImage(effectIndex.getImageIndex().getImage());
        visualRenderer.setSize(ICON_SIZE);
    }

}
