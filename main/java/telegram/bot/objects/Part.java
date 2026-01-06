package telegram.bot.objects;

import telegram.bot.globals.Enum;
import telegram.bot.globals.MeshData;

public class Part extends BasePart {
    public String shape = "Block";

    public MeshData MeshData() {
        return Enum.PartType.get(shape);
    }
}
