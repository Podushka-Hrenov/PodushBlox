import static java.lang.Math.PI;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import graphics.core.*;
import graphics.math.*;

public class Test {
    static File outputFile = new File("C:\\Users\\User\\Desktop\\Render.png");
    static Workspace workspace = new Workspace();
    public static void main(String[] args) throws IOException {
        var camera = new Camera(Vec2Int.one.muln(1000));
        camera.cframe = new CFrame(0, 0, -5.1f);
        camera.settings(PI/2, 10f, 100f);
        camera.setParent(workspace);

        var part = new Part();
        part.cframe = new CFrame(0, 0, 0, PI*.3, PI*.1, PI*.6);
        part.setParent(workspace);

        camera.render();

        ImageIO.write(camera.imagePanel.image, "PNG", outputFile);
        System.out.println("Done!");
    }
}

