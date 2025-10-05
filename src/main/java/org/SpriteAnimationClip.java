package org;

import javafx.scene.image.Image;
import utils.Vector2;

public class SpriteAnimationClip {

    protected static class AnimationNode {

        /**
         * The left most point of the sprite cut.
         */
        public Vector2 clipAnchor = null;

        /**
         * The size of the sprite cut.
         */
        public Vector2 clipSize = null;

        /**
         * The duration to show this sprite cut.
         */
        public double duration = 0.0;

        /**
         * The next node in the list.
         */
        public AnimationNode next = null;

        /**
         * The sprite image of the animation.
         */
        public Image image = null;

        /**
         * The angle of rotation in degrees.
         */
        public double rotationAngle = 0.0;

        /**
         * The size of the clip to be rendered.
         */
        public Vector2 renderSize = null;

    }

    private Image image;
    protected AnimationNode head;
    private AnimationNode tail;
    private AnimationNode currentFrame;
    private boolean isLoop;
    private String imagePath;

    protected SpriteAnimationClip() {
        image = null;
        head = null;
        tail = null;
        currentFrame = null;
        isLoop = false;
        imagePath = null;
    }

    /**
     * Add a frame to this animation clip's sequence. Should
     * only be called within {@link SpriteAnimator}.
     *
     * @param clipAnchor    The anchor point of the clip (top left).
     * @param clipSize      The size from {@code clipAnchor} of the clip.
     * @param renderSize    The size to render the clip.
     * @param duration      The duration of the frame.
     * @param rotationAngle The angle of rotation in degrees.
     */
    protected void addFrame(Vector2 clipAnchor, Vector2 clipSize, Vector2 renderSize, double duration, double rotationAngle) {

        // Create node
        var animationNode = new AnimationNode();
        animationNode.clipAnchor = clipAnchor;
        animationNode.clipSize = clipSize;
        animationNode.duration = duration;
        animationNode.image = image;
        animationNode.renderSize = renderSize;
        animationNode.rotationAngle = rotationAngle;

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
     * @param path The relative path to the sprite sheet.
     *             Sprite sheet should be inside {@code resources}
     *             folder, and {@code path} should
     *             begin with a {@code /}. e.g. {@code "/image.png"}.
     */
    protected void setSpriteSheet(String path) {

        try {
            // Get the resource folder
            java.net.URL url = getClass().getResource(path);
            if (url == null) {
                throw new Exception("Path stream is null");
            }
            // Set image and sprite
            String actualPath = url.toExternalForm();
            image = new Image(actualPath);
            imagePath = actualPath;
        } catch (Exception e) {
            throw new RuntimeException("Image not found: " + e);
        }

    }

    /**
     * Clear this animation clip's data, including all of
     * its frame. Should only be called within {@link SpriteAnimator}.
     */
    protected void clearClip() {

        image = null;
        currentFrame = null;

        var temp = head;
        while (temp != null) {
            var current = temp;
            temp = temp.next;
            current.next = null;
            current.clipAnchor = null;
            current.clipSize = null;
            current.duration = 0;
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

}
