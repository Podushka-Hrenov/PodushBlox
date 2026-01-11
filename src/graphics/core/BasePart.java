package telegram.bot.graphics.core;

import telegram.bot.graphics.math.Vec3;
import telegram.bot.graphics.math.Color3;

public abstract class BasePart extends PVInstance {
    public Color3 color = Color3.Gray;
    public Vec3 size = Vec3.one.muln(5);
    
    public BasePart() {}

    public MeshData meshData() {return null;}
}
