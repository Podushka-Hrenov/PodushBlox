package telegram.bot.globals;

import static java.lang.Math.abs;
import static java.lang.Math.min;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ImagePanel {
    public short height, width;
    private float[] zBuffer;
    private BufferedImage image;
    
    public ImagePanel(short width, short height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.height = height; this.width = width;
        this.zBuffer = new float[width*height];

        Arrays.fill(zBuffer, Float.POSITIVE_INFINITY);
    }

    public boolean write(Vec3 pos, Color color) {
        short x = (short)(pos.x + this.width/2);
        short y = (short)(-pos.y + this.height/2);

        if (x < 0 || x >= width || y < 0 || y >= height) return false;

        int idx = y * width + x;
        if (pos.z >= zBuffer[idx]) return true;

        image.setRGB(x, y, color.getRGB());
        zBuffer[idx] = pos.z;

        return true;
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public void writeline(Vec3 v1, Vec3 v2, Color color) {
        Vec3 ab = v2.sub(v1);

        if (ab.y*ab.y > ab.x*ab.x) {
            float currentLenght = abs(ab.y);

            if (v1.x*v1.x > v2.x*v2.x) {Vec3 temp = v1; v1 = v2; v2 = temp;}

            for (short y = 1; y <= currentLenght; y++) {
                float alpha = y / currentLenght;
                if (!write(v1.lerp(v2, alpha), color)) return;
            }
        } else {
            float currentWidth = abs(ab.x);

            if (v1.y*v1.y > v2.y*v2.y) {Vec3 temp = v1; v1 = v2; v2 = temp;}
            
            for (short x = 1; x <= currentWidth; x++) {
                float alpha = x / currentWidth;
                if (!write(v1.lerp(v2, alpha), color)) return;
            }
        }
    }

    public void writeTris(Vec3 v1, Vec3 v2, Vec3 v3, Color color) {
        if (v3.y > v1.y) {Vec3 temp = v1; v1 = v3; v3 = temp;}
        if (v2.y > v1.y) {Vec3 temp = v1; v1 = v2; v2 = temp;}
        if (v2.y < v3.y) {Vec3 temp = v3; v3 = v2; v2 = temp;}

        float totalHeight = v1.y - v3.y;
        Vec3 v1p = v3.lerp(v1, (v2.y - v3.y)/totalHeight);

        {
            float segHeight = v2.y - v3.y;

            for (float y = v3.y; y < min(v2.y, height); y++) {
                float alpha = (y - v3.y) / segHeight;
                
                Vec3 A = v3.lerp(v2, alpha);
                Vec3 B = v3.lerp(v1p, alpha);

                if (A.x > B.x) {Vec3 temp = A; A = B; B = temp;}

                for (float x = A.x; x < min(B.x, width); x++) {
                    write(new Vec3(x, y, lerp(A.z, B.z, x/(A.x-B.x))), color);
                }
            }
        }

        {
            float segHeight = v1.y - v2.y;

            for (float y = v2.y; y < min(v1.y, height); y++) {
                float alpha = (y - v2.y) / segHeight;
                
                Vec3 A = v2.lerp(v1, alpha);
                Vec3 B = v1p.lerp(v1, alpha);

                if (A.x > B.x) {Vec3 temp = A; A = B; B = temp;}

                for (float x = A.x; x < min(B.x, width); x++) {
                    write(new Vec3(x, y, lerp(A.z, B.z, x/(A.x-B.x))), color);
                }
            }
        }
    }

    public void clear(Color fillColor) {
        int colorRGB = fillColor.getRGB();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, colorRGB);
            }
        }

        Arrays.fill(zBuffer, Float.POSITIVE_INFINITY);
    }

    public File createFile(String path, String fileName) throws IOException {
        File file = new File(String.join("\\", path, fileName));
        ImageIO.write(this.image, "PNG", file);

        return file;
    }
}