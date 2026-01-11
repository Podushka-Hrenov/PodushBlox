package telegram.bot.graphics.math;

import static java.lang.Math.sqrt;

public class Vec3 {
    public final float x, y, z;

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float dot(Vec3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public float magnitude() {
        return (float) sqrt(dot(this));
    }

    public Vec3 unit() {
        return divn(magnitude());
    }

    public Vec3 lerp(Vec3 goal, float t) {
        return add(goal.sub(this).muln(t));
    }

    public Vec3 cross(Vec3 other) {
        return new Vec3(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        );
    }

    public Vec3 negative() {
        return new Vec3(-x, -y, -z);
    }

    public Vec3 add(Vec3 other) {
        return new Vec3(x + other.x, y + other.y, z + other.z);
    }

    public Vec3 sub(Vec3 other) {
        return new Vec3(x - other.x, y - other.y, z - other.z);
    }

    public Vec3 mul(Vec3 other) {
        return new Vec3(x * other.x, y * other.y, z * other.z);
    }

    public Vec3 div(Vec3 other) {
        return new Vec3(x / other.x, y / other.y, z / other.z);
    }

    public Vec3 muln(float num) {
        return new Vec3(x * num, y * num, z * num);
    }

    public Vec3 divn(float num) {
        return new Vec3(x / num, y / num, z / num);
    }

    @Override
    public String toString() {
        return String.format("Vec3 %f, %f, %f", x, y, z);
    }

    public static final Vec3 zero = new Vec3(0, 0, 0);
    public static final Vec3 one = new Vec3(1, 1, 1);
    public static final Vec3 xAxis = new Vec3(1, 0, 0);
    public static final Vec3 yAxis = new Vec3(0, 1, 0);
    public static final Vec3 zAxis = new Vec3(0, 0, 1); 
}