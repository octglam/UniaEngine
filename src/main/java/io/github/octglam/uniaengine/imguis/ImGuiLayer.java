package io.github.octglam.uniaengine.imguis;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImFloat;
import imgui.type.ImString;
import io.github.octglam.uniaengine.spaces.Space;
import org.joml.Vector3f;

public class ImGuiLayer {
    private boolean showText = false;

    private Space selectedSpace;
    private ImString selectedSpaceName = new ImString("                                                                                                                                ");
    private ImFloat[] selectedSpacePos = new ImFloat[3];
    private ImFloat[] selectedSpaceRot = new ImFloat[3];
    private ImFloat[] selectedSpaceScale = new ImFloat[3];

    public ImGuiLayer(){
        selectedSpacePos[0] = new ImFloat(0.0f);
        selectedSpacePos[1] = new ImFloat(0.0f);
        selectedSpacePos[2] = new ImFloat(0.0f);
        selectedSpaceRot[0] = new ImFloat(0.0f);
        selectedSpaceRot[1] = new ImFloat(0.0f);
        selectedSpaceRot[2] = new ImFloat(0.0f);
        selectedSpaceScale[0] = new ImFloat(0.0f);
        selectedSpaceScale[1] = new ImFloat(0.0f);
        selectedSpaceScale[2] = new ImFloat(0.0f);
    }

    private void setSelectedSpaceData(){
        selectedSpace.name = selectedSpaceName.get();
        selectedSpace.position = new Vector3f(selectedSpacePos[0].get(),selectedSpacePos[1].get(),selectedSpacePos[2].get());
        selectedSpace.rotation = new Vector3f(selectedSpaceRot[0].get(),selectedSpaceRot[1].get(),selectedSpaceRot[2].get());
        selectedSpace.scale = new Vector3f(selectedSpaceScale[0].get(),selectedSpaceScale[1].get(),selectedSpaceScale[2].get());
    }

    private void spacePosRotScale(){
        ImGui.text("Name : ");
        ImGui.inputText("String", selectedSpaceName, ImGuiInputTextFlags.AlwaysOverwrite);

        ImGui.separator();

        ImGui.text("Position : ");
        ImGui.inputFloat("X", selectedSpacePos[0], ImGuiInputTextFlags.AlwaysOverwrite);
        ImGui.inputFloat("Y", selectedSpacePos[1], ImGuiInputTextFlags.AlwaysOverwrite);
        ImGui.inputFloat("Z", selectedSpacePos[2], ImGuiInputTextFlags.AlwaysOverwrite);

        ImGui.separator();

        ImGui.text("Rotation : ");
        ImGui.inputFloat("rX", selectedSpaceRot[0], ImGuiInputTextFlags.AlwaysOverwrite);
        ImGui.inputFloat("rY", selectedSpaceRot[1], ImGuiInputTextFlags.AlwaysOverwrite);
        ImGui.inputFloat("rZ", selectedSpaceRot[2], ImGuiInputTextFlags.AlwaysOverwrite);

        ImGui.separator();

        ImGui.text("Scale : ");
        ImGui.inputFloat("sX", selectedSpaceScale[0], ImGuiInputTextFlags.AlwaysOverwrite);
        ImGui.inputFloat("sY", selectedSpaceScale[1], ImGuiInputTextFlags.AlwaysOverwrite);
        ImGui.inputFloat("sZ", selectedSpaceScale[2], ImGuiInputTextFlags.AlwaysOverwrite);

        if(selectedSpace != null){
            setSelectedSpaceData();
        }
    }

    public void imgui(){
        ImGui.begin("Hierarchy");

        spacePosRotScale();

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

        selectedSpaceName.set(space.name);

        selectedSpacePos[0].set(space.position.x);
        selectedSpacePos[1].set(space.position.y);
        selectedSpacePos[2].set(space.position.z);

        selectedSpaceRot[0].set(space.rotation.x);
        selectedSpaceRot[1].set(space.rotation.y);
        selectedSpaceRot[2].set(space.rotation.z);

        selectedSpaceScale[0].set(space.scale.x);
        selectedSpaceScale[1].set(space.scale.y);
        selectedSpaceScale[2].set(space.scale.z);
    }
}
