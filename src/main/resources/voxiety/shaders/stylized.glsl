#type vertex
#version 330 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;

void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform float uTime;
uniform sampler2D uTextures[8];
uniform vec2 uResolution;
uniform float uPixelSize;  // Controls the level of pixelation
uniform float uColorShift; // Controls the color shifting intensity

out vec4 color;

void main()
{
    // Pixelation effect
    float pixelSize = max(1.0, uPixelSize);
    vec2 pixelatedCoords = floor(fTexCoords * uResolution / pixelSize) * pixelSize / uResolution;

    // Sample the texture
    int id = int(fTexId);
    vec4 texColor = vec4(1, 1, 1, 1);
    if (id == 0) {
        texColor = fColor;
    } else if (id == 1) {
        texColor = fColor * texture(uTextures[0], pixelatedCoords);
    } else if (id == 2) {
        texColor = fColor * texture(uTextures[1], pixelatedCoords);
    } else if (id == 3) {
        texColor = fColor * texture(uTextures[2], pixelatedCoords);
    } else if (id == 4) {
        texColor = fColor * texture(uTextures[3], pixelatedCoords);
    } else if (id == 5) {
        texColor = fColor * texture(uTextures[4], pixelatedCoords);
    } else if (id == 6) {
        texColor = fColor * texture(uTextures[5], pixelatedCoords);
    } else if (id == 7) {
        texColor = fColor * texture(uTextures[6], pixelatedCoords);
    } else if (id == 8) {
        texColor = fColor * texture(uTextures[7], pixelatedCoords);
    }

    // Color shifting effect based on time
    float colorShiftAmount = sin(uTime) * uColorShift;
    vec4 shiftedColor = vec4(
    texColor.r + colorShiftAmount,
    texColor.g + cos(uTime * 0.7) * uColorShift,
    texColor.b + sin(uTime * 0.5) * uColorShift,
    texColor.a
    );

    // Apply a vignette effect
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(fTexCoords, center);
    float vignette = smoothstep(0.8, 0.2, dist);

    // Final color
    color = mix(shiftedColor, vec4(shiftedColor.rgb * vignette, shiftedColor.a), 0.3);
}
