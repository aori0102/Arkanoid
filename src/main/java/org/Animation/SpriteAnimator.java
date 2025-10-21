package org.Animation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.Time;
import utils.Vector2;


import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Objects;

public class SpriteAnimator extends MonoBehaviour {

    private static final Gson animationClipLoader = new GsonBuilder()
            .registerTypeAdapter(SpriteAnimationClip.class, new AnimationClipAdapter())
            .create();

    private SpriteRenderer spriteRenderer;
    private HashMap<AnimationClipData, SpriteAnimationClip> animationClipMap;
    private SpriteAnimationClip.AnimationNode currentAnimationNode;
    private Time.CoroutineID currentFrameCoroutineID = null;
    private Runnable currentAnimationFinishCallback = null;
    private Vector2 renderSize = null;
    private Vector2 pivot = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public SpriteAnimator(GameObject owner) {
        super(owner);

        animationClipMap = new HashMap<>();
        currentAnimationNode = null;
        spriteRenderer = addComponent(SpriteRenderer.class);
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
        System.out.println("Loading animation clip: " + clipKey);
        System.out.println("Path: " + clipKey.getAnimationClipDataPath());
        System.out.println("Resource: " + getClass().getResourceAsStream(clipKey.getAnimationClipDataPath()));

        try {
            Reader reader = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(clipKey.getAnimationClipDataPath())));
            var clip = animationClipLoader
                    .fromJson(reader, SpriteAnimationClip.class);
            animationClipMap.put(clipKey, clip);
        } catch (JsonSyntaxException e) {
            System.err.println(SpriteAnimator.class.getSimpleName() + " | Error while loading animation clip: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(SpriteAnimator.class.getSimpleName() + " | Unknown error while loading animation clip: " + e.getMessage());
        }

    }

    /**
     * Start playing an animation clip. This stops the current animation.
     *
     * @param clipKey The animation key to play.
     */
    public void playAnimation(AnimationClipData clipKey, Runnable onFinishedCallback) {

        if (!animationClipMap.containsKey(clipKey)) {
            throw new RuntimeException("Animation clip doesn't exist. Use addAnimationClip() to create an animation clip");
        }

        if (currentFrameCoroutineID != null) {
            Time.removeCoroutine(currentFrameCoroutineID);
            currentFrameCoroutineID = null;
        }

        currentAnimationFinishCallback = onFinishedCallback;

        var clip = animationClipMap.get(clipKey);
        currentAnimationNode = clip.head;
        if (currentAnimationNode == null) {
            System.err.println(SpriteAnimator.class.getSimpleName() + " | Animation clip of key \"" + clipKey + "\" is empty!");
        }

        spriteRenderer.setImage(clip.getSpriteSheet());
        if (renderSize != null) {
            spriteRenderer.setSize(renderSize);
        }
        if (pivot != null) {
            spriteRenderer.setPivot(pivot);
        }
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
            currentFrameCoroutineID = Time.addCoroutine(this::progressFrame, Time.time + currentAnimationNode.frame.getDuration());
        } else {
            currentFrameCoroutineID = null;
            if (currentAnimationFinishCallback != null) {
                currentAnimationFinishCallback.run();
                currentAnimationFinishCallback = null;
            }
        }
    }

    /**
     * Set the render size for every frame played in this animator.
     *
     * @param renderSize The render size to set.
     */
    public void setRenderSize(Vector2 renderSize) {
        this.renderSize = renderSize;
    }

    /**
     * Set the render pivot for every frame played in this animator.
     *
     * @param pivot The pivot to set
     */
    public void setPivot(Vector2 pivot) {
        this.pivot = pivot;
    }

    @Override
    protected void destroyComponent() {
        spriteRenderer = null;
        for (var key : animationClipMap.keySet()) {
            animationClipMap.get(key).clearClip();
        }
        animationClipMap.clear();
        animationClipMap = null;
        currentAnimationNode = null;
        if (currentFrameCoroutineID != null) {
            Time.removeCoroutine(currentFrameCoroutineID);
            currentAnimationNode = null;
        }
    }

}
