package io.github.octglam.uniaengine.shaders;

import io.github.octglam.uniaengine.spaces.Camera;
import io.github.octglam.uniaengine.spaces.SunLight;
import io.github.octglam.uniaengine.utils.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "res/shaders/static/vs.glsl";
    private static final String FRAGMENT_FILE = "res/shaders/static/fs.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColour;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLight;
    private int location_textureCoordinatesMul;
    private int location_skyColour;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColour = super.getUniformLocation("lightColour");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_useFakeLight = super.getUniformLocation("useFakeLight");
        location_textureCoordinatesMul = super.getUniformLocation("textureCoordinatesMul");
        location_skyColour = super.getUniformLocation("skyColour");
    }

    public void loadSkyColour(Vector3f colour){
        super.loadVector(location_skyColour, colour);
    }

    public void loadTextureCoordinatesMul(float mul){
        super.loadFloat(location_textureCoordinatesMul, mul);
    }

    public void loadFakeLightingVariable(boolean useFake){
        super.loadBoolean(location_useFakeLight, useFake);
    }

    public void loadShineVariables(float damper, float reflectivity){
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadLight(SunLight sunLight){
        super.loadVector(location_lightPosition, sunLight.position);
        super.loadVector(location_lightColour, sunLight.colour);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Transformation.getViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(location_projectionMatrix, projection);
    }
}
