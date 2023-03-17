package io.github.octglam.uniaengine;

import io.github.octglam.uniaengine.scenes.AbstractScene;
import io.github.octglam.uniaengine.scenes.Scene;
import io.github.octglam.uniaengine.spaces.guis.widgets.Button;
import io.github.octglam.uniaengine.spaces.guis.GuiBase;
import io.github.octglam.uniaengine.models.TexturedModel;
import io.github.octglam.uniaengine.renderers.Loader;
import io.github.octglam.uniaengine.renderers.MasterRenderer;
import io.github.octglam.uniaengine.renderers.Window;
import io.github.octglam.uniaengine.spaces.Camera;
import io.github.octglam.uniaengine.spaces.SunLight;
import io.github.octglam.uniaengine.spaces.Model;
import io.github.octglam.uniaengine.spaces.guis.widgets.WinWidget;
import io.github.octglam.uniaengine.spaces.terrains.Terrain;
import io.github.octglam.uniaengine.textures.ModelTexture;
import io.github.octglam.uniaengine.utils.ModelLoader;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Collections;

public class UniaEngine implements Runnable {
    private final Thread gameThread = new Thread(this, "UniaEngine");

    public String[] args;
    public Window window = new Window(1480, 800, "UniaEngine");
    public Loader loader = new Loader();
    public MasterRenderer masterRenderer = new MasterRenderer(window, loader);

    public UniaEngine(String[] args){
        this.args = args;
        gameThread.start();
    }

    public void run() {
        window.init();
        masterRenderer.init();

        // scenes
        AbstractScene scene = new Scene("test", masterRenderer, new SunLight("Sun", new Vector3f(0, 500, -20), new Vector3f(1,1,1)),
                new Camera("EditorCamera", new Vector3f(0,0,2), new Vector3f(0,0,0)));
        scene.camera.canMove = true;

        // spaces
        TexturedModel swordModel = new TexturedModel(ModelLoader.loadModel("res/models/sword/sword.gltf", loader),
                new ModelTexture(loader.loadTexture("res/textures/sword/Object001_mtl_baseColor.jpeg")));
        TexturedModel robotModel = new TexturedModel(ModelLoader.loadModel("res/models/medical_robot/scene.gltf", loader),
                new ModelTexture(loader.loadTexture("res/textures/medical_robot/medbot.003_baseColor.png")));
        robotModel.texture.shineDamper = 50;
        robotModel.texture.reflectivity = 5;

        Model space = new Model("robot", robotModel, new Vector3f(0,0,-25), new Vector3f(0, 0, 0), new Vector3f(1,1,1));
        Model sword = new Model("sword", swordModel, new Vector3f(0,0,-25), new Vector3f(0, 0, 0), new Vector3f(1,1,1));

        Terrain terrain = new Terrain("terrain1", new Vector3f(-1f, -1f, -1f), new Vector3f(0,0,0), new Vector3f(1,1,1),
                loader, new ModelTexture(loader.loadTexture("res/textures/grass.png")), 800f);
        Terrain terrain2 = new Terrain("terrain2", new Vector3f(0f, -1f, -1f), new Vector3f(0,0,0), new Vector3f(1,1,1),
                loader, new ModelTexture(loader.loadTexture("res/textures/grass.png")), 800f);

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

        scene.addGui(hierarchy);
        scene.addSpace(space);
        scene.addSpace(sword);
        scene.addSpace(terrain);
        scene.addSpace(terrain2);

        while(!window.shouldClose()){
            space.rotation.add(new Vector3f(0,1,0));
            space.position.z+=0.02f;

            scene.update();
        }

        loader.cleanUp();
        masterRenderer.cleanUp();
    }
}