package telegram.bot.graphics.math;

import static java.lang.Math.sqrt;

public class Vec3Int {
    public final int x, y, z;

    public Vec3Int(int x, int y, int z) {
        this.x = x; this.y = y; this.z = z;
    }

    public Vec3Int(float x, float y, float z) {
        this.x = (int)x; this.y = (int)y; this.z = (int)z;
    }

    public float dot(Vec3Int other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public float magnitude() {
        return (float) sqrt(dot(this));
    }

    public Vec3Int unit() {
        return divn(magnitude());
    }

    public Vec3Int lerp(Vec3Int goal, float t) {
        return add(goal.sub(this).muln(t));
    }

    public Vec3Int cross(Vec3Int other) {
        return new Vec3Int(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        );
    }

    public Vec3Int negative() {
        return new Vec3Int(-x, -y, -z);
    }

    public Vec3Int add(Vec3Int other) {
        return new Vec3Int(x + other.x, y + other.y, z + other.z);
    }

    public Vec3Int sub(Vec3Int other) {
        return new Vec3Int(x - other.x, y - other.y, z - other.z);
    }

    public Vec3Int mul(Vec3Int other) {
        return new Vec3Int(x * other.x, y * other.y, z * other.z);
    }

    public Vec3Int div(Vec3Int other) {
        return new Vec3Int(x / other.x, y / other.y, z / other.z);
    }

    public Vec3Int muln(float num) {
        return new Vec3Int(x * num, y * num, z * num);
    }

    public Vec3Int divn(float num) {
        return new Vec3Int(x / num, y / num, z / num);
    }

    @Override
    public String toString() {
        return String.format("Vec3Int %f, %f, %f", x, y, z);
    }

    public static final Vec3Int zero = new Vec3Int(0, 0, 0);
    public static final Vec3Int one = new Vec3Int(1, 1, 1);
    public static final Vec3Int xAxis = new Vec3Int(1, 0, 0);
    public static final Vec3Int yAxis = new Vec3Int(0, 1, 0);
    public static final Vec3Int zAxis = new Vec3Int(0, 0, 1); 
}