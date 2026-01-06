package telegram.bot.globals;

import java.util.HashMap;
import java.io.IOException;

public class Enum {
    public static HashMap<String, MeshData> PartType = new HashMap<>() {{
        try {
            put("Block", new MeshData(
                "C:\\Users\\User\\Desktop\\telegram-bot\\app\\src\\main\\resources\\models\\cube.obj"
            ));
            put("Monkey", new MeshData(
                "C:\\Users\\User\\Desktop\\telegram-bot\\app\\src\\main\\resources\\models\\monkey.obj"
            ));
            put("Tank", new MeshData(
                "C:\\Users\\User\\Desktop\\telegram-bot\\app\\src\\main\\resources\\models\\tank.obj"
            ));
        } catch (IOException e) {
            throw new RuntimeException("attempt to load model", e);
        }
    }};
}
