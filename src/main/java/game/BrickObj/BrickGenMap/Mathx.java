package game.BrickObj.BrickGenMap;

/**
 * Class Mathx help some problem relate to Math.
 */

public final class Mathx {
    private Mathx() {}

    /**
     * Keep that value is always between 0 and 1.
     * @param x that value
     */
    public static double keep01(double x) {
        return Math.max(0.0, Math.min(1.0, x)   );
    }

    public static double lerp(double a, double b, double t) {
        return a + (b - a) * keep01(t);
    }

    public static double map(double v, double a0, double a1, double b0, double b1) {
        if (a1 == a0) return b0;
        double t = (v - a0) / (a1 - a0);
        return b0 + (b1 - b0) * t;
    }
    public static int clampI(int v, int lo, int hi) { return Math.max(lo, Math.min(hi, v)); }
}
