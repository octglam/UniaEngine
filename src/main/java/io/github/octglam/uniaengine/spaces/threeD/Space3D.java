package io.github.octglam.uniaengine.spaces.threeD;

import io.github.octglam.uniaengine.spaces.Space;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Objects;

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

    private void updateChildren(HashMap<String, Space> children){
        if(!children.isEmpty()) {
            for (String childname : children.keySet()) {
                Space bchild = children.get(childname);
                if (bchild instanceof Space3D child) {
                    child.position.x += positionD.x;
                    child.position.y += positionD.y;
                    child.position.z += positionD.z;
                    scene.addSpace(child);
                    if(!child.getChildren().isEmpty()){
                        updateChildren(child.getChildren());
                    }
                }
            }
        }
    }

    public void update(){
        positionD.x = position.x-prevPosition.x;
        positionD.y = position.y-prevPosition.y;
        positionD.z = position.z-prevPosition.z;

        //update
        updateChildren(getChildren());

        prevPosition.set(position);
    }

    @Override
    public void giveData() {
        super.giveData();
        hierarchyData.put("position", position);
        hierarchyData.put("rotation", rotation);
        hierarchyData.put("scale", scale);
    }

    @Override
    public void linkHierarchyData() {
        super.linkHierarchyData();
        position = (Vector3f) hierarchyData.get("position");
        rotation = (Vector3f) hierarchyData.get("rotation");
        scale = (Vector3f) hierarchyData.get("scale");
    }
}
