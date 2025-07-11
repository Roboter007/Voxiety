#type vertex
#version 330 core

layout (location=0) in vec2 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;

void main() {
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;
    gl_Position = uProjection * uView * vec4(aPos, 0.0, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[8];

out vec4 color;

void main() {
    vec4 texColor = vec4(1, 1, 1, 1);
    if (fTexId > 0) {
        int id = int(fTexId);
        texColor = fColor * texture(uTextures[id], fTexCoords);

        // Invertierung der Farben (RGB-Komponenten)
        texColor.rgb = 1.0 - texColor.rgb;
    } else {
        texColor = fColor;
    }

    color = texColor;
}
