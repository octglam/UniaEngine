package io.github.octglam.uniaengine.utils.maths;

import io.github.octglam.uniaengine.spaces.threeD.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transformation {
    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale){
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(new Vector3f(translation.x, translation.y, 0)).
                scale(new Vector3f(scale.x, scale.y, 1f));
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale){
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(translation).
                rotateX((float) Math.toRadians(rotation.x)).
                rotateY((float) Math.toRadians(rotation.y)).
                rotateZ((float) Math.toRadians(rotation.z)).
                scale(scale);
        return matrix;
    }

    public static Matrix4f getViewMatrix(Camera camera){
        Vector3f pos = camera.position;
        Vector3f rot = camera.rotation;
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.rotate((float) Math.toRadians(rot.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rot.y), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(rot.z), new Vector3f(0, 0, 1));
        matrix.translate(-pos.x, -pos.y, -pos.z);
        return matrix;
    }
}
