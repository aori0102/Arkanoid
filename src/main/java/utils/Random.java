package utils;

import java.util.ArrayList;
import java.util.List;

public class Random {

    private static java.util.Random random;

    /**
     * Initialize the random machine. Called within {@code main}.
     */
    public static void init() {
        random = new java.util.Random(System.currentTimeMillis());
    }

    /**
     * Return a random integer within {@code [min, max)}.
     *
     * @param min The minimum value (inclusive).
     * @param max The maximum value (exclusive).
     * @return A random integer within {@code [min, max)}.
     */
    public static int range(int min, int max) {
        if (min == max) {
            return min;
        }
        return random.nextInt(min, max);
    }

    /**
     * Return a random decimal number within {@code [min, max)}.
     *
     * @param min The minimum value (inclusive).
     * @param max The maximum value (exclusive).
     * @return A random decimal number within {@code [min, max)}.
     */
    public static double range(double min, double max) {
        if (min == max) {
            return min;
        }
        return random.nextDouble(min, max);
    }

    /**
     * Shuffle the given list.
     *
     * @param input The list to shuffle.
     * @param <T>   List type.
     * @return A copied shuffled version of the input list.
     */
    public static <T> List<T> shuffle(List<T> input) {
        var output = new ArrayList<>(input);
        for (int i = output.size() - 1; i > 0; i--) {
            int j = range(0, i + 1);
            var temp = output.get(i);
            output.set(i, output.get(j));
            output.set(j, temp);
        }
        return output;
    }

}
