package telegram.bot.objects;

import java.io.File;
import java.io.IOException;

import telegram.bot.globals.MeshData;

public class MeshPart extends BasePart {
    private MeshData meshData;

    public MeshPart(File modelFile) throws IOException {
        meshData = new MeshData(modelFile);
    }

    @Override
    public MeshData MeshData() {
        return meshData;
    }
}
