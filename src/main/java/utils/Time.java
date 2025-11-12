package utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;

/**
 * Utility for getting information on application time.
 */
public class Time {

    public final static class CoroutineID {
    }

    /**
     * Coroutine, meaning scheduled action.
     */
    private static class Coroutine {
        public Runnable action;
        public double tick;
        public double delay;
    }

    private static final double offset = System.nanoTime() / 1000000000.0;
    private static final HashMap<CoroutineID, Coroutine> coroutineMap = new HashMap<>();

    private static double timeScale = 1.0;
    private static double time = 0.0;
    private static double realTime = 0.0;
    private static double deltaTime = 0.0;
    private static double unscaledDeltaTime = 0.0;

    /**
     * Update time for this frame. Should only be
     * called within {@code main} once.
     */
    public static void update() {

        updateTime();
        updateCoroutines();

    }

    private static void updateTime() {

        var prev = realTime;
        realTime = System.nanoTime() / 1000000000.0 - offset;
        unscaledDeltaTime = realTime - prev;
        deltaTime = unscaledDeltaTime * timeScale;
        time += deltaTime;

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
     * @param delay  The time in seconds upon
     *               when the action will be executed.
     * @return A coroutine ID of the created coroutine. It
     * can be used via {@link #removeCoroutine} to
     * manually terminate the coroutine if needed.
     */
    public static CoroutineID addCoroutine(Runnable action, double delay) {
        var id = new CoroutineID();
        var coroutine = new Coroutine();
        coroutine.action = action;
        coroutine.delay = delay;
        coroutine.tick = time + delay;
        coroutineMap.put(id, coroutine);
        return id;
    }

    /**
     * Immediately remove the coroutine with the given ID.
     * <p>
     * This function safely ignore {@code null} input.
     * </p>
     *
     * @param id The id of the coroutine given when added with
     *           {@link #addCoroutine}.
     */
    public static void removeCoroutine(CoroutineID id) {
        coroutineMap.remove(id);
    }

    /**
     * Reset the given {@link Coroutine}.
     * <p>
     * Note that if this coroutine is extended using {@link #delayCoroutine}, the time
     * it takes to execute the action will be the new delay instead of the old one.
     * </p>
     *
     * @param id The {@link CoroutineID} of the coroutine to be reset.
     * @throws NoSuchElementException If the {@link Coroutine} with the corresponding
     *                                {@link CoroutineID} is not found
     */
    public static void resetCoroutine(CoroutineID id) {
        var coroutine = coroutineMap.get(id);
        if (coroutine != null) {
            coroutine.tick = time + coroutine.delay;
        } else {
            throw new NoSuchElementException("Coroutine not found!");
        }
    }

    /**
     * Delay the specified {@link Coroutine} by an extended amount.
     *
     * @param id    The {@link CoroutineID} of the coroutine to be delayed.
     * @param delay The delay in seconds.
     * @throws NoSuchElementException If the {@link Coroutine} with the corresponding
     *                                {@link CoroutineID} is not found
     */
    public static void delayCoroutine(CoroutineID id, double delay) {
        var coroutine = coroutineMap.get(id);
        if (coroutine != null) {
            coroutine.tick += delay;
            coroutine.delay += delay;
        } else {
            throw new NoSuchElementException("Coroutine not found!");
        }
    }

    /**
     * Get the remaining time in seconds of the specified {@link Coroutine}.
     *
     * @param id The {@link CoroutineID} on the coroutine to check.
     * @return The time in seconds until execution, or {@link Double#MIN_VALUE}
     * if the {@link Coroutine} with the corresponding {@link CoroutineID} is not
     * found
     */
    public static double getRemainingTime(CoroutineID id) {
        var coroutine = coroutineMap.get(id);
        if (coroutine != null) {
            return coroutine.tick - Time.getTime();
        }
        return Double.MIN_VALUE;
    }

    /**
     * Check if there is a {@link Coroutine} corresponding to the given {@link CoroutineID}.
     *
     * @param id The {@link CoroutineID} to check.
     * @return {@code true} if there is a valid {@link Coroutine}, otherwise {@code false}.
     */
    public static boolean hasCoroutine(CoroutineID id) {
        return id != null && coroutineMap.containsKey(id);
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

    /**
     * Scaling of game time. Determine how fast or slow time progresses.
     *
     * @return The scaling of game time.
     */
    public static double getTimeScale() {
        return timeScale;
    }

    /**
     * The game's current time in seconds from the start of the application.
     * This takes {@code timeScale} into consideration, so the game's time will
     * be slower or faster than {@link #realTime}.
     */
    public static double getTime() {
        return time;
    }

    /**
     * The real time in seconds from the start of the application.
     */
    public static double getRealTime() {
        return realTime;
    }


    /**
     * The time difference in seconds between this and last frame.
     */
    public static double getDeltaTime() {
        return deltaTime;
    }

    /**
     *
     * @return The delta time that is not affected by timescale.
     */
    public static double getUnscaledDeltaTime() {
        return unscaledDeltaTime;
    }

}