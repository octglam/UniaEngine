package io.github.octglam.uniaengine.spaces.threeD;

import io.github.octglam.uniaengine.spaces.Space;
import org.joml.Vector3f;

import java.util.HashMap;

public class Space3D extends Space {
    public Vector3f position, prevPosition, positionD, rotation, scale;

    public Space3D(String name, Vector3f position, Vector3f rotation, Vector3f scale){
        super(name);
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
        HashMap<String, Space> children = getChildren();
        for(String childname : children.keySet()){
            Space bchild = children.get(childname);
            if(bchild instanceof Space3D) {
                Space3D child = (Space3D) children.get(childname);
                child.position.x += positionD.x;
                child.position.y += positionD.y;
                child.position.z += positionD.z;
            }
        }
        prevPosition = position;
    }

    @Override
    public void giveData() {
        super.giveData();
        hierarchyData.put("Position", position);
    }

    @Override
    public void linkHierarchyData() {
        super.linkHierarchyData();
        position = (Vector3f) hierarchyData.get("Position");
    }
}
