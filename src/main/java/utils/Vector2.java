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

    public void Normalize() {

        float mag = Magnitude();
        x /= mag;
        y /= mag;

    }

    public float Magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public static float Dot(Vector2 a, Vector2 b) {
        return a.x * b.x + a.y * b.y;
    }

    public static float Angle(Vector2 a, Vector2 b) {

        float dot = Dot(a, b);
        float product = a.Magnitude() * b.Magnitude();

        if (product == 0f) {
            return 0f;
        }

        return (float) Math.acos(dot / product);

    }

    public Vector2 Add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 Subtract(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    public Vector2 Multiply(float other) {
        return new Vector2(x * other, y * other);
    }

    public Vector2 Divide(float other) {
        return new Vector2(x / other, y / other);
    }

    public Vector2 Scale(Vector2 other) {
        return new Vector2(x * other.x, y * other.y);
    }

}
