package utils.UITween;

import javafx.scene.Node;
import org.GameObject.GameObject;
import utils.NodeUtils;
import utils.UITween.Ease;
import utils.UITween.TweenManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DOTween-like Tween system for animating GameObjects.
 * Supports movement, scale, rotation, fade, ease, delay, looping, and time scale control.
 */
public class Tween {

    // === Inner Class: represents each animated node ===
    private static class TargetState {
        Node node;
        double fromX, toX;
        double fromY, toY;
        double fromScaleX, toScaleX;
        double fromScaleY, toScaleY;
        double fromRotate, toRotate;
        double fromOpacity, toOpacity;
        boolean moveX, moveY, scale, rotate, fade;
    }

    // === Fields ===
    private final List<TargetState> targets = new ArrayList<>();

    private double duration = 1.0;
    private double elapsed = 0.0;
    private double delay = 0.0;

    private boolean playing = false;
    private boolean completed = false;
    private boolean loop = false;
    private boolean yoyo = false;
    private boolean ignoreTimeScale = false;

    private Ease ease = Ease.IN_OUT;

    private Runnable onStart;
    private Runnable onComplete;

    // === Constructor ===
    private Tween(GameObject gameObject) {
        for (Node node : NodeUtils.collectNodes(gameObject.getTransform())) {
            TargetState t = new TargetState();
            t.node = node;
            targets.add(t);
        }
    }

    /** Create a new tween for a given GameObject */
    public static Tween to(GameObject gameObject) {
        return new Tween(gameObject);
    }

    // === Property setters ===

    public Tween moveX(double toX, double duration) {
        this.duration = duration;
        for (TargetState t : targets) {
            t.fromX = t.node.getTranslateX();
            t.toX = toX;
            t.moveX = true;
        }
        return this;
    }

    public Tween moveY(double toY, double duration) {
        this.duration = duration;
        for (TargetState t : targets) {
            t.fromY = t.node.getTranslateY();
            t.toY = toY;
            t.moveY = true;
        }
        return this;
    }

    public Tween moveYBy(double offsetY, double duration) {
        this.duration = duration;
        for (TargetState t : targets) {
            t.fromY = t.node.getTranslateY();
            t.toY = t.fromY + offsetY;
            t.moveY = true;
        }
        return this;
    }


    public Tween scaleTo(double toScale, double duration) {
        this.duration = duration;
        for (TargetState t : targets) {
            t.fromScaleX = t.node.getScaleX();
            t.fromScaleY = t.node.getScaleY();
            t.toScaleX = toScale;
            t.toScaleY = toScale;
            t.scale = true;
        }
        return this;
    }

    public Tween rotateTo(double toRotate, double duration) {
        this.duration = duration;
        for (TargetState t : targets) {
            t.fromRotate = t.node.getRotate();
            t.toRotate = toRotate;
            t.rotate = true;
        }
        return this;
    }

    public Tween fadeTo(double toOpacity, double duration) {
        this.duration = duration;
        for (TargetState t : targets) {
            t.fromOpacity = t.node.getOpacity();
            t.toOpacity = toOpacity;
            t.fade = true;
        }
        return this;
    }

    public Tween ease(Ease ease) {
        this.ease = ease;
        return this;
    }

    public Tween setDelay(double delay) {
        this.delay = delay;
        return this;
    }

    public Tween setLoop(boolean loop, boolean yoyo) {
        this.loop = loop;
        this.yoyo = yoyo;
        return this;
    }

    public Tween ignoreTimeScale(boolean ignore) {
        this.ignoreTimeScale = ignore;
        return this;
    }

    public Tween onStart(Runnable action) {
        this.onStart = action;
        return this;
    }

    public Tween onComplete(Runnable action) {
        this.onComplete = action;
        return this;
    }

    // === Control ===

    public void play() {
        if (playing) return;
        playing = true;
        completed = false;
        elapsed = 0.0;
        if (onStart != null) onStart.run();
        TweenManager.addTween(this);
    }

    public void stop() {
        playing = false;
        completed = true;
    }

    public boolean isPlaying() { return playing; }
    public boolean isCompleted() { return completed; }
    public boolean isIgnoringTimeScale() { return ignoreTimeScale; }

    // === Update (called from TweenManager) ===
    public void update(double deltaTime) {
        if (!playing || completed) return;

        elapsed += deltaTime;

        // Handle delay
        if (elapsed < delay) return;

        double t = (elapsed - delay) / duration;
        if (t > 1.0) t = 1.0;

        double eased = ease.apply(t);

        for (TargetState s : targets) {
            if (s.moveX) s.node.setTranslateX(lerp(s.fromX, s.toX, eased));
            if (s.moveY) s.node.setTranslateY(lerp(s.fromY, s.toY, eased));
            if (s.scale) {
                s.node.setScaleX(lerp(s.fromScaleX, s.toScaleX, eased));
                s.node.setScaleY(lerp(s.fromScaleY, s.toScaleY, eased));
            }
            if (s.rotate) s.node.setRotate(lerp(s.fromRotate, s.toRotate, eased));
            if (s.fade) s.node.setOpacity(lerp(s.fromOpacity, s.toOpacity, eased));
        }

        // Handle completion or looping
        if (t >= 1.0) {
            if (loop) {
                elapsed = delay > 0 ? -delay : 0;
                if (yoyo) swapStartAndEnd();
            } else {
                playing = false;
                completed = true;
                if (onComplete != null) onComplete.run();
            }
        }
    }

    // === Helper functions ===

    private void swapStartAndEnd() {
        for (TargetState s : targets) {
            double tmp;
            tmp = s.fromX; s.fromX = s.toX; s.toX = tmp;
            tmp = s.fromY; s.fromY = s.toY; s.toY = tmp;
            tmp = s.fromScaleX; s.fromScaleX = s.toScaleX; s.toScaleX = tmp;
            tmp = s.fromScaleY; s.fromScaleY = s.toScaleY; s.toScaleY = tmp;
            tmp = s.fromRotate; s.fromRotate = s.toRotate; s.toRotate = tmp;
            tmp = s.fromOpacity; s.fromOpacity = s.toOpacity; s.toOpacity = tmp;
        }
    }

    private double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
}
