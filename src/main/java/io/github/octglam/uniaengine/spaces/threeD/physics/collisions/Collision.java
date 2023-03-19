package io.github.octglam.uniaengine.spaces.threeD.physics.collisions;

import org.joml.Vector3f;

public class Collision {
    public Vector3f distance;
    public boolean isIntersecting;

    public Collision(Vector3f distance, boolean intersects){
        this.distance = distance;
        this.isIntersecting = intersects;
    }
}
