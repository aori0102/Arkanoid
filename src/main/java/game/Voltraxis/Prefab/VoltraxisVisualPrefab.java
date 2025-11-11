package game.Voltraxis.Prefab;

import game.Voltraxis.VoltraxisVisual;
import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.ParticleSystem.Emitter.EmitTypes;
import org.ParticleSystem.EmitterGenerator;
import org.ParticleSystem.ParticleType;
import org.Prefab.Prefab;
import utils.Vector2;

/**
 * Prefab for Voltraxis' visual.
 */
public final class VoltraxisVisualPrefab extends Prefab {

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
        animator.addAnimationClip(AnimationClipData.Voltraxis_Weakened);

        var smokeEmitter = EmitterGenerator.emitterHashMap.get(EmitTypes.Cone)
                .generateEmitter(27, 340, 599, 1.442, 2.323, 36.0);
        smokeEmitter.setParticleType(ParticleType.Smoke);
        smokeEmitter.setBaseDirection(Vector2.up());
        smokeEmitter.getGameObject().setParent(visualObject);

        visual.linkSmokeEmitter(smokeEmitter);

        return visualObject;

    }

}