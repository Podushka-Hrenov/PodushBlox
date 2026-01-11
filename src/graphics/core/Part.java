package telegram.bot.graphics.core;

import telegram.bot.graphics.core.enums.PartType;

public class Part extends BasePart {
    public PartType shape = PartType.BLOCK;

    public MeshData meshData() {
        return shape.meshData;
    }
}
