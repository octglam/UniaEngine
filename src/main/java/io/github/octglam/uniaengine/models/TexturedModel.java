package io.github.octglam.uniaengine.models;

import io.github.octglam.uniaengine.textures.ModelTexture;

public class TexturedModel {
    public RawModel rawModel;
    public ModelTexture texture;

    public float textureCoordinatesMul = 1.0f;

    public TexturedModel(RawModel model, ModelTexture texture){
        this.rawModel = model;
        this.texture = texture;
    }
}
