package graphics.core.enums;

import java.io.FileReader;
import java.io.IOException;

import graphics.core.MeshData;

public enum PartType {
    BLOCK("cube.obj");

    public MeshData meshData;

    PartType(String filePath) {
        String modelsDirectory = "C:\\Users\\User\\Desktop\\Rava3D\\assets\\models\\";

        try (FileReader fileReader = new FileReader(modelsDirectory + filePath)) {
            this.meshData = new MeshData(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

