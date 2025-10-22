package utils;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Utility for getting information on application time.
 */
public class Time {

    public final static class CoroutineID {
    }

    /**
     * Coroutine, meaning scheduled action.
     *
     * @param action The action to be executed.
     * @param tick   The time to execute.
     */
    private record Coroutine(Runnable action, double tick) {
    }

    private static final double offset = System.nanoTime() / 1000000000.0;
    private static final HashMap<CoroutineID, Coroutine> coroutineMap = new HashMap<>();

    private static double timeScale = 1.0;

    // TODO: private plz
    /**
     * The current time in seconds from the start of the application.
     */
    public static double time = 0.0;

    /**
     * The time difference in seconds between this and last frame.
     */
    public static double deltaTime = 0.0;

    /**
     * Update time for this frame. Should only be
     * called within {@code main} once.
     */
    public static void update() {

        updateTime();
        updateCoroutines();

    }

    private static void updateTime() {

        var prev = time;
        time = System.nanoTime() / 1000000000.0 - offset;
        deltaTime = (time - prev) * timeScale;

    }

    private static void updateCoroutines() {

        var idSet = new HashSet<>(coroutineMap.keySet());
        for (var id : idSet) {
            var coroutine = coroutineMap.get(id);
            if (coroutine != null && time >= coroutine.tick) {
                coroutine.action.run();
                coroutineMap.remove(id);
            }
        }

    }

    /**
     * Add an action that will automatically execute
     * after a set amount of time.
     *
     * @param action The action to be executed.
     * @param tick   The time in seconds after upon
     *               when the action will be executed.
     * @return A coroutine ID of the created coroutine. It
     * can be used via {@link #removeCoroutine} to
     * manually terminate the coroutine if needed.
     */
    public static CoroutineID addCoroutine(Runnable action, double tick) {
        var id = new CoroutineID();
        var coroutine = new Coroutine(action, tick);
        coroutineMap.put(id, coroutine);
        return id;
    }

    /**
     * Immediately remove the coroutine with the given ID.
     *
     * @param id The id of the coroutine given when added with
     *           {@link #addCoroutine}.
     */
    public static void removeCoroutine(CoroutineID id) {
        coroutineMap.remove(id);
    }

    /**
     * Set the scaling of time of the game, meaning how fast time moves.
     *
     * @param timeScale The scale of time, i.g. {@code 1.0} is normal speed,
     *                  {@code 0.0} is frozen.
     */
    public static void setTimeScale(double timeScale) {
        Time.timeScale = timeScale;
    }

}