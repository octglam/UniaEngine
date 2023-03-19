package io.github.octglam.uniaengine.spaces.threeD.physics.collisions;

import org.joml.Vector3f;

public class AABB {
    public Vector3f center, half_extent;

    public AABB(Vector3f center, Vector3f half_extent){
        this.center = center;
        this.half_extent = half_extent;
    }

    public Collision isIntersecting(AABB box2){
        Vector3f distance = box2.center.sub(center, new Vector3f());
        distance.x = Math.abs(distance.x);
        distance.y = Math.abs(distance.y);
        distance.z = Math.abs(distance.z);

        distance.sub(half_extent.add(box2.half_extent, new Vector3f()));

        return new Collision(distance, distance.x < 0 && distance.y < 0 && distance.z < 0);
    }
}
