package de.Roboter007.voxiety.core.renderer.textures;

import org.joml.Vector2f;

public class Glyph {
    public final int textureID;       // das gemeinsame Atlas
    public final Vector2f uv0, uv1;   // UV‐Koordinaten (x0/y0, x1/y1)
    public final Vector2f offset;     // Pen‐Offset
    public final Vector2f advance;    // Pen‐Advance

    public Glyph(int textureID, Vector2f uv0, Vector2f uv1, Vector2f offset, Vector2f advance) {
        this.textureID = textureID;
        this.uv0       = uv0;
        this.uv1       = uv1;
        this.offset    = offset;
        this.advance   = advance;
    }
}
