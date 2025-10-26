package org.Animation;

import javafx.scene.image.Image;
import org.Rendering.ImageAsset;
import utils.Vector2;

import java.util.ArrayList;
import java.util.List;

public class SpriteAnimationClip {

    protected static class AnimationNode {

        /**
         * Frame info.
         */
        public AnimationFrame frame = null;

        /**
         * The next node in the list.
         */
        public AnimationNode next = null;

    }

    private Image spriteSheet = null;
    protected AnimationNode head = null;
    private AnimationNode tail = null;
    private AnimationNode currentFrame = null;
    private boolean isLoop = false;
    private ImageAsset.ImageIndex imageIndex = null;
    private Vector2 renderSize = Vector2.zero();
    private Vector2 pivot = Vector2.zero();

    /**
     * Add a frame to this animation clip's sequence. Should
     * only be called within {@link SpriteAnimator}.
     *
     * @param frame The frame to be added.
     */
    public void addFrame(AnimationFrame frame) {

        // Create node
        var animationNode = new AnimationNode();
        animationNode.frame = frame;

        // Insert into link list
        if (currentFrame == null) {
            head = animationNode;
        } else {
            currentFrame.next = animationNode;
        }
        currentFrame = animationNode;
        tail = animationNode;
        if (isLoop) {
            tail.next = head;
        }

    }

    /**
     * Fetch a sprite sheet with the provided path for
     * this animation clip. Will provide an exception
     * if fetching fails or the file cannot be found.
     * Should only be called within {@link SpriteAnimator}.
     *
     * @param imageIndex The index sprite sheet of the animation clip.
     */
    public void setSpriteSheet(ImageAsset.ImageIndex imageIndex) {
        this.imageIndex = imageIndex;
        spriteSheet = imageIndex.getImage();
    }

    /**
     * Set this clip's looping. Should only be
     * called within {@link SpriteAnimator}.
     *
     * @param loop Whether to loop the clip.
     */
    protected void setLoop(boolean loop) {

        isLoop = loop;
        if (tail != null) {

            if (loop) {
                tail.next = head;
            } else {
                tail.next = null;
            }

        }

    }

    // TODO: doc this script again since changed from animator to clip adapter
    /**
     * Get the sprite sheet bound to this animation clip.
     *
     * @return The sprite sheet bound to this animation clip.
     */
    protected Image getSpriteSheet() {
        return spriteSheet;
    }

    public void setRenderSize(Vector2 renderSize) {
        this.renderSize = renderSize;
    }

    public Vector2 getRenderSize() {
        return renderSize;
    }

    public Vector2 getPivot() {
        return pivot;
    }

    public void setPivot(Vector2 renderPivot) {
        this.pivot = renderPivot;
    }

    public ImageAsset.ImageIndex getImageIndex() {
        return imageIndex;
    }

    public List<AnimationFrame> getFrameList() {

        List<AnimationFrame> frameList = new ArrayList<>();
        var temp = head;
        while (temp != null) {
            frameList.add(temp.frame);
            temp = temp.next;
        }

        return frameList;

    }

    public boolean getLoop() {
        return isLoop;
    }

}
