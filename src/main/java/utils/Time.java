package utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class Time {

    /**
     * Coroutine, meaning scheduled action.
     *
     * @param action The action to be executed.
     * @param tick   The time to execute.
     */
    private record Coroutine(Runnable action, double tick) {
    }

    private static final double offset = System.nanoTime() / 1000000000.0;
    private static final Set<Coroutine> coroutineSet = new HashSet<>();

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
    public static void updateTime() {

        var prev = time;
        time = System.nanoTime() / 1000000000.0 - offset;
        deltaTime = time - prev;

        var currentCoroutineSet = new HashSet<>(coroutineSet);
        coroutineSet.clear();
        for (var coroutine : currentCoroutineSet) {
            if (time >= coroutine.tick) {
                coroutine.action.run();
            } else {
                coroutineSet.add(coroutine);
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
     */
    public static void addCoroutine(Runnable action, double tick) {
        coroutineSet.add(new Coroutine(action, tick));
    }

}
