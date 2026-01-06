package telegram.bot.globals;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class CFrame {
    public final float x, y, z;
    public final float m11, m12, m13;
    public final float m21, m22, m23;
    public final float m31, m32, m33;

    public CFrame(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;

        m11 = 1; m12 = 0; m13 = 0;
        m21 = 0; m22 = 1; m23 = 0;
        m31 = 0; m32 = 0; m33 = 1;
    }

    public CFrame(float x, float y, float z, float m11, float m12, float m13, float m21, float m22, float m23, float m31, float m32, float m33) {
        this.x = x; this.y = y; this.z = z;

        this.m11 = m11; this.m12 = m12; this.m13 = m13;
        this.m21 = m21; this.m22 = m22; this.m23 = m23;
        this.m31 = m31; this.m32 = m32; this.m33 = m33;
    }

    public CFrame(Vec3 at, Vec3 lookAt) {
        Vec3 look = lookAt.sub(at).unit();
        Vec3 right = Vec3.yAxis.cross(look.negative()).unit();
        Vec3 up = right.cross(look).unit();

        x = at.x;
        y = at.y;
        z = at.z;

        m11 = right.x; m12 = right.y; m13 = right.z;
        m21 = up.x;    m22 = up.y;    m23 = up.z;
        m31 = look.x;  m32 = look.y;  m33 = look.z;
    }

    public CFrame(Vec3 pos, Vec3 xVec, Vec3 yVec, Vec3 zVec) {
        x = pos.x;
        y = pos.y;
        z = pos.z;

        m11 = xVec.x; m12 = xVec.y; m13 = xVec.z;
        m21 = yVec.x; m22 = yVec.y; m23 = yVec.z;
        m31 = zVec.x; m32 = zVec.y; m33 = zVec.z;
    }

    public CFrame(float x, float y, float z, double rx, double ry, double rz) {
        this.x = x; this.y = y; this.z = z;
    
        float crx = (float) cos(rx); float srx = (float) sin(rx);
        float cry = (float) cos(ry); float sry = (float) sin(ry);
        float crz = (float) cos(rz); float srz = (float) sin(rz);

        m11 = cry * crz;
        m12 = cry * srz;
        m13 = -sry;

        m21 = srx * sry * crz - crx * srz;
        m22 = srx * sry * srz + crx * crz;
        m23 = srx * cry;

        m31 = crx * sry * crz + srx * srz;
        m32 = crx * sry * srz - srx * crz;
        m33 = crx * cry;
    }

    public CFrame Inverse() {
        return new CFrame(
            -x * m11 - y * m21 - z * m31,
            -x * m12 - y * m22 - z * m32,
            -x * m13 - y * m23 - z * m33,
            m11, m21, m31,
            m12, m22, m32,
            m13, m23, m33
        );
    }

    public Vec3 Position() {
        return new Vec3(this.x, this.y, this.z);
    }

    public CFrame Rotation() {
        return new CFrame(0, 0, 0,
            m11, m12, m13,
            m21, m22, m23,
            m31, m32, m33
        );
    }

    public Vec3 XVector() {
        return new Vec3(m11, m12, m13);
    }

    public Vec3 YVector() {
        return new Vec3(m21, m22, m23);
    }

    public Vec3 ZVector() {
        return new Vec3(m31, m32, m33);
    }

    public CFrame mul(CFrame other) {
        return new CFrame(
            x + other.x * m11 + other.y * m12 + other.z * m13,
            y + other.x * m21 + other.y * m22 + other.z * m23,
            z + other.x * m31 + other.y * m32 + other.z * m33,
            m11 * other.m11 + m12 * other.m21 + m13 * other.m31,
            m11 * other.m12 + m12 * other.m22 + m13 * other.m32,
            m11 * other.m13 + m12 * other.m23 + m13 * other.m33,
            m21 * other.m11 + m22 * other.m21 + m23 * other.m31,
            m21 * other.m12 + m22 * other.m22 + m23 * other.m32,
            m21 * other.m13 + m22 * other.m23 + m23 * other.m33,
            m31 * other.m11 + m32 * other.m21 + m33 * other.m31,
            m31 * other.m12 + m32 * other.m22 + m33 * other.m32,
            m31 * other.m13 + m32 * other.m23 + m33 * other.m33
        );
    }

    public Vec3 mulv(Vec3 other) {
        return new Vec3(
            x + other.x * m11 + other.y * m12 + other.z * m13,
            y + other.x * m21 + other.y * m22 + other.z * m23,
            z + other.x * m31 + other.y * m32 + other.z * m33
        );
    }

    public CFrame addv(Vec3 other) {
        return new CFrame(
            x + other.x, y + other.y, z + other.z,
            m11, m12, m13,
            m21, m22, m23,
            m31, m32, m33
        );
    }

    public CFrame subv(Vec3 other) {
        return new CFrame(
            x - other.x, y - other.y, z - other.z,
            m11, m12, m13,
            m21, m22, m23,
            m31, m32, m33
        );
    }

    @Override
    public String toString() {
        return String.format(
            "CFrame: (%f, %f, %f) (%f, %f, %f) (%f, %f, %f) (%f, %f, %f)",
            x, y, z, m11, m12, m13,
            m21, m22, m23, m31, m32, m33
        );
    }
}