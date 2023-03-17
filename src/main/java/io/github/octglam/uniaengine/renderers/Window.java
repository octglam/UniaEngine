package io.github.octglam.uniaengine.renderers;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import io.github.octglam.uniaengine.imguis.ImGuiLayer;
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
    public ImGuiLayer imguiLayer;

    public Input input;

    public Vector4f rgba = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    private final Matrix4f projectionMatrix;

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private String glslVersion = null;

    public Window(int width, int height, String title, ImGuiLayer layer){
        imguiLayer = layer;
        Window.width = width;
        Window.height = height;
        this.title = title;
        projectionMatrix = new Matrix4f();
    }

    public Window(int width, int height, String title){
        Window.width = width;
        Window.height = height;
        this.title = title;
        projectionMatrix = new Matrix4f();
    }

    private void initWindow(){
        GLFWErrorCallback.createPrint().set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize glfw");
        }

        glslVersion = "#version 330";

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
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

    private void initImGui(){
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        //io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
    }

    public void init(){
        initWindow();
        if(imguiLayer != null) {
            initImGui();

            imGuiGlfw.init(handle, true);
            imGuiGl3.init(glslVersion);
        }
    }

    public boolean shouldClose(){
        return glfwWindowShouldClose(handle);
    }

    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(rgba.x, rgba.y, rgba.z, rgba.w);
        input.update();

        if(imguiLayer != null) {
            imGuiGlfw.newFrame();
            ImGui.newFrame();

            imguiLayer.imgui();
        }
    }

    public void update(){
        if(imguiLayer != null) {
            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowHandle = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupWindowHandle);
            }
        }

        glfwSwapBuffers(handle);
        glfwPollEvents();
    }

    public void destroy(){
        if(imguiLayer != null) {
            imGuiGl3.dispose();
            imGuiGlfw.dispose();
            ImGui.destroyContext();
        }

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
