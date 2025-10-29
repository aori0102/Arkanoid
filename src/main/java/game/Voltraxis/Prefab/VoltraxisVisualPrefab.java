package game.Voltraxis.Prefab;

import game.Voltraxis.VoltraxisVisual;
import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;

/**
 * Prefab for Voltraxis' visual.
 */
public final class VoltraxisVisualPrefab implements IVoltraxisPrefab {

    @Override
    public GameObject instantiatePrefab() {

        var visualObject = GameObjectManager.instantiate("VoltraxisVisual");
        var visual = visualObject.addComponent(VoltraxisVisual.class);

        // Load animation
        var animator = visualObject.addComponent(SpriteAnimator.class);
        animator.addAnimationClip(AnimationClipData.Voltraxis_Idle);
        animator.addAnimationClip(AnimationClipData.Voltraxis_NormalAttack);
        animator.addAnimationClip(AnimationClipData.Voltraxis_Charging_Phase_1);
        animator.addAnimationClip(AnimationClipData.Voltraxis_Charging_Phase_2);
        animator.addAnimationClip(AnimationClipData.Voltraxis_Charging_Phase_3);
        animator.addAnimationClip(AnimationClipData.Voltraxis_Charging_Phase_4);
        animator.addAnimationClip(AnimationClipData.Voltraxis_Charging_Phase_5);
        animator.addAnimationClip(AnimationClipData.Voltraxis_Charging_Phase_6);
        animator.addAnimationClip(AnimationClipData.Voltraxis_Charging_FinishCharging);
        animator.addAnimationClip(AnimationClipData.Voltraxis_Charging_UnleashingLaser);
        animator.addAnimationClip(AnimationClipData.Voltraxis_Charging_EnterCharging);
        animator.addAnimationClip(AnimationClipData.Voltraxis_Charging_ExitUltimate);

        return visualObject;

    }

}