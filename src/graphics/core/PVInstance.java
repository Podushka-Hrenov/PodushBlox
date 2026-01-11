package telegram.bot.graphics.core;

import telegram.bot.graphics.math.CFrame;
import telegram.bot.graphics.math.Vec3;

public abstract class PVInstance extends Instance {
    public CFrame cframe;

    public PVInstance() {
        cframe = new CFrame(0, 0, 0);
    }

    public Vec3 position() {
        return cframe.position();
    }
}
