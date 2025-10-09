package utils;

public class Vector2 {

    /**
     * Vector with its {@code x} and {@code y} components being 0.
     *
     * @return Vector zero.
     */
    public static Vector2 zero() {
        return new Vector2(0.0, 0.0);
    }

    /**
     * Vector with its {@code x} and {@code y} components being 1.
     *
     * @return Vector one.
     */
    public static Vector2 one() {
        return new Vector2(1.0, 1.0);
    }

    /**
     * Vector representing the global up, shorthand for {@code Vector2(0.0, -1.0)}.
     *
     * @return Vector up.
     */
    public static Vector2 up() {
        return new Vector2(0.0, -1.0);
    }

    /**
     * Vector representing the global down, shorthand for {@code Vector2(0.0, 1.0)}.
     *
     * @return Vector down.
     */
    public static Vector2 down() {
        return new Vector2(0.0, 1.0);
    }

    /**
     * Vector representing the global left, shorthand for {@code Vector2(1.0, 0.0)}.
     *
     * @return Vector left.
     */
    public static Vector2 left() {
        return new Vector2(1.0, 0.0);
    }

    /**
     * Vector representing the global right, shorthand for {@code Vector2(-1.0, 0.0)}.
     *
     * @return Vector right
     */
    public static Vector2 right() {
        return new Vector2(-1.0, 0.0);
    }

    public double x;
    public double y;

    /**
     * Create a vector in 2D space, default value is (0, 0).
     */
    public Vector2() {
        x = 0.0;
        y = 0.0;
    }

    /**
     * Create a vector in 2D space.
     *
     * @param x The x component of the vector.
     * @param y The y component of the vector.
     */
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a vector in 2D space that is equal to {@code v}.
     *
     * @param v The vector to copy.
     */
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
        if (mag == 0.0) {
            return normalized;
        }
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
     * Multiply this vector with {@code other} component-wise.
     *
     * @param other The other vector.
     * @return Product of {@code this} and {@code other} component-wise.
     */
    public Vector2 scaleUp(Vector2 other) {
        return new Vector2(x * other.x, y * other.y);
    }

    /**
     * Divide this vector with {@code other} component-wise.
     *
     * @param other The other vector.
     * @return Quotient of {@code this} and {@code other} component-wise.
     */
    public Vector2 scaleDown(Vector2 other) {
        return new Vector2(x * other.x, y * other.y);
    }

    /**
     * Get the additive inverse of this vector, meaning its negative.
     *
     * @return The additive inverse of this vector.
     */
    public Vector2 inverse() {
        return new Vector2(-x, -y);
    }

    /**
     * Check if two vectors are equal.
     *
     * @param other The other vector to check.
     * @return {@code true} if two vectors are identical, otherwise {@code false}.
     */
    public boolean equals(Vector2 other) {
        return x == other.x && y == other.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}