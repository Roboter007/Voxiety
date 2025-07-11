#type vertex
#version 330 core

// Default Vertex Shader mit TexCoords

layout(location = 0) in vec3 aPos;        // Position im Modell‑Raum
layout(location = 1) in vec2 aTexCoord;   // UV-Koordinate

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;

out vec2 vTexCoord;  // an Fragment‑Shader weiterreichen

void main() {
    gl_Position = uProjection * uView * uModel * vec4(aPos, 1.0);
    vTexCoord  = aTexCoord;
}


#type fragment
#version 330 core

// Fragment Shader, der optional ein Font‑Atlas sampelt

in vec2 vTexCoord;
out vec4 FragColor;

// Wenn du keinen Font renderst, kannst du diese Uniform auf (0,0,0,1) setzen
uniform vec4 uTextColor;

// Der Atlas, in dem deine Glyphe als R‑Kanal abgelegt ist
uniform sampler2D uFontAtlas;

// Ein Flag, ob wir Textur-Rendering wollen (1) oder nur Flat‑Color (0)
uniform int uUseTexture;

void main() {
    vec4 baseColor = uTextColor;

    if (uUseTexture == 1) {
        // texture(...).r ist dein Alpha‑Mask‑Wert
        float alpha = texture(uFontAtlas, vTexCoord).r;
        baseColor.a *= alpha;
    }

    FragColor = baseColor;
}
