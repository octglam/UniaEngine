package io.github.octglam.uniaengine.spaces.threeD;

import org.joml.Vector3f;

public class SunLight extends Space {
    public Vector3f colour;

    public SunLight(String name, Vector3f position, Vector3f colour) {
        super(name, position, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
        this.colour = colour;
    }
}
