package game.Voltraxis.Object;

import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.MathUtils;
import utils.Time;

public class UltimateLaser extends MonoBehaviour {

    private static final double LASER_APPEAR_RATE = 23.013;

    private SpriteRenderer renderer = null;
    private double ratio = 0.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public UltimateLaser(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        getComponent(SpriteAnimator.class).playAnimation(AnimationClipData.Voltraxis_UltimateLaser, null);
        renderer = getComponent(SpriteRenderer.class);
        renderer.setFillAmount(0.0);
        renderer.setFillType(SpriteRenderer.FillType.Vertical_TopToBottom);
    }

    @Override
    public void update() {
        ratio = MathUtils.lerp(ratio, 1.0, Time.deltaTime * LASER_APPEAR_RATE);
        renderer.setFillAmount(ratio);
    }

}