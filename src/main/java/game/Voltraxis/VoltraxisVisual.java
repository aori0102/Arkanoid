package game.Voltraxis;

import org.*;
import utils.Vector2;

public final class VoltraxisVisual extends MonoBehaviour {

    private Voltraxis voltraxis = null;
    private SpriteAnimator animator = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisVisual(GameObject owner) {
        super(owner);

        animator = addComponent(SpriteAnimator.class);
        animator.addAnimationClip(AnimationClipData.Voltraxis_Idle);
        var renderer = addComponent(SpriteRenderer.class);
        renderer.setPivot(new Vector2(0.5, 0.5));
        renderer.setSize(VoltraxisData.BOSS_SIZE);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    public void awake() {
        animator.playAnimation(AnimationClipData.Voltraxis_Idle);
    }

    @Override
    protected void destroyComponent() {
        voltraxis = null;
        animator = null;
    }

    void setVoltraxis(Voltraxis voltraxis) {
        this.voltraxis = voltraxis;
    }

}