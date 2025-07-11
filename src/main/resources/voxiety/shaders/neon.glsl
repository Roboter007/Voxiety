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
uniform float uTime; // Zeit für Animationseffekte

out vec4 color;

// Funktion für Farbverstärkung mit Sättigung und Leuchtintensität
vec3 enhanceColor(vec3 color, float saturation, float brightness) {
    // Umwandlung in HSV-ähnlichen Farbraum
    float maxVal = max(max(color.r, color.g), color.b);
    float minVal = min(min(color.r, color.g), color.b);
    float delta = maxVal - minVal;
    
    // Erhöhe Sättigung
    if (maxVal != 0.0) {
        vec3 enhanced = (color - minVal) / maxVal;
        enhanced = mix(vec3(1.0), enhanced, saturation);
        enhanced *= maxVal * brightness;
        return enhanced;
    }
    
    return color * brightness;
}

// Funktion für pulsierenden Neon-Glüheffekt
float pulseGlow(float time) {
    return 1.0 + 0.2 * sin(time * 2.0);
}

void main() {
    vec4 texColor = vec4(1, 1, 1, 1);
    
    if (fTexId > 0) {
        int id = int(fTexId);
        texColor = fColor * texture(uTextures[id], fTexCoords);
        
        // Neon-Effekt anwenden
        float glowIntensity = pulseGlow(uTime);
        texColor.rgb = enhanceColor(texColor.rgb, 1.8, glowIntensity);
        
        // Füge leichtes Leuchten an den Kanten hinzu
        float edgeFactor = max(0.0, 1.0 - 2.0 * abs(fTexCoords.x - 0.5)) * 
                          max(0.0, 1.0 - 2.0 * abs(fTexCoords.y - 0.5));
        vec3 edgeGlow = texColor.rgb * (1.0 - edgeFactor) * 0.5;
        
        // Kombiniere Farbeffekte
        texColor.rgb += edgeGlow;
        
        // Verstärke die dunkleren Bereiche leicht
        texColor.rgb = pow(texColor.rgb, vec3(0.85));
    } else {
        texColor = fColor;
    }

    color = texColor;
}