package io.github.octglam.uniaengine.spaces;

import io.github.octglam.uniaengine.imguis.ImGuiLayer;
import io.github.octglam.uniaengine.scenes.AbstractScene;
import io.github.octglam.uniaengine.spaces.threeD.Space3D;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Space {
    public String name;
    public Space parent;
    public AbstractScene scene;

    private HashMap<String, Space> children = new HashMap<>();

    public LinkedHashMap<String, Object> hierarchyData = new LinkedHashMap<>();

    public Space(String name){
        this.name = name;
    }

    public void giveData(){
        hierarchyData.put("name", name);
    }

    public void linkHierarchyData() {
        this.name = (String) hierarchyData.get("name");
    }

    public void addChild(Space child){
        child.parent = this;
        children.put(child.name, child);
    }

    public HashMap<String, Space> getChildren(){
        return children;
    }
}
