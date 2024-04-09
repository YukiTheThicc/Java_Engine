    #type vertex
    #version 330 core

    layout (location=0) in vec3 attrPos;
    layout (location=1) in vec3 attrColor;

    uniform mat4 uProjection;
    uniform mat4 uView;
    uniform int uType;

    out vec3 fragColor;
    out int type;

    void main() {
        fragColor = attrColor;
        type = uType;
        gl_Position = uProjection * uView * vec4(attrPos, 1.0);
    }

    #type fragment
    #version 330 core

    in vec3 fragColor;

    out vec4 color;

    void main() {

        color = vec4(fragColor, 1);
    }