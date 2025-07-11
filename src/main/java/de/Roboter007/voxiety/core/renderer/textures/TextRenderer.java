package de.Roboter007.voxiety.core.renderer.textures;

import de.Roboter007.voxiety.core.renderer.font.Font;
import de.Roboter007.voxiety.core.renderer.shader.Shader;
import de.Roboter007.voxiety.core.renderer.textures.Glyph;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class TextRenderer {
    private final Shader shader;
    private final Font font;
    private final int vaoID, vboID;

    public TextRenderer(Shader shader, Font font) {
        this.shader = shader;
        this.font   = font;

        // VAO/VBO für Quad (pos.xy, uv.xy) aufsetzen
        vaoID = glGenVertexArrays();
        vboID = glGenBuffers();
        glBindVertexArray(vaoID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, 4 * 4 * Float.BYTES, GL_DYNAMIC_DRAW);

        // Position (layout 0)
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);

        // UV (layout 1)
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void renderText(String text, float x, float y, float r, float g, float b, float a) {
        shader.use();
        // Model‑Matrix für das Quad‑Batching stellen wir hier als Identity
        shader.uploadMat4f("uModel", new Matrix4f().identity());
        // Text‑Farbe & Texture‑Flag
        shader.uploadVec4f("uTextColor", new Vector4f(r, g, b, a));
        shader.uploadInt("uUseTexture", 1);
        shader.uploadInt("uFontAtlas", 0);

        glActiveTexture(GL_TEXTURE0);
        glBindVertexArray(vaoID);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        float penX = x;
        for (char c : text.toCharArray()) {
            Glyph glyph = font.getGlyph(c);

            float x0 = penX + glyph.offset.x;
            float y0 = y - glyph.offset.y;
            float x1 = x0 + (glyph.uv1.x - glyph.uv0.x);
            float y1 = y0 - (glyph.uv1.y - glyph.uv0.y);

            FloatBuffer buf = BufferUtils.createFloatBuffer(16);
            buf.put(new float[]{
                    x0, y0, glyph.uv0.x / Font.ATLAS_SIZE, glyph.uv0.y / Font.ATLAS_SIZE,
                    x0, y1, glyph.uv0.x / Font.ATLAS_SIZE, glyph.uv1.y / Font.ATLAS_SIZE,
                    x1, y1, glyph.uv1.x / Font.ATLAS_SIZE, glyph.uv1.y / Font.ATLAS_SIZE,
                    x1, y0, glyph.uv1.x / Font.ATLAS_SIZE, glyph.uv0.y / Font.ATLAS_SIZE
            });
            buf.flip();

            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, buf);

            // Quad zeichnen (Atlas bereits gebunden)
            glDrawArrays(GL_TRIANGLE_FAN, 0, 4);

            penX += glyph.advance.x;
        }

        glDisable(GL_BLEND);
        glBindVertexArray(0);
        shader.detach();
    }

    public void cleanup() {
        glDeleteBuffers(vboID);
        glDeleteVertexArrays(vaoID);
    }
}
