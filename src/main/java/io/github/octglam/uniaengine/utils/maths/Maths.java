package io.github.octglam.uniaengine.utils.maths;

import io.github.octglam.uniaengine.inputs.Input;
import io.github.octglam.uniaengine.renderers.Window;
import org.joml.Vector2f;

public class Maths {
    public static Vector2f getNormalizedMousePosition(){
        double mx = Input.getMouseX();
        double my = Input.getMouseY();
        double normalizedX = -1.0 + 2.0 * mx / Window.width;
        double normalizedY = 1.0 - 2.0 * my / Window.height;
        return new Vector2f((float) normalizedX, (float) normalizedY);
    }
}
