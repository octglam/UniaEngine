package io.github.octglam.uniaengine.scenes;

import io.github.octglam.uniaengine.renderers.MasterRenderer;
import io.github.octglam.uniaengine.spaces.threeD.Camera;
import io.github.octglam.uniaengine.spaces.threeD.SunLight;

public class Scene extends AbstractScene {
    public Scene(String name, MasterRenderer masterRenderer, SunLight sun, Camera camera){
        super(name, masterRenderer, sun, camera);
    }

    @Override
    public void onUpdate(){
    }
}
