package utils;

public class Vector2 {

    public double x;
    public double y;

    public Vector2() {
        x = 0.0;
        y = 0.0;
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }


    /**
     * Get the normalized of this vector.
     *
     * @return This vector normalized.
     */
    public Vector2 normalize() {

        Vector2 normalized = new Vector2(this);
        double mag = magnitude();
        normalized.x /= mag;
        normalized.y /= mag;
        return normalized;

    }

    /**
     * Get this vector's magnitude.
     *
     * @return This vector's magnitude.
     */
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Get the dot product between two vectors.
     *
     * @param a First vector.
     * @param b Second vector.
     * @return The dot product of {@code a} and {@code b}.
     */
    public static double dot(Vector2 a, Vector2 b) {
        return a.x * b.x + a.y * b.y;
    }

    /**
     * Get the angle between two vectors.
     *
     * @param a First vector.
     * @param b Second vector
     * @return The angle in degrees between {@code a} and {@code b}.
     */
    public static double angle(Vector2 a, Vector2 b) {

        double dot = dot(a, b);
        double product = a.magnitude() * b.magnitude();

        if (product == 0f) {
            return 0f;
        }

        return Math.acos(dot / product);

    }

    /**
     * Add this vector with {@code other}.
     *
     * @param other The other vector.
     * @return Sum of {@code this} and {@code other}.
     */
    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    /**
     * Subtract this vector from {@code other}.
     *
     * @param other The other vector.
     * @return Difference between {@code this} and {@code other}.
     */
    public Vector2 subtract(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    /**
     * Multiply this vector with {@code other}.
     *
     * @param other The multiplication coefficient.
     * @return Product of {@code this} and {@code other}.
     */
    public Vector2 multiply(double other) {
        return new Vector2(x * other, y * other);
    }

    /**
     * Divide this vector with {@code other}.
     *
     * @param other The division coefficient.
     * @return Quotient of {@code this} and {@code other}.
     */
    public Vector2 divide(double other) {
        return new Vector2(x / other, y / other);
    }

    /**
     * Mutltiply this vector with {@code other} component-wise.
     *
     * @param other The other vector.
     * @return Product of {@code this} and {@code other} component-wise.
     */
    public Vector2 scale(Vector2 other) {
        return new Vector2(x * other.x, y * other.y);
    }

}
