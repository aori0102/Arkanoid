package utils.UITween;

import utils.Time;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Global Tween manager.
 * Updates and manages all active tweens each frame.
 */
public final class TweenManager {

    private static final List<Tween> activeTweens = new ArrayList<>();

    private TweenManager() {} // prevent instantiation

    /** Add a tween to the update list */
    public static void addTween(Tween tween) {
        if (tween == null) return;
        activeTweens.add(tween);
    }

    /** Update all tweens — should be called every frame */
    public static void update() {
        if (activeTweens.isEmpty()) return;

        double delta = Time.getDeltaTime();
        double unscaledDelta = Time.getUnscaledDeltaTime();

        Iterator<Tween> iterator = activeTweens.iterator();
        while (iterator.hasNext()) {
            Tween tween = iterator.next();

            // Use the right delta depending on ignoreTimeScale
            double dt = tween.isIgnoringTimeScale() ? unscaledDelta : delta;
            tween.update(dt);

            // Remove finished tweens
            if (tween.isCompleted()) {
                iterator.remove();
            }
        }
    }

    /** Immediately stop and clear all tweens */
    public static void clearAll() {
        activeTweens.clear();
    }

    /** Returns the number of active tweens (for debugging) */
    public static int getActiveTweenCount() {
        return activeTweens.size();
    }
}

