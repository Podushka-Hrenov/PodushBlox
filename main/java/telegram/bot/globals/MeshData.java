package telegram.bot.globals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

public class MeshData {
    public Vec3[] vertexs;
    public int[] tris;
    public Vec3[] normals;

    public MeshData(Vec3[] vertexs, int[] tris) {
        this.vertexs = vertexs;
        this.tris = tris;

        for (int i = 0; i <= tris.length; i += 3) {
            Vec3 V1 = vertexs[tris[i+0]];
            Vec3 V2 = vertexs[tris[i+1]];
            Vec3 V3 = vertexs[tris[i+2]];

            normals[i/3] = V2.sub(V1).cross(V2.sub(V3)).unit();
        }
    }

    public Vec3 getAABB() {
        float maxX = 0;
        float maxY = 0;
        float maxZ = 0;

        for (Vec3 vert : vertexs) {
            if (vert.x > maxX) maxX = vert.x;
            if (vert.y > maxY) maxY = vert.y;
            if (vert.z > maxZ) maxZ = vert.z;
        }

        return new Vec3(maxX*2, maxY*2, maxZ*2);
    }

    public MeshData(String filePath) throws IOException {
        loadModel(new FileReader(filePath));
    }

    public MeshData(File file) throws IOException {
        loadModel(new FileReader(file));
    }

    private void loadModel(FileReader fileReader) throws IOException {
        try (BufferedReader reader = new BufferedReader(fileReader)) {
            String line;

            for (int i = 1; i <= 3; i++) {
                line = reader.readLine();
            }

            ArrayList<Vec3> vertexs = new ArrayList<>();
            ArrayList<Vec3> normals = new ArrayList<>();
            ArrayList<Integer> tris = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;

                line = line.replace(',', '.');
                String[] data = line.split("\\s+");
                
                switch (data[0]) {
                    case "v": {
                        float x = Float.valueOf(data[1]);
                        float y = Float.valueOf(data[2]);
                        float z = Float.valueOf(data[3]);

                        vertexs.add(new Vec3(x, y, z));
                        break;
                    }  
                    case "vn": {
                        float x = Float.valueOf(data[1]);
                        float y = Float.valueOf(data[2]);
                        float z = Float.valueOf(data[3]);

                        normals.add(new Vec3(x, y, z));
                        break;
                    }
                    case "f": {
                        for (int i = 1; i <= 3; i++) {
                            String[] trisData = data[i].split("/");

                            int vertexIndex = Integer.valueOf(trisData[0]) - 1;
                            int normalIndex = Integer.valueOf(trisData[2]) - 1;

                            tris.add(vertexIndex);
                            tris.add(normalIndex);
                        }
                    }
                }
            }

            if (vertexs.size() == 0 || normals.size() == 0 || tris.size() == 0) {
                throw new IOException();
            }

            Vec3 xx = vertexs.get(0);

            for (int i = 1; i < vertexs.size(); i++) {
                xx = xx.add(vertexs.get(i));
            }

            xx = xx.divn(vertexs.size());

            for (int i = 0; i < vertexs.size(); i++) {
                vertexs.set(i, vertexs.get(i).sub(xx));
            }
            
            this.vertexs = vertexs.toArray(Vec3[]::new);
            this.normals = normals.toArray(Vec3[]::new);
            this.tris = tris.stream().mapToInt(Integer::intValue).toArray();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "MeshData";
    }
}
