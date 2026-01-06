package telegram.bot.objects;

import telegram.bot.globals.Vec2;
import telegram.bot.globals.Vec3;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.tan;

import java.util.Arrays;

import telegram.bot.globals.ImagePanel;
import telegram.bot.globals.MeshData;

public class Camera extends PVInstance {
    public float fov, near, far, aspect;
    private Vec2 imageSize;
    private ImagePanel image;

    public Camera(float fov, float near, float far, Vec2 imageSize) {
        this.fov = fov; this.near = near; this.far = far;
        this.aspect = imageSize.x / imageSize.y;

        this.image = new ImagePanel((short) imageSize.x, (short) imageSize.y);
        this.imageSize = imageSize;
    }

    public File render() throws IOException {
        image.clear(new Color(0,0,0));

        BasePart[] baseParts = Arrays.stream(this.Parent().Children())
        .filter(BasePart.class::isInstance).toArray(BasePart[]::new);

        float a = (float) (1.0 / (aspect * tan(fov / 2)));
        float b = (float) (1.0 /tan(this.fov/2));
        float c = -(far + near) / (far - near);
        float d = -2.0f * far * near / (far - near);
        
        for (BasePart basePart : baseParts) {
            MeshData meshData = basePart.MeshData();

            Vec3[] vertexs = Arrays.stream(
                meshData.vertexs).map(vec -> {
                    Vec3 i = CFrame.Inverse().mul(basePart.CFrame).mulv(vec);
                    return new Vec3(
                        (a*i.x/i.z)*imageSize.x, 
                        (b*i.y/i.z)*imageSize.y, 
                        ((c * i.z + d) / i.z)
                    );
                }
            ).toArray(Vec3[]::new);

            for (int i = 0; i < meshData.tris.length; i += 6) {
                Vec3 normal = basePart.CFrame.Rotation().mulv(meshData.normals[meshData.tris[i+1]]);
                
                Vec3 v1 = vertexs[meshData.tris[i+0]];
                Vec3 v2 = vertexs[meshData.tris[i+2]];
                Vec3 v3 = vertexs[meshData.tris[i+4]];

                float scalar = Vec3.yAxis.dot(normal);
                Color color = basePart.color.muln(scalar + 1.2f).toColor();

                image.writeTris(v1, v2, v3, color);
            }
        }

        return this.image.createFile("C:\\Users\\User\\Desktop\\", "Render.png");
    }
}
