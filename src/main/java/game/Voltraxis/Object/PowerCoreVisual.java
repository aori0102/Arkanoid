package game.Voltraxis.Object;

import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public class PowerCoreVisual extends MonoBehaviour {

    private SpriteAnimator animator = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerCoreVisual(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        animator = getComponent(SpriteAnimator.class);
        animator.playAnimation(AnimationClipData.Voltraxis_PowerCore_Idle, null);
    }

    public void animateLowCharging() {
        animator.playAnimation(AnimationClipData.Voltraxis_PowerCore_Idle_ChargingLow, null);
    }

    public void animateMediumCharging() {
        animator.playAnimation(AnimationClipData.Voltraxis_PowerCore_Idle_ChargingMedium, null);
    }

    public void animateHighCharging() {
        animator.playAnimation(AnimationClipData.Voltraxis_PowerCore_Idle_ChargingHigh, null);
    }

}
