package io.github.octglam.uniaengine.spaces.guis.widgets;

import io.github.octglam.uniaengine.inputs.Input;
import io.github.octglam.uniaengine.renderers.Loader;
import io.github.octglam.uniaengine.spaces.guis.GuiBase;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Button extends GuiBase {
    public boolean isPressed = false;

    public List<Vector3f> pressAppearance = null;

    public Button(String name, int texture, Vector2f position, Vector2f scale, IButtonEvent iEvent, Loader loader) {
        super(name, texture, position, scale, iEvent, loader);
    }

    public void onClick() {}
    public void onUnPressed() {}

    @Override
    public void onMouseEntered() {
        if(pressAppearance != null) colour.set(pressAppearance.get(0).x, pressAppearance.get(0).y, pressAppearance.get(0).z, colour.w);

        IButtonEvent iEvent1 = null;
        if(iEvent != null) iEvent1 = (IButtonEvent) iEvent;
        if(Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)){
            isPressed = true;
            if(iEvent != null) iEvent1.onClick(this);
            onClick();

            if(pressAppearance != null) colour.set(pressAppearance.get(2).x, pressAppearance.get(2).y, pressAppearance.get(2).z, colour.w);
        } else if(isPressed && !Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            isPressed = false;
            if(iEvent != null) iEvent1.onUnPressed(this);
            onUnPressed();

            if(pressAppearance != null) colour.set(pressAppearance.get(1).x, pressAppearance.get(1).y, pressAppearance.get(1).z, colour.w);
        }
    }

    @Override
    public void onMouseExited() {
        if(pressAppearance != null) colour.set(pressAppearance.get(1).x, pressAppearance.get(1).y, pressAppearance.get(1).z, colour.w);
    }

    @Override
    public void onUpdate() {
        IButtonEvent iEvent1 = (IButtonEvent) iEvent;
        if(isPressed){
            if(iEvent != null) iEvent1.onClick(this);
            onClick();
        }
    }

    public interface IButtonEvent extends IEvent {
        void onClick(GuiBase gui);
        void onUnPressed(GuiBase gui);
    }
}
