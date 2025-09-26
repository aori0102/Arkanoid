package utils;

public class Random {

    private static java.util.Random random;

    /**
     * Initialize the random machine. Called within {@code main}.
     */
    public static void init() {
        random = new java.util.Random(System.currentTimeMillis());
    }

    /**
     * Return a random integer within [{@code min}, {@code max}).
     *
     * @param min The minimum value (inclusive).
     * @param max The maximum value (exclusive).
     * @return A random integer within [{@code min}, {@code max}).
     */
    public static int Range(int min, int max) {
        return random.nextInt(min, max);
    }

    /**
     * Return a random decimal number within [{@code min}, {@code max}).
     *
     * @param min The minimum value (inclusive).
     * @param max The maximum value (exclusive).
     * @return A random decimal number within [{@code min}, {@code max}).
     */
    public static double Range(double min, double max) {
        return random.nextDouble(min, max);
    }

}
