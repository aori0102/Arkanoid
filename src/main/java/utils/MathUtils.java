package utils;

public final class MathUtils {

    /**
     * Linear interpolate from point {@code a} to
     * point {@code b} at time point {@code t}.
     *
     * @param a The starting point of interpolation
     *          ({@code t = 0}).
     * @param b The ending point of interpolation
     *          ({@code t = 1}).
     * @param t The point of interpolation
     * @return The point of interpolation along {@code a}
     * to {@code b}.
     */
    public static double lerp(double a, double b, double t) {
        return (1 - t) * a + t * b;
    }

}
