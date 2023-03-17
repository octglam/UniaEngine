#version 150

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform vec4 colour;

void main(void){
    out_Color = texture(guiTexture,textureCoords) * colour;
}
