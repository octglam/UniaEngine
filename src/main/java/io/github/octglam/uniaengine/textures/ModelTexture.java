package io.github.octglam.uniaengine.textures;

public class ModelTexture {
    public int textureID;

    public float shineDamper = 1;
    public float reflectivity = 0;

    public boolean hasTransparency = false;
    public boolean useFakeLighting = false;

    public ModelTexture(int id){
        this.textureID = id;
    }
}
