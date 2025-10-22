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
    public void playAnimation(AnimationClipData clipKey) {

        if (!animationClipMap.containsKey(clipKey)) {
            throw new RuntimeException("Animation clip doesn't exist. Use addAnimationClip() to create an animation clip");
        }

        if (currentFrameCoroutineID != null) {
            Time.removeCoroutine(currentFrameCoroutineID);
            currentFrameCoroutineID = null;
        }

        var clip = animationClipMap.get(clipKey);
        currentAnimationNode = clip.head;
        if (currentAnimationNode == null) {
            System.err.println(SpriteAnimator.class.getSimpleName() + " | Animation clip of key \"" + clipKey + "\" is empty!");
        }

        spriteRenderer.setImage(clip.getSpriteSheet());
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
            spriteRenderer.setSize(currentAnimationNode.frame.getRenderSize());
            spriteRenderer.setImageRotation(currentAnimationNode.frame.getRotationAngle());
            currentFrameCoroutineID = Time.addCoroutine(this::progressFrame, Time.time + currentAnimationNode.frame.getDuration());
        } else {
            currentFrameCoroutineID = null;
        }
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
