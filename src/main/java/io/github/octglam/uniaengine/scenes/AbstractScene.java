package io.github.octglam.uniaengine.scenes;

import io.github.octglam.uniaengine.renderers.MasterRenderer;
import io.github.octglam.uniaengine.spaces.Camera;
import io.github.octglam.uniaengine.spaces.Space;
import io.github.octglam.uniaengine.spaces.SunLight;
import io.github.octglam.uniaengine.spaces.guis.GuiBase;

import java.util.HashMap;

public abstract class AbstractScene {
    public String name;
    public SunLight sun;
    public Camera camera;

    public HashMap<String, Space> spaces = new HashMap<>();
    public HashMap<String, GuiBase> guis = new HashMap<>();

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

        for(String guiName : guis.keySet()){
            GuiBase gui = guis.get(guiName);

            renderer.processGui(gui);
        }

        renderer.render(sun, camera);
    }

    public void addSpace(Space space) {
        spaces.put(space.name, space);
    }
    public void addGui(GuiBase gui) {
        guis.put(gui.name, gui);
    }

    public abstract void onUpdate();
}