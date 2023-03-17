package io.github.octglam.uniaengine.spaces;

import io.github.octglam.uniaengine.models.TexturedModel;
import org.joml.Vector3f;

public class Model extends Space {
    public TexturedModel model;

    public Model(String name, TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale){
        super(name, position, rotation, scale);
        this.model = model;
    }
}
