package io.github.octglam.uniaengine.spaces.threeD;

import org.joml.Vector3f;

import java.util.HashMap;

public class Space {
    public String name;
    public Vector3f position, prevPosition, positionD, rotation, scale;
    public Space parent;

    private HashMap<String, Space> children = new HashMap<>();

    public Space(String name, Vector3f position, Vector3f rotation, Vector3f scale){
        this.name = name;
        this.position = position;
        this.prevPosition = position;
        this.positionD = new Vector3f();
        this.rotation = rotation;
        this.scale = scale;
    }

    public void update(){
        positionD.x = position.x-prevPosition.x;
        positionD.y = position.y-prevPosition.y;
        positionD.z = position.z-prevPosition.z;

        //update

        for(String childname : children.keySet()){
            Space child = children.get(childname);
            child.position.x+=positionD.x;
            child.position.y+=positionD.y;
            child.position.z+=positionD.z;
        }
        prevPosition = position;
    }

    public void addChild(Space child){
        child.parent = this;
        children.put(child.name, child);
    }

    public HashMap<String, Space> getChildren(){
        return children;
    }
}
