package io.github.octglam.uniaengine.spaces.guis.widgets;

import io.github.octglam.uniaengine.inputs.Input;
import io.github.octglam.uniaengine.renderers.Loader;
import io.github.octglam.uniaengine.renderers.MasterRenderer;
import io.github.octglam.uniaengine.spaces.guis.GuiBase;
import io.github.octglam.uniaengine.utils.EngineVars;
import io.github.octglam.uniaengine.utils.Maths;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class WinWidget extends GuiBase {
    private Button edge;
    private Button hiddenbtn;
    private MasterRenderer renderer;
    private boolean isDragged = false;
    private boolean isEnteredEdgeBtn = false;
    private Vector2f dragOffset = new Vector2f();

    public boolean canProcessInEditor = false;

    public WinWidget(String name, Vector2f position, Vector2f scale, IEvent iEvent, Loader loader) {
        super(name, -999, position, scale, iEvent, loader);

        hiddenbtn = new Button(name+"_hiddenbtn", loader.loadTexture("res/textures/widgets/hiddenbtn.png"), new Vector2f(position.x+scale.x-0.03f, position.y+scale.y), new Vector2f(0.018f, 0.03f),
                new Button.IButtonEvent() {
                    public void toVisible(GuiBase gui){
                        if(EngineVars.isEditor && !canProcessInEditor) return;

                        setVisible(!isVisible());
                        for(GuiBase child : getChildren().values()){
                            if(child.equals(edge) || child.equals(gui)) continue;
                            child.setVisible(isVisible());
                        }
                    }

                    @Override
                    public void onClick(GuiBase gui) {
                        gui.colour = new Vector4f(0.8f, 0.8f, 0.8f, gui.colour.w);
                    }

                    @Override
                    public void onUnPressed(GuiBase gui) {
                        gui.colour = new Vector4f(1f, 1f, 1f, gui.colour.w);
                        toVisible(gui);
                        isEnteredEdgeBtn = false;
                    }

                    @Override
                    public void onMouseEntered(GuiBase gui) {
                        gui.colour = new Vector4f(1.2f, 1.2f, 1.2f, gui.colour.w);
                        if(!isEnteredEdgeBtn) isEnteredEdgeBtn = true;
                    }

                    @Override
                    public void onMouseExited(GuiBase gui) {
                        gui.colour = new Vector4f(1f, 1f, 1f, gui.colour.w);
                        Button btn = (Button) gui;
                        if(!btn.isPressed) isEnteredEdgeBtn = false;
                        if(!Input.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT) && btn.isPressed){
                            btn.isPressed = false;
                            btn.onUnPressed();
                        }
                    }
                }, loader);
        hiddenbtn.pressAppearance = new ArrayList<>();
        hiddenbtn.pressAppearance.add(0, new Vector3f(1.2f, 1.2f, 1.2f));    // MouseEntered
        hiddenbtn.pressAppearance.add(1, new Vector3f(1f, 1f, 1f));          // ButtonUnPressed & MouseExited
        hiddenbtn.pressAppearance.add(2, new Vector3f(0.8f, 0.8f, 0.8f));    // ButtonPressed
        addChild(hiddenbtn);

        edge = new Button(name+"_edge", -999, new Vector2f(position.x, position.y+scale.y), new Vector2f(scale.x, 0.04f),
                new Button.IButtonEvent() {
                    @Override
                    public void onClick(GuiBase gui) {
                        if(isEnteredEdgeBtn) return;
                        if(EngineVars.isEditor && !canProcessInEditor) return;

                        isDragged = true;
                        dragOffset = Maths.getNormalizedMousePosition();
                        GuiBase win = renderer.renderGuis.get(name);
                        win.position = new Vector2f(dragOffset.x, dragOffset.y - scale.y);
                    }

                    @Override
                    public void onUnPressed(GuiBase gui) {
                        isDragged = false;
                    }

                    @Override
                    public void onMouseEntered(GuiBase gui) {
                    }

                    @Override
                    public void onMouseExited(GuiBase gui) {
                    }
                }, loader);
        addChild(edge);
    }

    @Override
    public void onPrepare(MasterRenderer renderer) {
        super.onPrepare(renderer);

        edge.zIndex = zIndex+1;
        this.renderer = renderer;
        renderer.processGui(edge);
    }
}
