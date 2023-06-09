package io.github.octglam.uniaengine;

import io.github.octglam.uniaengine.imguis.ImGuiLayer;
import io.github.octglam.uniaengine.scenes.AbstractScene;
import io.github.octglam.uniaengine.scenes.Scene;
import io.github.octglam.uniaengine.spaces.guis.widgets.Button;
import io.github.octglam.uniaengine.models.TexturedModel;
import io.github.octglam.uniaengine.renderers.Loader;
import io.github.octglam.uniaengine.renderers.MasterRenderer;
import io.github.octglam.uniaengine.renderers.Window;
import io.github.octglam.uniaengine.spaces.threeD.Camera;
import io.github.octglam.uniaengine.spaces.threeD.SunLight;
import io.github.octglam.uniaengine.spaces.threeD.Model;
import io.github.octglam.uniaengine.spaces.guis.widgets.WinWidget;
import io.github.octglam.uniaengine.spaces.threeD.physics.collisions.AABB;
import io.github.octglam.uniaengine.spaces.threeD.terrains.Terrain;
import io.github.octglam.uniaengine.textures.ModelTexture;
import io.github.octglam.uniaengine.utils.EngineVars;
import io.github.octglam.uniaengine.utils.ModelLoader;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class UniaEngine implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger("UniaEngine");
    private final Thread gameThread = new Thread(this, "UniaEngine");

    public String[] args;
    public Window window = new Window(1480, 800, "UniaEngine", new ImGuiLayer());
    public Loader loader = new Loader();
    public MasterRenderer masterRenderer = new MasterRenderer(window, loader);

    public UniaEngine(String[] args){
        this.args = args;
        gameThread.start();
    }

    public void run() {
        LOGGER.info("UniaEngine Initializing...");

        EngineVars.isEditor = true;

        window.init();
        masterRenderer.init();

        // scenes
        AbstractScene scene = new Scene("test", masterRenderer, new SunLight("Sun", new Vector3f(0, 100, -20), new Vector3f(1,1,1)),
                new Camera("EditorCamera", new Vector3f(0,0,2), new Vector3f(0,0,0)));
        scene.camera.canMove = true;

        // spaces
        TexturedModel robotModel = new TexturedModel(ModelLoader.loadModel("res/models/medical_robot/scene.gltf", loader),
                new ModelTexture(loader.loadTexture("res/textures/medical_robot/medbot.003_baseColor.png")));
        robotModel.texture.shineDamper = 50;
        robotModel.texture.reflectivity = 5;

        Model space = new Model("robot", robotModel, new Vector3f(0,0,-25), new Vector3f(0, 0, 0), new Vector3f(1,1,1));

        Model space2 = new Model("robot2", robotModel, new Vector3f(0,0,-24.5f), new Vector3f(0, 0, 0), new Vector3f(1,1,1));

        Terrain terrain = new Terrain("terrain1", new Vector3f(-1f, -1f, -1f), new Vector3f(0,0,0), new Vector3f(1,1,1),
                loader, new ModelTexture(loader.loadTexture("res/textures/grass.png")), 800f);
        Terrain terrain2 = new Terrain("terrain2", new Vector3f(0f, -1f, -1f), new Vector3f(0,0,0), new Vector3f(1,1,1),
                loader, new ModelTexture(loader.loadTexture("res/textures/grass.png")), 800f);
        terrain.generateHeightMap = true;
        terrain2.generateHeightMap = true;
        terrain.generateTerrain();
        terrain2.generateTerrain();

        // guis
        Button testBtn = new Button("btn", -999, new Vector2f(-0.7f, 0f), new Vector2f(0.1f, 0.2f),
                null, loader);
        testBtn.pressAppearance = new ArrayList<>();
        testBtn.pressAppearance.add(0, new Vector3f(0.3f, 0.3f, 0.3f));    // MouseEntered
        testBtn.pressAppearance.add(1, new Vector3f(0.2f, 0.2f, 0.2f));    // ButtonUnPressed & MouseExited
        testBtn.pressAppearance.add(2, new Vector3f(0.17f, 0.17f, 0.17f)); // ButtonPressed
        testBtn.colour = new Vector4f(0.2f, 0.2f, 0.2f, 1.0f);

        WinWidget hierarchy = new WinWidget("Hierarchy", new Vector2f(-0.8f, 0f), new Vector2f(0.2f, 1f),
                null, loader);
        hierarchy.zIndex = 999;
        hierarchy.colour = new Vector4f(0.3f, 0.3f, 0.3f, 1.0f);
        hierarchy.addChild(testBtn);

        scene.addSpace(hierarchy);
        scene.addSpace(space);
        scene.addSpace(space2);
        scene.addSpace(terrain);
        scene.addSpace(terrain2);

        window.imguiLayer.selectSpace(space);

        LOGGER.info("Initialized!");

        AABB box1 = new AABB(new Vector3f(0,0,0), new Vector3f(1,1,1));
        AABB box2 = new AABB(new Vector3f(1,0,0), new Vector3f(1,1,1));
        if(box1.isIntersecting(box2).isIntersecting)
            System.out.println("test");

        while(!window.shouldClose()){
            scene.update();
        }

        LOGGER.info("Clean upping...");

        loader.cleanUp();
        masterRenderer.cleanUp();
    }
}
