package io.github.octglam.uniaengine.renderers;

import io.github.octglam.uniaengine.spaces.Space;
import io.github.octglam.uniaengine.spaces.guis.GuiBase;
import io.github.octglam.uniaengine.shaders.StaticShader;
import io.github.octglam.uniaengine.spaces.threeD.Camera;
import io.github.octglam.uniaengine.spaces.threeD.SunLight;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class MasterRenderer {
    private static final Logger LOGGER = LoggerFactory.getLogger("MasterRenderer");
    private final StaticShader shader = new StaticShader();
    private final Window window;
    private final StaticRenderer staticRenderer;
    private final GuiRenderer guiRenderer;

    public final HashMap<String, Space> spaces = new HashMap<>();

    public float sky_red = 0.6f;
    public float sky_green = 0.78f;
    public float sky_blue = 0.76f;

    private Camera currentCamera;

    public MasterRenderer(Window window, Loader loader){
        this.window = window;
        staticRenderer = new StaticRenderer(window, shader);
        guiRenderer = new GuiRenderer(loader, this);
    }

    public void init(){
        enableCulling();
        shader.init();
        staticRenderer.init();
        guiRenderer.init();

        LOGGER.info("MasterRenderer Initialized!");
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
        staticRenderer.render(spaces);
        shader.stop();
        camera.update();

        for(int i = 0; i < spaces.values().stream().toList().size(); i++){
            Space space = spaces.values().stream().toList().get(i);
            if(space instanceof GuiBase){
                GuiBase gui = (GuiBase) space;

                gui.onPrepare(this);
            }
        }

        guiRenderer.render(spaces.values().stream().toList());

        window.update();
        spaces.clear();
    }

    public void processSpace(Space space){
        spaces.put(space.name, space);
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
