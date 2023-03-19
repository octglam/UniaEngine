package io.github.octglam.uniaengine.spaces;

import io.github.octglam.uniaengine.imguis.ImGuiLayer;
import io.github.octglam.uniaengine.spaces.threeD.Space3D;

import java.util.HashMap;

public class Space {
    public String name;
    public Space parent;

    private HashMap<String, Space> children = new HashMap<>();

    public HashMap<String, Object> hierarchyData = new HashMap<>();

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
