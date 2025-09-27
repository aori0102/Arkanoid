package org;

import utils.Time;
import utils.Vector2;


import java.util.HashMap;

public class SpriteAnimator extends MonoBehaviour {

    private SpriteRenderer spriteRenderer;
    private HashMap<String, SpriteAnimationClip> animationClipMap;
    private SpriteAnimationClip.AnimationNode currentAnimationClipFrame;
    private double lastFramePlayingTick;
    private double currentFrameDuration;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public SpriteAnimator(GameObject owner) {

        super(owner);

        animationClipMap = new HashMap<>();
        currentAnimationClipFrame = null;
        currentFrameDuration = 0.0;
        lastFramePlayingTick = 0.0;
        spriteRenderer = addComponent(SpriteRenderer.class);

    }

    /**
     * Set the loop for an animation clip.
     *
     * @param clipKey The animation key to set.
     * @param loop    Whether to loop this animation or not.
     */
    public void setLoop(String clipKey, boolean loop) {

        if (!animationClipMap.containsKey(clipKey)) {
            throw new RuntimeException("Animation clip doesn't exist");
        }

        animationClipMap.get(clipKey).setLoop(loop);

    }

    /**
     * Set the sprite sheet for an animation clip.
     *
     * @param clipKey The animation key to set.
     * @param path    The path pointing to the sprite sheet. The file
     *                should be within the {@code resources folder}, and
     *                the path begins with a {@code /}.
     */
    public void setSprite(String clipKey, String path) {

        if (!animationClipMap.containsKey(clipKey)) {
            animationClipMap.put(clipKey, new SpriteAnimationClip());
        }

        animationClipMap.get(clipKey).setSpriteSheet(path);

    }

    /**
     * Add an animation clip.
     *
     * @param clipKey The key of the animation clip.
     */
    public void addAnimationClip(String clipKey) {

        if (animationClipMap.containsKey(clipKey)) {
            throw new RuntimeException("Animation clip doesn't exist. Use addAnimationClip() to create an animation clip");
        }

        animationClipMap.put(clipKey, new SpriteAnimationClip());

    }

    /**
     * Add a frame to an animation clip.
     *
     * @param clipKey    The key of the animation to set.
     * @param clipAnchor The anchor point (top left) of the sprite clip.
     * @param clipSize   The size from the {@code clipAnchor} of the sprite clip.
     * @param renderSize The size to render the clip.
     * @param duration   The duration of the frame in seconds.
     */
    public void addFrame(String clipKey, Vector2 clipAnchor, Vector2 clipSize, Vector2 renderSize, double duration) {

        if (!animationClipMap.containsKey(clipKey)) {
            throw new RuntimeException("Animation clip doesn't exist. Use addAnimationClip() to create an animation clip");
        }

        animationClipMap.get(clipKey).addFrame(clipAnchor, clipSize, renderSize, duration);

    }

    /**
     * Start playing an animation clip. This stops the current animation.
     *
     * @param clipKey The animation key to play.
     */
    public void playAnimation(String clipKey) {

        if (!animationClipMap.containsKey(clipKey)) {
            throw new RuntimeException("Animation clip doesn't exist. Use addAnimationClip() to create an animation clip");
        }

        currentAnimationClipFrame = animationClipMap.get(clipKey).head;
        if (currentAnimationClipFrame == null) {
            throw new RuntimeException("Animation clip of key \"" + clipKey + "\" is empty!");
        }

        lastFramePlayingTick = Time.time;
        currentFrameDuration = currentAnimationClipFrame.duration;

    }

    /**
     * Stop the current animation.
     */
    public void stopAnimation() {
        currentAnimationClipFrame = null;
    }

    public void update() {

        if (currentAnimationClipFrame != null) {

            if (Time.time > lastFramePlayingTick + currentFrameDuration) {

                currentAnimationClipFrame = currentAnimationClipFrame.next;
                if (currentAnimationClipFrame != null) {
                    currentFrameDuration = currentAnimationClipFrame.duration;
                    spriteRenderer.overrideImage(currentAnimationClipFrame.image);
                    spriteRenderer.overrideClip(currentAnimationClipFrame.clipAnchor, currentAnimationClipFrame.clipSize);
                    spriteRenderer.overrideRenderSize(currentAnimationClipFrame.renderSize);
                    lastFramePlayingTick = Time.time;
                }

            }

        }

    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new SpriteAnimator(newOwner);
    }

    @Override
    protected void destroyComponent() {
        spriteRenderer = null;
        for (var key : animationClipMap.keySet()) {
            animationClipMap.get(key).clearClip();
        }
        animationClipMap.clear();
        animationClipMap = null;
        currentAnimationClipFrame = null;
        currentFrameDuration = 0.0;
        lastFramePlayingTick = 0.0;
    }

}
