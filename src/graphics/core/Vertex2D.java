package telegram.bot.graphics.core;

public class Vertex2D {
    public final int sX, sY, tX, tY;
    public final float depth;

    public Vertex2D(int sX, int sY, int tX, int tY, float depth) {
        this.sX = sX; this.sY = sY;
        this.tX = tX; this.tY = sY;
        this.depth = depth;
    }
}
