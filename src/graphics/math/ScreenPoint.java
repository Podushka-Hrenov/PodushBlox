package telegram.bot.graphics.math;

import static java.lang.Math.sqrt;

public class ScreenPoint {
    public int x, y;
    public float depth;

    public ScreenPoint(int x, int y, float depth) {
        this.x = x; this.y = y; 
        this.depth = depth;
    }

    public ScreenPoint(float x, float y, float depth) {
        this.x = (int)x; this.y = (int)y; 
        this.depth = depth;
    }

    public ScreenPoint set(int x, int y, float depth) {
        this.x = x; this.y = y; this.depth = depth;
        return this;
    }

    public float dot(ScreenPoint other) {
        return x * other.x + y * other.y;
    }

    public float magnitude() {
        return (float)sqrt(x*x + y*y);
    }

    public ScreenPoint unit() {
        return divn(magnitude());
    }

    public ScreenPoint lerp(ScreenPoint goal, float t) {
        return ScreenPoint.identily().lerpLocal(goal, t);
    }

    public ScreenPoint lerpLocal(ScreenPoint goal, float t) {
        return lerpLocal(this, goal, t);
    }

    public ScreenPoint lerpLocal(ScreenPoint from, ScreenPoint goal, float t) {
        x = (int)(from.x + (goal.x - from.x) * t);
        y = (int)(from.y + (goal.y - from.y) * t);
        depth = from.depth + (goal.depth - from.depth) * t;

        return this;
    }

    public ScreenPoint negative() {
        return new ScreenPoint(-x, -y, depth);
    }

    public ScreenPoint add(ScreenPoint other) {
        return new ScreenPoint(x + other.x, y + other.y, depth + other.depth);
    }

    public ScreenPoint sub(ScreenPoint other) {
        return new ScreenPoint(x - other.x, y - other.y, depth - other.depth);
    }

    public ScreenPoint mul(ScreenPoint other) {
        return new ScreenPoint(x * other.x, y * other.y, depth * other.depth);
    }

    public ScreenPoint div(ScreenPoint other) {
        return new ScreenPoint(x / other.x, y / other.y, depth / other.depth);
    }

    public ScreenPoint muln(float num) {
        return new ScreenPoint(x * num, y * num, depth * num);
    }

    public ScreenPoint divn(float num) {
        return new ScreenPoint(x / num, y / num, depth / num);
    }

    public static ScreenPoint identily() {return new ScreenPoint(0, 0, 0);}

    @Override
    public String toString() {
        return String.format("ScreenPoint %d, %d, %f", x, y, depth);
    }
}
