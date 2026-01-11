package telegram.bot.graphics.math;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class CFrame {
    public float x, y, z;
    public float r00, r01, r02;
    public float r10, r11, r12;
    public float r20, r21, r22;

    public CFrame(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;

        r00 = 1f; r10 = 0f; r20 = 0f;
        r01 = 0f; r11 = 1f; r21 = 0f;
        r02 = 0f; r12 = 0f; r22 = 1f;
    }

    public CFrame(float x, float y, float z, 
        float r00, float r10, float r20,
        float r01, float r11, float r21,
        float r02, float r12, float r22) {
        
        this.x = x; this.y = y; this.z = z;

        this.r00 = r00; this.r10 = r10; this.r20 = r20;
        this.r01 = r01; this.r11 = r11; this.r21 = r21;
        this.r02 = r02; this.r12 = r12; this.r22 = r22;
    }

    public CFrame(float x, float y, float z, double rz, double ry, double rx) {
        this.x = x; this.y = y; this.z = z;
    
        double cosX = cos(rx), sinX = sin(rx);
        double cosY = cos(ry), sinY = sin(ry);
        double cosZ = cos(rz), sinZ = sin(rz);

        r00 = (float)(cosY * cosZ);
        r10 = (float)(cosX * sinZ + sinX * sinY * cosZ);
        r20 = (float)(sinX * sinZ - cosX * sinY * cosZ);

        r01 = (float)(-cosY * sinZ);
        r11 = (float)(cosX * cosZ - sinX * sinY * sinZ);
        r21 = (float)(sinX * cosZ + cosX * sinY * sinZ);

        r02 = (float)(sinY);
        r12 = (float)(-sinX * cosY);
        r22 = (float)(cosX * cosY);
    }

    public CFrame inverse() {
        return new CFrame(
            -(r00 * x + r01 * y + r02 * z),
            -(r10 * x + r11 * y + r12 * z),
            -(r20 * x + r21 * y + r22 * z),

            r00, r01, r02,
            r10, r11, r12,
            r20, r21, r22
        );
    }

    public Vec3 mulv(Vec3 other) {
        return new Vec3(
            x + r00 * other.x + r01 * other.y + r02 * other.z,
            y + r10 * other.x + r11 * other.y + r12 * other.z,
            z + r20 * other.x + r21 * other.y + r22 * other.z
        );
    }

    public CFrame setMulOf(CFrame cf0, CFrame cf1) {
        cf0.x += cf0.r00 * cf1.x + cf0.r01 * cf1.y + cf0.r02 * cf1.z;
        cf0.y += cf0.r10 * cf1.x + cf0.r11 * cf1.y + cf0.r12 * cf1.z;
        cf0.z += cf0.r20 * cf1.x + cf0.r21 * cf1.y + cf0.r22 * cf1.z;

        cf0.r00 = cf0.r00 * cf1.r00 + cf0.r01 * cf1.r10 + cf0.r02 * cf1.r20;
        cf0.r10 = cf0.r10 * cf1.r00 + cf0.r11 * cf1.r10 + cf0.r12 * cf1.r20;
        cf0.r20 = cf0.r20 * cf1.r00 + cf0.r21 * cf1.r10 + cf0.r22 * cf1.r20;
        cf0.r01 = cf0.r00 * cf1.r01 + cf0.r01 * cf1.r11 + cf0.r02 * cf1.r21;
        cf0.r11 = cf0.r10 * cf1.r01 + cf0.r11 * cf1.r11 + cf0.r12 * cf1.r21;
        cf0.r21 = cf0.r20 * cf1.r01 + cf0.r21 * cf1.r11 + cf0.r22 * cf1.r21;
        cf0.r02 = cf0.r00 * cf1.r02 + cf0.r01 * cf1.r12 + cf0.r02 * cf1.r22;
        cf0.r12 = cf0.r10 * cf1.r02 + cf0.r11 * cf1.r12 + cf0.r12 * cf1.r22;
        cf0.r22 = cf0.r20 * cf1.r02 + cf0.r21 * cf1.r12 + cf0.r22 * cf1.r22;

        return this;
    }

    public CFrame mulLocal(CFrame other) {
        return setMulOf(this, other);
    }

    public CFrame mul(CFrame other) {
        return CFrame.identily().setMulOf(this, other);
    }

    public CFrame rotation() {
        return new CFrame(0, 0, 0, 
            r00, r10, r20, r01, r11, r21, r02, r12, r22
        );
    }

    public Vec3 position() {
        return new Vec3(x, y, z);
    }

    public Vec3 xVector() {
        return new Vec3(r00, r10, r20);
    }

    public Vec3 yVector() {
        return new Vec3(r01, r11, r21);
    }

    public Vec3 zVector() {
        return new Vec3(r02, r12, r22);
    }

    @Override
    public String toString() {
        return String.format(
            "CFrame %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f",
            x, y, z, r00, r10, r20, r01, r11, r21, r02, r12, r22
        );
    }

    public static CFrame identily() {return new CFrame(0, 0, 0);}
}