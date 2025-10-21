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

    /**
     * Convert angle from degree to radian.
     *
     * @param deg The angle in degree.
     * @return The angle in radian.
     */
    public static double deg2rad(double deg) {
        return Math.PI * deg / 180.0;
    }

    /**
     * Convert angle from radian to degree.
     *
     * @param rad The angle in radian.
     * @return The angle in degree.
     */
    public static double rad2deg(double rad) {
        return 180.0 * rad / Math.PI;
    }

}
