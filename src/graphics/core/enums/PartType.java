package telegram.bot.graphics.core.enums;

import java.io.FileReader;
import java.io.IOException;

import telegram.bot.graphics.core.MeshData;

public enum PartType {
    BLOCK("cube.obj");

    public MeshData meshData;

    PartType(String filePath) {
        String modelsDirectory = "C:\\Users\\User\\Desktop\\Проекты\\Телеграм Боты\\PaintBot\\app\\src\\main\\resources\\models\\";

        try (FileReader fileReader = new FileReader(modelsDirectory + filePath)) {
            this.meshData = new MeshData(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
