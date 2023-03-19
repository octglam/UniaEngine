package io.github.octglam.uniaengine.spaces.threeD;

import io.github.octglam.uniaengine.inputs.Input;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera extends Space3D {
    public float FOV = 70f;
    public float NEAR_PLANE = 0.1f;
    public float FAR_PLANE = 1000f;
    public float mouseSensitivity = 0.05f;
    public float moveSpeed = 0.1f;
    public float runSpeed = moveSpeed*5f;

    public float currentSpeed = moveSpeed;

    public boolean canMove = false;

    public Camera(String name, Vector3f position, Vector3f rotation) {
        super(name, position, rotation, new Vector3f(1, 1, 1));
    }

    @Override
    public void update() {
        if(Input.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT) && canMove){
            Input.setXXMode(GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

            rotation.y+=Input.getMouseDX()*mouseSensitivity;
            rotation.x+=Input.getMouseDY()*mouseSensitivity;

            if(Input.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)){
                currentSpeed = runSpeed;
            } else {
                currentSpeed = moveSpeed;
            }

            if(Input.isKeyPressed(GLFW.GLFW_KEY_W)){
                position.z-=Math.cos(Math.toRadians(rotation.y)) * currentSpeed;
                position.x+=Math.sin(Math.toRadians(rotation.y)) * currentSpeed;
                position.y-=Math.sin(Math.toRadians(rotation.x)) * currentSpeed;
            }
            if(Input.isKeyPressed(GLFW.GLFW_KEY_S)){
                position.z-=Math.cos(Math.toRadians(rotation.y)) * -currentSpeed;
                position.x+=Math.sin(Math.toRadians(rotation.y)) * -currentSpeed;
                position.y-=Math.sin(Math.toRadians(rotation.x)) * -currentSpeed;
            }
            if(Input.isKeyPressed(GLFW.GLFW_KEY_A)){
                position.z-=Math.sin(Math.toRadians(rotation.y)) * currentSpeed;
                position.x-=Math.cos(Math.toRadians(rotation.y)) * currentSpeed;
            }
            if(Input.isKeyPressed(GLFW.GLFW_KEY_D)){
                position.z+=Math.sin(Math.toRadians(rotation.y)) * currentSpeed;
                position.x+=Math.cos(Math.toRadians(rotation.y)) * currentSpeed;
            }
        } else {
            Input.setXXMode(GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }
}
