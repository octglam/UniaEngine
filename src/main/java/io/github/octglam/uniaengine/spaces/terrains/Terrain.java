package io.github.octglam.uniaengine.spaces.terrains;

import io.github.octglam.uniaengine.models.RawModel;
import io.github.octglam.uniaengine.models.TexturedModel;
import io.github.octglam.uniaengine.renderers.Loader;
import io.github.octglam.uniaengine.spaces.Model;
import io.github.octglam.uniaengine.textures.ModelTexture;
import io.github.octglam.uniaengine.utils.OpenSimplex2S;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;

public class Terrain extends Model {
    private static final double FREQUENCY = 1.0 / 64.0;

    public float SIZE;
    public int VERTEX_COUNT = 128;
    public float MAX_HEIGHT = 20;
    public float MAX_PIXEL_COLOUR = 256 * 256 * 256;

    public ModelTexture texture;

    public Terrain(String name, Vector3f position, Vector3f rotation, Vector3f scale, Loader loader, ModelTexture texture, float size){
        super(name, null, new Vector3f(position.x * size, position.y, position.z * size), rotation, scale);
        this.texture = texture;
        this.SIZE = size;
        this.model = new TexturedModel(generateTerrain(loader), texture);
        this.model.textureCoordinatesMul = 80.0f;
    }

    private RawModel generateTerrain(Loader loader){
        BufferedImage image = new BufferedImage((int) SIZE, (int) SIZE, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < SIZE; y++)
        {
            for (int x = 0; x < SIZE; x++)
            {
                double value = OpenSimplex2S.noise3_ImproveXY(0, x * FREQUENCY, y * FREQUENCY, 0.0);
                int rgb = 0x010101 * (int)((value + 1) * 127.5);
                image.setRGB(x, y, rgb);
            }
        }

        VERTEX_COUNT = image.getHeight();

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer*3+1] = getHeight(j, i, image);
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                normals[vertexPointer*3] = 0;
                normals[vertexPointer*3+1] = 1;
                normals[vertexPointer*3+2] = 0;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private float getHeight(int x, int z, BufferedImage image){
        if(x<0 || x>=image.getHeight() || z<0 || z>=image.getHeight()){
            return 0;
        }
        float height = image.getRGB(x, z);
        height += MAX_PIXEL_COLOUR/2f;
        height /= MAX_PIXEL_COLOUR/2f;
        height *= MAX_HEIGHT;
        return height;
    }
}
