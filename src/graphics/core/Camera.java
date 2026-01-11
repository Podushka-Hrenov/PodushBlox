package telegram.bot.graphics.core;

import telegram.bot.graphics.math.CFrame;
import telegram.bot.graphics.math.ScreenPoint;
import telegram.bot.graphics.math.Vec2Int;
import telegram.bot.graphics.math.Vec3;
import telegram.bot.graphics.ui.ImagePanel;

import java.awt.Color;
import java.io.IOException;

import static java.lang.Math.tan;

class RenderCache {
    public ScreenPoint[] projectedVertexs = new ScreenPoint[1];
    public CFrame inversedCamera;
}

public class Camera extends PVInstance {
    private float aspect, xScale, yScale, zProjection, zTranslation;
    private final RenderCache renderCache = new RenderCache();
    public final ImagePanel imagePanel;

    public Camera(Vec2Int imageSize) {
        aspect = imageSize.x / imageSize.y;
        imagePanel = new ImagePanel(imageSize.x, imageSize.y, this::distoreDepth);
    }

    public void settings(double fov, float near, float far) {
        xScale = (float) (1.0 / (aspect * tan(fov / 2)));
        yScale = (float) (1.0 /tan(fov/2));
        zProjection = -(far + near) / (far - near);
        zTranslation = -2.0f * far * near / (far - near);
    }

    public float distoreDepth(float toHandle) {
        return zProjection * toHandle + zTranslation;
    }

    public void renderBasePart(BasePart basePart) {
        MeshData meshData = basePart.meshData();
        int totalVertexs = meshData.vertexs.length;

        if (totalVertexs > renderCache.projectedVertexs.length) {
            renderCache.projectedVertexs = new ScreenPoint[totalVertexs];
        }

        ScreenPoint[] projectedVertexs = renderCache.projectedVertexs;
        int[] triangles = meshData.tris;
        
        for (int i = 0; i < totalVertexs; i++) {
            Vec3 projected = renderCache.inversedCamera
                .mulv(basePart.cframe.mulv(meshData.vertexs[i])
            );

            int x = (int)(xScale*projected.x/projected.z*imagePanel.width);
            int y = (int)(yScale*projected.y/projected.z*imagePanel.height);
            float depth = 1.0f / (distoreDepth(projected.z));

            var screenPoint = projectedVertexs[i];
            if (screenPoint != null) {screenPoint.set(x, y, depth); continue;};

            projectedVertexs[i] = new ScreenPoint(x, y, depth);
        }

        CFrame basePartRotation = basePart.cframe.rotation();

        for (int i = 0; i < triangles.length; i += 6) {
            Vec3 normal = basePartRotation.mulv(meshData.normals[triangles[i+1]]);
                
            var v1 = projectedVertexs[triangles[i+0]];
            var v2 = projectedVertexs[triangles[i+2]];
            var v3 = projectedVertexs[triangles[i+4]];

            float scalar = Vec3.yAxis.dot(normal);
            Color color = basePart.color.muln(.9f * (scalar + 1.1f)).toColor();

            imagePanel.writeTris(v1, v2, v3, color);
        }
    }

    public void render() throws IOException {
        imagePanel.clear(Color.BLACK);
        
        renderCache.inversedCamera = cframe.inverse();

        parent().forEachChildrens(instance -> {
            if (!(instance instanceof BasePart)) {return;}
            renderBasePart((BasePart) instance);
        });
    }
}