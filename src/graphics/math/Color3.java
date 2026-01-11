package telegram.bot.graphics.math;

import static java.lang.Math.clamp;

import java.awt.Color;

public class Color3 {
    public final float r, g, b;

    public Color3(float r, float g, float b) {
        this.r = r; this.g = g; this.b = b;
    }

    public Color3(Color color) {
        r = color.getRed(); g = color.getGreen(); b = color.getBlue();
    }

    public Color3 lerp(Color3 goal, float t) {
        return add(goal.sub(this).muln(t));
    }

    public Color3 blend(Color3 other, float alpha) {
        return other.muln(alpha).mul(this);
    }

    public Color toColor() {
        return new Color(clamp((int)r, 0, 255), clamp((int)g, 0, 255), clamp((int)b, 0, 255));
    }

    public Color3 add(Color3 other) {
        return new Color3(r + other.r, g + other.g, b + other.b);
    }

    public Color3 sub(Color3 other) {
        return new Color3(r - other.r, g - other.g, b - other.b);
    }

    public Color3 mul(Color3 other) {
        return new Color3(r * other.r, g * other.g, b * other.b);
    }

    public Color3 div(Color3 other) {
        return new Color3(r / other.r, g / other.g, b / other.b);
    }

    public Color3 muln(float num) {
        return new Color3(r * num, g * num, b * num);
    }

    public Color3 divn(float num) {
        return new Color3(r / num, g / num, b / num);
    }

    @Override
    public String toString() {
        return String.format("Color3 %f, %f, %f", r, g, b);
    }

    public static Color3 Red = new Color3(255, 0, 0);
    public static Color3 Green = new Color3(0, 255, 0);
    public static Color3 Blue = new Color3(0, 0, 255);
    public static Color3 Gray = new Color3(173, 165, 135);
}
