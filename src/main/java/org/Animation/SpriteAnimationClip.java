package org.Animation;

import javafx.scene.image.Image;
import org.Rendering.ImageAsset;

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

    private Image spriteSheet;
    protected AnimationNode head;
    private AnimationNode tail;
    private AnimationNode currentFrame;
    private boolean isLoop;
    private ImageAsset.ImageIndex imageIndex;

    public SpriteAnimationClip() {
        spriteSheet = null;
        head = null;
        tail = null;
        currentFrame = null;
        isLoop = false;
        imageIndex = null;
    }

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
     * Clear this animation clip's data, including all of
     * its frame. Should only be called within {@link SpriteAnimator}.
     */
    protected void clearClip() {

        spriteSheet = null;
        currentFrame = null;

        var temp = head;
        while (temp != null) {
            var current = temp;
            temp = temp.next;
            current.next = null;
            current.frame = null;
        }
        head = null;

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

    /**
     * Get the sprite sheet bound to this animation clip.
     *
     * @return The sprite sheet bound to this animation clip.
     */
    protected Image getSpriteSheet() {
        return spriteSheet;
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
