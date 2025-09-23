package utils;

public class Vector2 {

    public float x;
    public float y;

    public Vector2() {
        x = 0f;
        y = 0f;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void normalize() {

        float mag = magnitude();
        x /= mag;
        y /= mag;

    }

    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public static float dot(Vector2 a, Vector2 b) {
        return a.x * b.x + a.y * b.y;
    }

    public static float angle(Vector2 a, Vector2 b) {

        float dot = dot(a, b);
        float product = a.magnitude() * b.magnitude();

        if (product == 0f) {
            return 0f;
        }

        return (float) Math.acos(dot / product);

    }

    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 subtract(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    public Vector2 multiply(float other) {
        return new Vector2(x * other, y * other);
    }

    public Vector2 divide(float other) {
        return new Vector2(x / other, y / other);
    }

    public Vector2 scale(Vector2 other) {
        return new Vector2(x * other.x, y * other.y);
    }

}
