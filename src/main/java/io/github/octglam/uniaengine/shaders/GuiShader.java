package io.github.octglam.uniaengine.shaders;

import io.github.octglam.uniaengine.shaders.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class GuiShader extends ShaderProgram {
    private static final String VERTEX_FILE = "res/shaders/gui/vs.glsl";
    private static final String FRAGMENT_FILE = "res/shaders/gui/fs.glsl";

    private int location_transformationMatrix;
    private int location_colour;

    public GuiShader(){
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_colour = super.getUniformLocation("colour");
    }

    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadColour(Vector4f colour){
        super.loadVector4(location_colour, colour);
    }
}
