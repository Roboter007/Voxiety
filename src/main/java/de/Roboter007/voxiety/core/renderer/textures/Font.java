package de.Roboter007.voxiety.core.renderer.font;

import de.Roboter007.voxiety.core.renderer.shader.Shader;
import de.Roboter007.voxiety.core.renderer.textures.Glyph;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;
import org.joml.Vector2f;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Font {
    private static final int FIRST_CHAR = 32;
    private static final int NUM_CHARS  = 95;  // 32..126
    public static final int ATLAS_SIZE = 512;

    private final Map<Character, Glyph> glyphs = new HashMap<>();

    public Font(String ttfPath, float pixelHeight) throws IOException {
        // 1) TTF-Datei in ByteBuffer laden
        ByteBuffer ttfBuffer = ByteBuffer.wrap(Files.readAllBytes(Path.of(ttfPath)));

        // 2) Bake-Atlas erstellen
        ByteBuffer bitmap = BufferUtils.createByteBuffer(ATLAS_SIZE * ATLAS_SIZE);
        STBTTBakedChar.Buffer cdata = STBTTBakedChar.malloc(NUM_CHARS);
        STBTruetype.stbtt_BakeFontBitmap(ttfBuffer, pixelHeight, bitmap, ATLAS_SIZE, ATLAS_SIZE, FIRST_CHAR, cdata);

        // 3) Atlas-Textur erzeugen
        int atlasTex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, atlasTex);
        // wichtig: ein-byte-alignment
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, ATLAS_SIZE, ATLAS_SIZE, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // 4) Für jede Glyphe ein Glyph-Objekt
        for (int i = 0; i < NUM_CHARS; i++) {
            char c = (char) (FIRST_CHAR + i);
            STBTTBakedChar bc = cdata.get(i);

            Glyph g = new Glyph(
                    atlasTex,
                    new Vector2f(bc.x0(), bc.y0()),
                    new Vector2f(bc.x1(), bc.y1()),
                    new Vector2f(bc.xoff(), bc.yoff()),
                    new Vector2f(bc.xadvance(), pixelHeight)
            );
            glyphs.put(c, g);
        }
    }

    public Glyph getGlyph(char c) {
        // Fallback auf Space, wenn nicht vorhanden
        return glyphs.getOrDefault(c, glyphs.get(' '));
    }
}
