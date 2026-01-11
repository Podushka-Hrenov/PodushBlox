package telegram.bot.graphics.math;

import static java.lang.Math.sqrt;

public class Vec2 {
    public final float x, y;

    public Vec2(float x, float y) {
        this.x = x; this.y = y;
    }

    public float dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    public float magnitude() {
        return (float)sqrt(dot(this));
    }

    public Vec2 unit() {
        return divn(magnitude());
    }

    public Vec2 lerp(Vec2 goal, float t) {
        return add(goal.sub(this).muln(t));
    }

    public Vec2 negative() {
        return new Vec2(-x, -y);
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 sub(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public Vec2 mul(Vec2 other) {
        return new Vec2(x * other.x, y * other.y);
    }

    public Vec2 div(Vec2 other) {
        return new Vec2(x / other.x, y / other.y);
    }

    public Vec2 muln(float num) {
        return new Vec2(x * num, y * num);
    }

    public Vec2 divn(float num) {
        return new Vec2(x / num, y / num);
    }

    @Override
    public String toString() {
        return String.format("Vec2 %f, %f", x, y);
    }

    public static final Vec2 zero = new Vec2(0, 0);
    public static final Vec2 one = new Vec2(1, 1);
    public static final Vec2 xAxis = new Vec2(1, 0);
    public static final Vec2 yAxis = new Vec2(0, 1);
}