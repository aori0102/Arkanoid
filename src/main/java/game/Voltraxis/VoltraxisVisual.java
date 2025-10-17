package game.Voltraxis;

import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * Visual component of Voltraxis.
 */
public final class VoltraxisVisual extends MonoBehaviour {

    private Voltraxis voltraxis = null;
    private SpriteAnimator animator;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisVisual(GameObject owner) {
        super(owner);
        animator = getComponent(SpriteAnimator.class);
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

    /**
     * Set the central boss class {@link Voltraxis}.
     *
     * @param voltraxis The central boss class.
     */
    void setVoltraxis(Voltraxis voltraxis) {
        this.voltraxis = voltraxis;
    }

}