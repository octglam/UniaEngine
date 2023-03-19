package io.github.octglam.uniaengine.spaces.guis;

import io.github.octglam.uniaengine.renderers.Loader;
import io.github.octglam.uniaengine.renderers.MasterRenderer;
import io.github.octglam.uniaengine.spaces.Space;
import io.github.octglam.uniaengine.utils.maths.Maths;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.HashMap;

public class GuiBase extends Space {
    public int texture;
    public Vector2f position, prevPosition, positionD, scale;
    public IEvent iEvent;
    public int zIndex = 0;

    private boolean isVisible = true;

    public Vector4f colour = new Vector4f(1,1,1,1);

    private Loader loader;

    public GuiBase(String name, int texture, Vector2f position, Vector2f scale, IEvent iEvent, Loader loader){
        super(name);
        this.name = name;
        this.loader = loader;
        if(texture != -999) this.texture = texture;
        else this.texture = loader.loadTexture("res/textures/white.png");
        this.position = position;
        this.prevPosition = position;
        this.positionD = new Vector2f();
        this.scale = scale;
        this.iEvent = iEvent;
    }

    public boolean contains(Vector2f v) { return v.x > position.x - scale.x && v.x < position.x + scale.x
            && v.y > position.y - scale.y && v.y < position.y + scale.y; }

    public void updateMouseEntered(){
        Vector2f mpos = Maths.getNormalizedMousePosition();

        if(contains(new Vector2f(mpos.x, mpos.y))){
            if(iEvent != null) iEvent.onMouseEntered(this);
            onMouseEntered();
        } else {
            if(iEvent != null) iEvent.onMouseExited(this);
            onMouseExited();
        }
    }

    public void update(){
        positionD.x = position.x-prevPosition.x;
        positionD.y = position.y-prevPosition.y;
        updateMouseEntered();
        onUpdate();

        HashMap<String, Space> children = getChildren();
        for(String childname : children.keySet()){
            Space bchild = children.get(childname);
            if(bchild instanceof GuiBase) {
                GuiBase child = (GuiBase) bchild;
                child.zIndex = child.zIndex + zIndex + 1;
                child.position.x += positionD.x;
                child.position.y += positionD.y;
            }
        }
        prevPosition = position;
    }

    public void onMouseEntered() {}
    public void onMouseExited() {}
    public void onUpdate(){}
    public void onPrepare(MasterRenderer renderer){
        HashMap<String, Space> children = getChildren();
        for(String childname : children.keySet()){
            Space bchild = children.get(childname);
            if(bchild instanceof GuiBase) {
                GuiBase child = (GuiBase) bchild;
                renderer.processSpace(child);
                child.onPrepare(renderer);
            }
        }
    }
    public void onRender(){}

    public void setVisible(boolean visible){
        isVisible = visible;
        if(visible){
            colour.w = 1.0f;
        } else {
            colour.w = 0.0f;
        }
    }

    public boolean isVisible(){
        return isVisible;
    }

    public interface IEvent {
        void onMouseEntered(GuiBase gui);
        void onMouseExited(GuiBase gui);
    }
}
