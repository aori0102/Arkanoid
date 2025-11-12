package org.Animation;

import javafx.scene.image.Image;
import org.Rendering.ImageAsset;
import utils.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains data for animating on a sprite sheet. This is serialized and deserialized
 * under {@link AnimationClipAdapter}.
 */
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

    protected Image getSpriteSheet() {
        return spriteSheet;
    }

    public Vector2 getRenderSize() {
        return renderSize;
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

    public Vector2 getPivot() {
        return pivot;
    }

    public void setRenderSize(Vector2 renderSize) {
        this.renderSize = renderSize;
    }

    public void setPivot(Vector2 renderPivot) {
        this.pivot = renderPivot;
    }

    public void setLoop(boolean loop) {

        isLoop = loop;
        if (tail != null) {

            if (loop) {
                tail.next = head;
            } else {
                tail.next = null;
            }

        }

    }

}
