package io.github.octglam.uniaengine.scenes;

import io.github.octglam.uniaengine.renderers.MasterRenderer;
import io.github.octglam.uniaengine.spaces.Space;
import io.github.octglam.uniaengine.spaces.threeD.Camera;
import io.github.octglam.uniaengine.spaces.threeD.Space3D;
import io.github.octglam.uniaengine.spaces.threeD.SunLight;
import io.github.octglam.uniaengine.spaces.guis.GuiBase;

import java.util.HashMap;

public abstract class AbstractScene {
    public String name;
    public SunLight sun;
    public Camera camera;

    public HashMap<String, Space> spaces = new HashMap<>();

    private MasterRenderer renderer;

    public AbstractScene(String name, MasterRenderer masterRenderer, SunLight sun, Camera camera){
        renderer = masterRenderer;
        this.name = name;
        this.sun = sun;
        this.camera = camera;
    }

    public void update(){
        onUpdate();

        for(String spaceName : spaces.keySet()){
            Space space = spaces.get(spaceName);

            renderer.processSpace(space);
        }

        renderer.render(sun, camera);
    }

    public void addSpace(Space space) {
        spaces.put(space.name, space);
        space.scene = this;
    }

    public abstract void onUpdate();
}
