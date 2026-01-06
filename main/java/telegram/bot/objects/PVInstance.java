package telegram.bot.objects;

import telegram.bot.globals.CFrame;
import telegram.bot.globals.Vec3;

public abstract class PVInstance extends Instance {
    public CFrame CFrame;

    public PVInstance() {
        this.CFrame = new CFrame(0, 0, 0);
    }

    public Vec3 Position() {
        return this.CFrame.Position();
    }
}
