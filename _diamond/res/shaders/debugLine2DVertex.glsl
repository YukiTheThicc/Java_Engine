#version 330 core

layout (location=0) in vec3 attrPos;
layout (location=1) in vec3 attrColor;

uniform mat4 uProjection;
uniform mat4 uView;

out vec3 fragColor;

void main() {
    fragColor = attrColor;
    gl_Position = uProjection * uView * vec4(attrPos, 1.0);
}