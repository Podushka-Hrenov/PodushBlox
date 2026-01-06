package telegram.bot.objects;

import telegram.bot.globals.Vec3;
import telegram.bot.globals.Color3;
import telegram.bot.globals.MeshData;

public abstract class BasePart extends PVInstance {
    public Color3 color = Color3.Gray;
    public Vec3 size = Vec3.one.muln(5);

    public BasePart() {}

    public MeshData MeshData() {return null;}
}
