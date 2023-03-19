package io.github.octglam.uniaengine.imguis;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import io.github.octglam.uniaengine.inputs.Input;
import io.github.octglam.uniaengine.spaces.Space;
import io.github.octglam.uniaengine.spaces.threeD.Space3D;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class ImGuiLayer {
    private static final Logger LOGGER = LoggerFactory.getLogger("ImGuiLayer");
    private boolean showText = false;

    private Space selectedSpace;
    private HashMap<String, Object> selectedSpaceData = new HashMap<>();

    public ImGuiLayer(){
        LOGGER.info("ImGuiLayer Initialized!");
    }

    private void setSelectedSpaceData(){
        for(String name : selectedSpaceData.keySet()) {
            Object value = selectedSpaceData.get(name);

            if(value instanceof ImString){
                selectedSpace.hierarchyData.put(name, ((ImString)(value)).get());
            } else if(value instanceof ImFloat){
                selectedSpace.hierarchyData.put(name, ((ImFloat)(value)).get());
            } else if(value instanceof ImInt){
                selectedSpace.hierarchyData.put(name, ((ImInt)(value)).get());
            }
        }
    }

    private void spaceData(){
        if(selectedSpace == null) return;

        for(String name : selectedSpaceData.keySet()){
            Object value = selectedSpaceData.get(name);

            ImGui.text(name + " : ");

            if(value instanceof ImString){
                ImGui.inputText(name, (ImString) value, ImGuiInputTextFlags.AlwaysOverwrite);
            } else if(value instanceof ImFloat){
                ImGui.inputFloat(name, (ImFloat) value, ImGuiInputTextFlags.AlwaysOverwrite);
            } else if(value instanceof ImInt){
                ImGui.inputInt(name, (ImInt) value, ImGuiInputTextFlags.AlwaysOverwrite);
            }
        }

        setSelectedSpaceData();
    }

    public void imgui(){
        ImGui.begin("Hierarchy");

        spaceData();

        ImGui.separator();

        if(ImGui.button("I am a button")){
            showText = true;
        }

        if(showText){
            ImGui.text("You clicked a button");
            ImGui.sameLine();
            if(ImGui.button("Stop showing text")){
                showText = false;
            }
        }

        ImGui.end();
    }

    public void selectSpace(Space space){
        selectedSpace = space;

        HashMap<String, Object> dummyDataMap = new HashMap<>();
        for(String name : space.hierarchyData.keySet()){
            Object value = space.hierarchyData.get(name);

            if(value instanceof String){
                ImString dummyString = new ImString("                                                     ");
                dummyString.set(value);
                dummyDataMap.put(name, dummyString);
            } else if(value instanceof Float){
                dummyDataMap.put(name, new ImFloat((Float) value));
            } else if(value instanceof Integer){
                dummyDataMap.put(name, new ImInt((Integer) value));
            }
        }
        selectedSpaceData = dummyDataMap;
    }
}
