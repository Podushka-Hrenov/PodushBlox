package telegram.bot.graphics.ui;

import telegram.bot.graphics.math.ScreenPoint;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.function.Function;

import org.bytedeco.javacv.Frame;

class DrawingCache {
    public ScreenPoint TrisB = ScreenPoint.identily();
    public ScreenPoint TrisA = ScreenPoint.identily();
    public ScreenPoint TrisV1p = ScreenPoint.identily();
}

public class ImagePanel {
    public final int height, width;
    private final ByteBuffer buffer;
    private final float[] zBuffer;
    private final Function<Float, Float> depthDistorter;
    private final DrawingCache drawingCache;
    
    public ImagePanel(int width, int height, Function<Float, Float> depthDistorter) {
        this.height = height; this.width = width;
        
        zBuffer = new float[width*height];
        buffer = ByteBuffer.allocate(width*height*3);
        this.depthDistorter = depthDistorter;
        drawingCache = new DrawingCache();
        
        Arrays.fill(zBuffer, Float.POSITIVE_INFINITY);
    }

    public int getPixelIndex(int x, int y) {
        return y * width + x;
    }

    public boolean write(ScreenPoint point, Color color) {
        return write(point.x, point.y, point.depth, color);
    }

    public boolean write(int x, int y, float depth, Color color) {
        x += width/2; y += height/2;

        if (x < 0 || x >= width || y < 0 || y >= height) return false;

        int bufferIndex = getPixelIndex(x, y) * 3;
        if (depth > zBuffer[bufferIndex/3]) return true;

        buffer.put(bufferIndex, (byte)color.getRed());
        buffer.put(bufferIndex+1, (byte)color.getGreen());
        buffer.put(bufferIndex+2, (byte)color.getBlue());

        zBuffer[bufferIndex/3] = depth;

        return true;
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public void writeTris(ScreenPoint v1, ScreenPoint v2,
    ScreenPoint v3, Color color) {
        
        if (v3.y > v1.y) {var temp = v1; v1 = v3; v3 = temp;}
        if (v2.y > v1.y) {var temp = v1; v1 = v2; v2 = temp;}
        if (v2.y < v3.y) {var temp = v3; v3 = v2; v2 = temp;}

        var v1p = drawingCache.TrisV1p.lerpLocal(
            v3, v1, (v2.y - v3.y)/(float)(v1.y - v3.y)
        );

        var A = drawingCache.TrisA; var B = drawingCache.TrisB;
        float segHeight = v2.y - v3.y;

        for (int y = 0; y < segHeight; y++) {
            float alpha = y / segHeight;

            A.lerpLocal(v3, v2, alpha);
            B.lerpLocal(v3, v1p, alpha);
             
            if (A.x > B.x) {var temp = A; A = B; B = temp;}
            float width = B.x - A.x;

            for (int x = A.x; x < B.x; x++) {
                float depth = 1.0f / lerp(A.depth, B.depth, (x - A.x)/width);
                write(x, v3.y + y, depthDistorter.apply(depth), color);
            }
        }
        
        segHeight = v1.y - v2.y;

        for (int y = 0; y < segHeight; y++) {
            float alpha = y / segHeight;

            A.lerpLocal(v2, v1, alpha);
            B.lerpLocal(v1p, v1, alpha);
                
            if (A.x > B.x) {var temp = A; A = B; B = temp;}
            float width = B.x - A.x;

            for (int x = A.x; x < B.x; x++) {
                float depth = 1.0f / lerp(A.depth, B.depth, (x - A.x)/width);
                write(x, v2.y + y, depthDistorter.apply(depth), color);
            }
        }
    }

    public Frame getAsFrame() {
        Frame frame = new Frame(width, height, Frame.DEPTH_UBYTE, 3);
        frame.image[0] = buffer;
        frame.imageStride = width * 3;

        return frame;
    }
    
    public void clear(Color fillColor) {
        var r = (byte) fillColor.getRed();
        var g = (byte) fillColor.getGreen();
        var b = (byte) fillColor.getBlue();

        buffer.clear();

        for (int i = 0; i < width * height; i++) {
            buffer.put(r); buffer.put(g); buffer.put(b);
        }

        buffer.flip();
        Arrays.fill(zBuffer, Float.POSITIVE_INFINITY);
    }
}


// public void writeline(Vec3 v1, Vec3 v2, Color color) {
//         Vec3 ab = v2.sub(v1);

//         if (ab.y*ab.y > ab.x*ab.x) {
//             float lenght = abs(ab.y);

//             for (short y = 1; y <= lenght; y++) {
//                 float alpha = y / lenght;
//                 if (!write(v1.lerp(v2, alpha), color)) return;
//             }
//         } else {
//             float width = abs(ab.x);

//             for (short x = 1; x <= width; x++) {
//                 float alpha = x / width;
//                 if (!write(v1.lerp(v2, alpha), color)) return;
//             }
//         }
//     }