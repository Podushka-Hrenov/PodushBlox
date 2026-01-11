package telegram.bot.graphics.math;

import static java.lang.Math.sqrt;

public class Vec2Int {
    public final int x, y;

    public Vec2Int(int x, int y) {
        this.x = x; this.y = y;
    }

    public Vec2Int(float x, float y) {
        this.x = (int)x; this.y = (int)y;
    }

    public float dot(Vec2Int other) {
        return x * other.x + y * other.y;
    }

    public float magnitude() {
        return (float)sqrt(x*x + y*y);
    }

    public Vec2Int unit() {
        return divn(magnitude());
    }

    public Vec2Int lerp(Vec2Int goal, float t) {
        return add(goal.sub(this).muln(t));
    }

    public Vec2Int negative() {
        return new Vec2Int(-x, -y);
    }

    public Vec2Int add(Vec2Int other) {
        return new Vec2Int(x + other.x, y + other.y);
    }

    public Vec2Int sub(Vec2Int other) {
        return new Vec2Int(x - other.x, y - other.y);
    }

    public Vec2Int mul(Vec2Int other) {
        return new Vec2Int(x * other.x, y * other.y);
    }

    public Vec2Int div(Vec2Int other) {
        return new Vec2Int(x / other.x, y / other.y);
    }

    public Vec2Int muln(float num) {
        return new Vec2Int(x * num, y * num);
    }

    public Vec2Int divn(float num) {
        return new Vec2Int(x / num, y / num);
    }

    @Override
    public String toString() {
        return String.format("Vec2 %d, %d", x, y);
    }

    public static final Vec2Int zero = new Vec2Int(0, 0);
    public static final Vec2Int one = new Vec2Int(1, 1);
    public static final Vec2Int xAxis = new Vec2Int(1, 0);
    public static final Vec2Int yAxis = new Vec2Int(0, 1);
}