package org.Animation;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.Time;

import java.util.HashMap;

/**
 * <b>Load and play animation on sprites.</b>
 * <p>
 * This component automatically requires a {@link SpriteRenderer}, and some {@link SpriteRenderer}'s
 * property will be overridden when an animation is playing. These include {@code clip},
 * {@code renderSize}, {@code rotation}, {@code pivot} and {@code image}.
 * </p>
 * <p>
 * Do note that the above properties can still be set within {@link SpriteRenderer} component, however
 * it requires to be set continuously to avoid being overridden by {@link SpriteAnimator}.
 * </p>
 * <p>
 * The timing within this system is based on {@link Time#getTime}, which is the game time and is affected
 * by {@link Time#getTimeScale}. This means that the animation playback speed can be faster or slower
 * based on the timescale.
 * </p>
 */
public class SpriteAnimator extends MonoBehaviour {

    private final SpriteRenderer spriteRenderer = addComponent(SpriteRenderer.class);
    private final HashMap<AnimationClipData, SpriteAnimationClip> animationClipMap = new HashMap<>();

    private SpriteAnimationClip.AnimationNode currentAnimationNode = null;

    private Time.CoroutineID progressFrame_coroutineID = null;
    private Runnable currentAnimationFinishCallback = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public SpriteAnimator(GameObject owner) {
        super(owner);
    }

    /**
     * Add an animation clip.
     *
     * @param clipKey The key of the animation clip.
     */
    public void addAnimationClip(AnimationClipData clipKey) {

        if (animationClipMap.containsKey(clipKey)) {
            throw new RuntimeException("Animation already exists");
        }

        animationClipMap.put(clipKey, clipKey.getAnimationClip());

    }

    /**
     * Start playing an animation clip and stops the current animation if there is any playing.
     *
     * @param clipKey The animation key to play.
     * @throws RuntimeException If there is no such animation clip bounds to {@code clipKey}.
     */
    public void playAnimation(AnimationClipData clipKey, Runnable onFinishedCallback) {

        if (!animationClipMap.containsKey(clipKey)) {
            throw new RuntimeException("Animation clip doesn't exist. Use addAnimationClip() to create an animation clip");
        }

        if (progressFrame_coroutineID != null) {
            Time.removeCoroutine(progressFrame_coroutineID);
            progressFrame_coroutineID = null;
        }

        currentAnimationFinishCallback = onFinishedCallback;

        var clip = animationClipMap.get(clipKey);
        currentAnimationNode = clip.head;
        if (currentAnimationNode == null) {
            System.err.println(SpriteAnimator.class.getSimpleName() + " | Animation clip of key \"" + clipKey + "\" is empty!");
        }

        spriteRenderer.setImage(clip.getSpriteSheet());
        spriteRenderer.setPivot(clip.getPivot());
        spriteRenderer.setSize(clip.getRenderSize());
        updateCurrentFrame();

    }

    /**
     * Stop the current animation.
     */
    public void stopAnimation() {
        currentAnimationNode = null;
    }

    /**
     * Progress to the next frame in the animation clip.
     */
    private void progressFrame() {
        if (currentAnimationNode != null) {
            currentAnimationNode = currentAnimationNode.next;
            updateCurrentFrame();
        }
    }

    /**
     * Render the current animation frame.
     */
    private void updateCurrentFrame() {
        if (currentAnimationNode != null) {
            spriteRenderer.setSpriteClip(
                    currentAnimationNode.frame.getClipAnchor(), currentAnimationNode.frame.getClipSize()
            );
            spriteRenderer.setImageRotation(currentAnimationNode.frame.getRotationAngle());
            progressFrame_coroutineID = Time.addCoroutine(this::progressFrame, Time.getTime() + currentAnimationNode.frame.getDuration());
        } else {
            progressFrame_coroutineID = null;
            if (currentAnimationFinishCallback != null) {
                currentAnimationFinishCallback.run();
                currentAnimationFinishCallback = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (progressFrame_coroutineID != null) {
            Time.removeCoroutine(progressFrame_coroutineID);
            currentAnimationNode = null;
        }
    }

}
