package telegram.bot.globals;

import static java.lang.Math.sqrt;

public class Vec2 {
    public final float x, y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float dot(Vec2 other) {
        return this.x * other.x + this.y * other.y;
    }

    public double magnitude() {
        return sqrt(this.x * this.x + this.y * this.y);
    }

    public Vec2 unit() {
        return this.divn((float) this.magnitude());
    }

    public Vec2 lerp(Vec2 goal, float t) {
        return this.add(goal.sub(this).muln(t));
    }

    public Vec2 negative() {
        return new Vec2(-this.x, -this.y);
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    public Vec2 sub(Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }

    public Vec2 mul(Vec2 other) {
        return new Vec2(this.x * other.x, this.y * other.y);
    }

    public Vec2 div(Vec2 other) {
        return new Vec2(this.x / other.x, this.y / other.y);
    }

    public Vec2 muln(float num) {
        return new Vec2(this.x * num, this.y * num);
    }

    public Vec2 divn(float num) {
        return new Vec2(this.x / num, this.y / num);
    }

    @Override
    public String toString() {
        return String.format("Vec2 %f, %f", this.x, this.y);
    }

    public static final Vec2 zero = new Vec2(0, 0);
    public static final Vec2 one = new Vec2(1, 1);
    public static final Vec2 xAxis = new Vec2(1, 0);
    public static final Vec2 yAxis = new Vec2(0, 1);
}