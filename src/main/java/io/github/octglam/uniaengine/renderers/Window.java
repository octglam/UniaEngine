package io.github.octglam.uniaengine.renderers;

import io.github.octglam.uniaengine.inputs.Input;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    public long handle;
    public static int width, height;
    public String title;

    public Input input;

    public Vector4f rgba = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    private final Matrix4f projectionMatrix;

    public Window(int width, int height, String title){
        Window.width = width;
        Window.height = height;
        this.title = title;
        projectionMatrix = new Matrix4f();
    }

    public void init(){
        GLFWErrorCallback.createPrint().set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize glfw");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        handle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (handle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        input = new Input(handle);
        GLFW.glfwSetKeyCallback(handle, input.getKeyboardCallback());
        GLFW.glfwSetCursorPosCallback(handle, input.getMouseMoveCallback());
        GLFW.glfwSetMouseButtonCallback(handle, input.getMouseButtonsCallback());

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            glfwGetWindowSize(handle, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            assert vidmode != null;
            glfwSetWindowPos(
                    handle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(handle);
        glfwSwapInterval(1);

        glfwShowWindow(handle);

        GL.createCapabilities();

        glClearColor(rgba.x, rgba.y, rgba.z, rgba.w);
    }

    public boolean shouldClose(){
        return glfwWindowShouldClose(handle);
    }

    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(rgba.x, rgba.y, rgba.z, rgba.w);
        input.update();
    }

    public void update(){
        glfwSwapBuffers(handle);
        glfwPollEvents();
    }

    public void destroy(){
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }

    public Matrix4f updateProjectionMatrix(float FOV, float NEAR_PLANE, float FAR_PLANE){
        float aspectRatio = (float) width / height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, NEAR_PLANE, FAR_PLANE);
    }
}
