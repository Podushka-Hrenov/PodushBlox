package telegram.bot.graphics.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import telegram.bot.graphics.math.Vec3;

public class MeshData {
    public final Vec3[] vertexs;
    public final int[] tris;
    public final Vec3[] normals;

    private Vec3 parseVec3(String[] data) {
        float x = Float.parseFloat(data[1]);
        float y = Float.parseFloat(data[2]);
        float z = Float.parseFloat(data[3]);

        return new Vec3(x, y, z);
    }

    public MeshData(FileReader fileReader) throws IOException {
        BufferedReader reader = new BufferedReader(fileReader);
        
        List<Vec3> vertexs = new ArrayList<>();
        List<Vec3> normals = new ArrayList<>();
        List<Integer> tris = new ArrayList<>();

        String line; 
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) continue;

            line = line.replace(',', '.');
            String[] data = line.split("\\s+");
                
            switch (data[0]) {
                case "v": vertexs.add(parseVec3(data)); break;
                case "vn": normals.add(parseVec3(data)); break;
                
                case "f": {
                    for (int i = 1; i <= 3; i++) {
                        String[] trisData = data[i].split("/");

                        int vertexIndex = Integer.parseInt(trisData[0]) - 1;
                        int normalIndex = Integer.parseInt(trisData[2]) - 1;

                        tris.add(vertexIndex);
                        tris.add(normalIndex);
                    }
                }
            }
        }

        reader.close();
            
        this.vertexs = vertexs.toArray(Vec3[]::new);
        this.normals = normals.toArray(Vec3[]::new);
        this.tris = tris.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public String toString() {
        return "MeshData";
    }
}
