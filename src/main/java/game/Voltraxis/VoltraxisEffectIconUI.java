package game.Voltraxis;

import game.Voltraxis.Prefab.VoltraxisPrefab;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

/**
 * Effect icon UI.
 */
public class VoltraxisEffectIconUI extends MonoBehaviour {

    private SpriteRenderer visualRenderer = null;

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

    /**
     * Initiate the pulse effect for this icon, making it
     * constantly fading and reappearing.
     */
    void setPulse() {

    }

    /**
     * Immediately set the local position for this icon.
     * Used by {@link VoltraxisEffectManager} when creating
     * an icon.
     *
     * @param position The immediate local position to set.
     */
    void setEntry(Vector2 position) {
        getTransform().setLocalPosition(position);
    }

    /**
     * Set the image renderer for the icon.<br><br>
     * <b><i><u>NOTE:</u> Only use within
     * {@link VoltraxisPrefab} as linking components.</i></b>
     *
     * @param renderer The image renderer for this icon.
     */
    public void setVisualRenderer(SpriteRenderer renderer) {
        visualRenderer = renderer;
    }

}
