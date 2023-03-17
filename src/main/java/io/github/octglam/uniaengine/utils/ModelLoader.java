package io.github.octglam.uniaengine.utils;

import io.github.octglam.uniaengine.models.RawModel;
import io.github.octglam.uniaengine.renderers.Loader;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModelLoader {
    public static RawModel loadModel(String fileName, Loader loader){
        ArrayList<Float> positions = new ArrayList<>();
        ArrayList<Float> texCoords = new ArrayList<>();
        ArrayList<Float> normals = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();

        AIScene scene = Assimp.aiImportFile(fileName, Assimp.aiProcess_Triangulate);

        assert scene != null;
        PointerBuffer buffer = scene.mMeshes();

        for(int i = 0; i < Objects.requireNonNull(buffer).limit(); i++){
            AIMesh mesh = AIMesh.create(buffer.get(i));
            processMesh(mesh, positions, texCoords, normals, indices);
        }

        float[] posArray = new float[positions.size()];
        int i = 0;

        for (Float f : positions) {
            posArray[i++] = (f != null ? f : Float.NaN);
        }

        float[] texcoords = new float[texCoords.size()];
        i = 0;

        for (Float f : texCoords) {
            texcoords[i++] = (f != null ? f : Float.NaN);
        }

        float[] norms = new float[normals.size()];
        i = 0;

        for (Float f : normals) {
            norms[i++] = (f != null ? f : Float.NaN);
        }

        int[] inds = new int[indices.size()];
        i = 0;

        for (Integer f : indices) {
            inds[i++] = f != null ? f : 0;
        }

        return loader.loadToVAO(posArray, texcoords, norms, inds);
    }

    public static int loadModelMaterialData(String fileName, Loader loader){
        AIScene scene = Assimp.aiImportFile(fileName, Assimp.aiProcess_Triangulate);

        assert scene != null;

        PointerBuffer textures = scene.mTextures();
        assert textures != null;
        for ( int i = 0; i < textures.remaining(); i++) {
            AITexture texture = AITexture.create(textures.get(i));
            long address = texture.pcData().address0();
            ByteBuffer buffer = MemoryUtil.memByteBuffer(address, texture.mWidth());

            //STBImage.stbi_set_flip_vertically_on_load(true);
            int[] width = new int[1];
            int[] height = new int[1];
            int[] comp = new int[1];
            ByteBuffer imageBuffer = STBImage.stbi_load_from_memory(buffer, width, height, comp, 4);

            int id = GL11.glGenTextures();
            loader.textures.add(id);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width[0], height[0], 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            assert imageBuffer != null;
            STBImage.stbi_image_free(imageBuffer);
            return id;
        }

        return -127;
    }

    public static RawModel loadObjModel(String fileName, Loader loader) {
        FileReader fr = null;
        try {
            fr = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load file!");
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fr);
        String line;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float[] verticesArray;
        float[] normalsArray = null;
        float[] textureArray = null;
        int[] indicesArray;
        try {

            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    textureArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1,indices,textures,normals,textureArray,normalsArray);
                processVertex(vertex2,indices,textures,normals,textureArray,normalsArray);
                processVertex(vertex3,indices,textures,normals,textureArray,normalsArray);
                line = reader.readLine();
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size()*3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for(Vector3f vertex:vertices){
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for(int i=0;i<indices.size();i++){
            indicesArray[i] = indices.get(i);
        }
        return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
    }

    private static void processVertex(String[] vertexData, List<Integer> indices,
                                      List<Vector2f> textures, List<Vector3f> normals, float[] textureArray,
                                      float[] normalsArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);
        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
        textureArray[currentVertexPointer*2] = currentTex.x;
        textureArray[currentVertexPointer*2+1] = 1 - currentTex.y;
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
        normalsArray[currentVertexPointer*3] = currentNorm.x;
        normalsArray[currentVertexPointer*3+1] = currentNorm.y;
        normalsArray[currentVertexPointer*3+2] = currentNorm.z;
    }

    private static void processMesh(AIMesh mesh, ArrayList<Float> positions, ArrayList<Float> texCoords, ArrayList<Float> normals, ArrayList<Integer> indices){
        AIVector3D.Buffer vectors = mesh.mVertices();

        for(int i = 0; i < vectors.limit(); i++){
            AIVector3D vector = vectors.get(i);

            positions.add(vector.x());
            positions.add(vector.y());
            positions.add(vector.z());
        }

        AIVector3D.Buffer coords = mesh.mTextureCoords(0);
        int numTextCoords = coords != null ? coords.remaining() : 0;
        for(int i = 0; i < numTextCoords; i++){
            AIVector3D coord = coords.get(i);

            texCoords.add(coord.x());
            texCoords.add(1 - coord.y());
        }

        AIVector3D.Buffer norms = mesh.mNormals();

        for(int i = 0; i < norms.limit(); i++){
            AIVector3D norm = norms.get(i);

            normals.add(norm.x());
            normals.add(norm.y());
            normals.add(norm.z());
        }

        AIFace.Buffer faces = mesh.mFaces();

        for(int i = 0; i < faces.limit(); i++){
            AIFace face = faces.get(i);

            for(int j = 0; j < face.mIndices().limit(); j++){
                IntBuffer indice = face.mIndices();
                indices.add(indice.get(0));
                indices.add(indice.get(1));
                indices.add(indice.get(2));
            }
        }
    }
}
