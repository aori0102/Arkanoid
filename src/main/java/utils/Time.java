package utils;

public class Time {

    private static final double offset = System.nanoTime() / 1000000000.0;

    /**
     * The current time in seconds from the start of the application.
     */
    public static double time = System.nanoTime();

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

    }

}
