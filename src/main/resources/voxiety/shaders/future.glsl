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

// Verbesserte Farbfunktion für einen futuristischen Look
vec3 futuristicColor(vec3 inputColor) {
    // Farbverschiebung in Richtung Blau/Türkis
    vec3 tint = vec3(0.8, 1.0, 1.2);
    
    // Erhöhe den Kontrast
    inputColor = pow(inputColor, vec3(0.85));
    
    // Verstärke bestimmte Farbkanäle für einen futuristischen Look
    inputColor.b = inputColor.b * 1.3;
    
    // Wende Farbtönung an
    inputColor = inputColor * tint;
    
    // Verbessere die Helligkeit dunkler Bereiche leicht
    inputColor = mix(inputColor, vec3(0.1, 0.15, 0.3), 0.1);
    
    return inputColor;
}

void main() {
    vec4 texColor = vec4(1, 1, 1, 1);
    
    if (fTexId > 0) {
        int id = int(fTexId);
        texColor = fColor * texture(uTextures[id], fTexCoords);
        
        // Futuristischer Effekt ohne Animation
        texColor.rgb = futuristicColor(texColor.rgb);
        
        // Kanten-Highlight-Effekt
        float edgeFactor = max(0.0, 1.0 - 2.0 * abs(fTexCoords.x - 0.5)) * 
                          max(0.0, 1.0 - 2.0 * abs(fTexCoords.y - 0.5));
        
        // Subtilere Kanteneffekte
        vec3 edgeGlow = vec3(0.1, 0.3, 0.6) * (1.0 - edgeFactor) * 0.4;
        texColor.rgb += edgeGlow;
        
        // Begrenze die Farbwerte auf einen gültigen Bereich
        texColor.rgb = clamp(texColor.rgb, 0.0, 1.0);
    } else {
        texColor = fColor;
    }

    color = texColor;
}