package io.github.octglam.uniaengine.renderers;

import io.github.octglam.uniaengine.models.RawModel;
import io.github.octglam.uniaengine.models.TexturedModel;
import io.github.octglam.uniaengine.shaders.StaticShader;
import io.github.octglam.uniaengine.spaces.Camera;
import io.github.octglam.uniaengine.spaces.Model;
import io.github.octglam.uniaengine.spaces.Space;
import io.github.octglam.uniaengine.textures.ModelTexture;
import io.github.octglam.uniaengine.utils.Transformation;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;

public class StaticRenderer {
    private static final float DEFAULT_FOV = 70f;
    private static final float DEFAULT_NEAR_PLANE = 0.1f;
    private static final float DEFAULT_FAR_PLANE = 1000f;

    private StaticShader shader;
    private Window window;

    public StaticRenderer(Window window, StaticShader shader){
        this.window = window;
        this.shader = shader;
    }

    public void init(){
        shader.start();
        shader.loadProjectionMatrix(window.updateProjectionMatrix(DEFAULT_FOV, DEFAULT_NEAR_PLANE, DEFAULT_FAR_PLANE));
        shader.stop();
    }

    public void render(HashMap<String, Space> spaces){
        for(String name : spaces.keySet()){
            Space space = spaces.get(name);

            boolean isModel = space instanceof Model;

            if(isModel) prepareTexturedModel(((Model) space).model);

            prepareInstance(space);
            if(isModel) GL11.glDrawElements(GL11.GL_TRIANGLES, ((Model) space).model.rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

            if(isModel) unbindTexturedModel();
            space.update();
        }
    }

    public void prepareTexturedModel(TexturedModel model){
        RawModel rawModel = model.rawModel;
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        ModelTexture texture = model.texture;
        if(texture.hasTransparency){
            MasterRenderer.disableCulling();
        }
        shader.loadTextureCoordinatesMul(model.textureCoordinatesMul);
        shader.loadFakeLightingVariable(texture.useFakeLighting);
        shader.loadShineVariables(texture.shineDamper, texture.reflectivity);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.texture.textureID);
    }

    public void prepareInstance(Space space){
        Matrix4f transformationMatrix = Transformation.createTransformationMatrix(space.position, space.rotation, space.scale);
        shader.loadTransformationMatrix(transformationMatrix);
    }

    public void unbindTexturedModel(){
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
}
