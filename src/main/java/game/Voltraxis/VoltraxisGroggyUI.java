package game.Voltraxis;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.MathUtils;
import utils.Time;

/**
 * UI class for groggy, controlled by {@link VoltraxisGroggy}.
 */
public class VoltraxisGroggyUI extends MonoBehaviour {

    private static final double GROGGY_BAR_CHANGE_RATE = 5.962;

    private SpriteRenderer fillRenderer = null;
    private double ratio = 0.0;
    private double targetRatio = 0.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisGroggyUI(GameObject owner) {
        super(owner);
    }

    @Override
    protected void destroyComponent() {
        fillRenderer = null;
    }

    @Override
    public void update() {
        ratio = MathUtils.lerp(ratio, targetRatio, Time.deltaTime * GROGGY_BAR_CHANGE_RATE);
        fillRenderer.setFillAmount(ratio);
    }

    /**
     * Attach the fill bar renderer for the groggy gauge UI.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link VoltraxisPrefab}
     * as a component linking process.</i></b>
     *
     * @param fill The fill bar renderer to set.
     */
    protected void attachFillRenderer(SpriteRenderer fill) {
        this.fillRenderer = fill;
    }

    /**
     * Set the target groggy ratio for groggy gauge UI update.<br><br>
     * <b><i><u>NOTE</u> : Only call within {@link VoltraxisGroggy}.</i></b>
     *
     * @param targetRatio The target ratio of groggy.
     */
    public void setTargetRatio(double targetRatio) {
        this.targetRatio = targetRatio;
    }

}
