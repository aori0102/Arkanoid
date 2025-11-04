package game.Voltraxis.Object.UltimateLaser;

import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.MathUtils;
import utils.Time;

public class UltimateLaserVisual extends MonoBehaviour {

    private static final double LASER_APPEAR_RATE = 23.013;

    private final SpriteRenderer renderer = addComponent(SpriteRenderer.class);
    private final SpriteAnimator animator = addComponent(SpriteAnimator.class);
    private double ratio = 0.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public UltimateLaserVisual(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        renderer.setFillAmount(ratio);
        renderer.setFillType(SpriteRenderer.FillType.Vertical_TopToBottom);
        animator.playAnimation(AnimationClipData.Voltraxis_UltimateLaser, null);
    }

    @Override
    public void update() {
        ratio = MathUtils.lerp(ratio, 1.0, Time.getDeltaTime() * LASER_APPEAR_RATE);
        renderer.setFillAmount(ratio);
    }

}