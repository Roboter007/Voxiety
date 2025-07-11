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

in vec4 vColor;
in vec2 vTexCoords;
in float vTexId;

out vec4 FragColor;

uniform sampler2D uTextures[8];
uniform float uTime;

void main() {
    int texIndex = int(vTexId);
    vec4 texColor = texture(uTextures[texIndex], vTexCoords);

    // Neon-Glow Effekt
    float glow = 0.5 + 0.5 * sin(uTime + vTexCoords.y * 20.0);
    vec3 neon = mix(texColor.rgb, vec3(0.0, 1.0, 1.0), glow * 0.3);

    // Scanline-Effekt
    float scanline = 0.9 + 0.1 * sin((vTexCoords.y + uTime * 0.5) * 200.0);

    FragColor = vec4(neon * scanline, texColor.a) * vColor;
}