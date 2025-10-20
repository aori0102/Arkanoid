package org.Animation;

import utils.Vector2;

/**
 * Utility class to provide frame information
 * for {@link SpriteAnimationClip}.
 */
public final class AnimationFrame {

    private Vector2 clipAnchor = new Vector2();
    private Vector2 clipSize = new Vector2();
    private double duration = 0.0;
    private double rotationAngle = 0.0;

    public Vector2 getClipAnchor() {
        return clipAnchor;
    }

    public void setClipAnchor(Vector2 clipAnchor) {
        this.clipAnchor = new Vector2(clipAnchor);
    }

    public Vector2 getClipSize() {
        return clipSize;
    }

    public void setClipSize(Vector2 clipSize) {
        this.clipSize = new Vector2(clipSize);
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

}