package game.Voltraxis;

import game.Voltraxis.Prefab.VoltraxisPrefab;
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
    public void awake() {
        Voltraxis.getInstance().getVoltraxisGroggy().onGroggyRatioChanged
                .addListener(this::voltraxisGroggy_onGroggyRatioChanged);
    }

    /**
     * Called when {@link VoltraxisGroggy#onGroggyRatioChanged} is invoked.<br><br>
     * This function updates {@link #ratio}, consequently updates the groggy bar visual UI as the groggy changes.
     *
     * @param sender Event caller {@link VoltraxisGroggy}.
     * @param e      Event argument represent the new ratio to set.
     */
    private void voltraxisGroggy_onGroggyRatioChanged(Object sender, Double e) {
        targetRatio = e;
    }

    @Override
    public void update() {
        ratio = MathUtils.lerp(ratio, targetRatio, Time.getDeltaTime() * GROGGY_BAR_CHANGE_RATE);
        fillRenderer.setFillAmount(ratio);
    }

    /**
     * Attach the fill bar renderer for the groggy gauge UI.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link VoltraxisPrefab}
     * as a component linking process.</i></b>
     *
     * @param fill The fill bar renderer to set.
     */
    public void attachFillRenderer(SpriteRenderer fill) {
        this.fillRenderer = fill;
    }

}