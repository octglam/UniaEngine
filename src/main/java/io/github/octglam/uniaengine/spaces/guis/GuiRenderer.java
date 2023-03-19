package io.github.octglam.uniaengine.spaces.guis;

import io.github.octglam.uniaengine.models.RawModel;
import io.github.octglam.uniaengine.renderers.Loader;
import io.github.octglam.uniaengine.renderers.MasterRenderer;
import io.github.octglam.uniaengine.spaces.Space;
import io.github.octglam.uniaengine.utils.Transformation;
import org.joml.Matrix4f;
import org.lwjgl.opengl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GuiRenderer {
    private static final Logger LOGGER = LoggerFactory.getLogger("GuiRenderer");
    private RawModel quad;
    private GuiShader shader;
    private Loader loader;
    private MasterRenderer renderer;

    public GuiRenderer(Loader loader, MasterRenderer renderer){
        this.loader = loader;
        this.renderer = renderer;
    }

    public void init(){
        quad = loader.loadToVAO(new float[]{-1, 1, -1, -1, 1, 1, 1, -1});
        shader = new GuiShader();
        shader.init();

        LOGGER.info("Initialized!");
    }

    public void render(List<Space> spaces){
        List<GuiBase> pg = new ArrayList<>();
        for(Space space : spaces){
            if(space instanceof GuiBase){
                pg.add((GuiBase) space);
            }
        }

        List<GuiBase> guis = new ArrayList<>(pg);
        guis.sort(Comparator.comparingInt(o -> o.zIndex));

        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        for(GuiBase gui:guis){
            gui.onRender();
            if(gui.texture != -999) GL13.glActiveTexture(GL13.GL_TEXTURE0);
            if(gui.texture != -999) GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.texture);

            Matrix4f matrix = Transformation.createTransformationMatrix(gui.position, gui.scale);
            shader.loadTransformation(matrix);
            shader.loadColour(gui.colour);

            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
            gui.update();
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void cleanUp(){
        shader.cleanUp();
    }
}
