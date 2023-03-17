package io.github.octglam.uniaengine.renderers;

import io.github.octglam.uniaengine.spaces.guis.GuiRenderer;
import io.github.octglam.uniaengine.spaces.guis.GuiBase;
import io.github.octglam.uniaengine.shaders.StaticShader;
import io.github.octglam.uniaengine.spaces.Camera;
import io.github.octglam.uniaengine.spaces.SunLight;
import io.github.octglam.uniaengine.spaces.Space;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;

public class MasterRenderer {
    private final StaticShader shader = new StaticShader();
    private final Window window;
    private final StaticRenderer staticRenderer;
    private final GuiRenderer guiRenderer;

    public final HashMap<String, Space> renderSpaces = new HashMap<>();
    public final HashMap<String, GuiBase> renderGuis = new HashMap<>();

    public float sky_red = 0.6f;
    public float sky_green = 0.78f;
    public float sky_blue = 0.76f;

    private Camera currentCamera;

    public MasterRenderer(Window window, Loader loader){
        this.window = window;
        staticRenderer = new StaticRenderer(window, shader);
        guiRenderer = new GuiRenderer(loader);
    }

    public void init(){
        enableCulling();
        shader.init();
        staticRenderer.init();
        guiRenderer.init();
    }

    public static void enableCulling(){
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling(){
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void prepare(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        window.rgba = new Vector4f(sky_red, sky_green, sky_blue, 1.0f);
    }

    public void render(SunLight sun, Camera camera){
        window.clear();
        prepare();

        shader.start();
        shader.loadSkyColour(new Vector3f(sky_red, sky_green, sky_blue));
        shader.loadLight(sun);
        setCurrentCamera(camera);
        updateCurrentCamera();
        staticRenderer.render(renderSpaces);
        shader.stop();
        camera.update();

        for(int i=renderGuis.values().size()-1; i>=0; i--){
            GuiBase gui = new ArrayList<>(renderGuis.values()).get(i);
            gui.onPrepare(this);
        }

        guiRenderer.render(renderGuis.values().stream().toList());

        window.update();
        renderGuis.clear();
        renderSpaces.clear();
    }

    public void processSpace(Space space){
        renderSpaces.put(space.name, space);
    }

    public void processGui(GuiBase gui){
        renderGuis.put(gui.name, gui);
    }

    public void cleanUp(){
        guiRenderer.cleanUp();
        shader.cleanUp();
        window.destroy();
    }

    public void setCurrentCamera(Camera camera){
        currentCamera = camera;
        if(!shader.isStarted()) shader.start();
        shader.loadProjectionMatrix(window.updateProjectionMatrix(camera.FOV, camera.NEAR_PLANE, camera.FAR_PLANE));
        if(!shader.isStarted()) shader.stop();
    }

    public Camera getCurrentCamera(){
        return currentCamera;
    }

    public void updateCurrentCamera(){
        shader.loadViewMatrix(currentCamera);
    }
}
