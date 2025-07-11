package de.Roboter007.voxiety.core.renderer.font;

import de.Roboter007.voxiety.core.renderer.textures.Glyph;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Font {
    public static final int ATLAS_SIZE = 512;
    private static final int FIRST_CHAR = 32;
    private static final int NUM_CHARS  = 95;

    private final int atlasTexID;
    private final Map<Character, Glyph> glyphs = new HashMap<>();

    public Font(Path ttfPath, float pixelHeight) throws IOException {
        // --- 1) Direct-ByteBuffer laden ---
        ByteBuffer ttfBuffer;
        try ( FileChannel fc = FileChannel.open(ttfPath, StandardOpenOption.READ) ) {
            ttfBuffer = BufferUtils.createByteBuffer((int)fc.size() + 1);
            while (fc.read(ttfBuffer) != -1) ;
        }
        ttfBuffer.flip();

        // --- 2) Atlas backen ---
        ByteBuffer bitmap = BufferUtils.createByteBuffer(ATLAS_SIZE * ATLAS_SIZE);
        STBTTBakedChar.Buffer cdata = STBTTBakedChar.malloc(NUM_CHARS);
        STBTruetype.stbtt_BakeFontBitmap(
                ttfBuffer, pixelHeight,
                bitmap, ATLAS_SIZE, ATLAS_SIZE,
                FIRST_CHAR, cdata
        );

        // --- 3) Atlas-Textur erzeugen ---
        atlasTexID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, atlasTexID);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED,
                ATLAS_SIZE, ATLAS_SIZE, 0,
                GL_RED, GL_UNSIGNED_BYTE, bitmap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        for (int i = 0; i < NUM_CHARS; i++) {
            char c = (char)(FIRST_CHAR + i);
            STBTTBakedChar bc = cdata.get(i);

            // Pixel‑Region im Atlas
            float x0 = bc.x0(), y0 = bc.y0();
            float x1 = bc.x1(), y1 = bc.y1();

            // in UV (0..1) umrechnen
            float u0 = x0 / ATLAS_SIZE;
            float v0 = y0 / ATLAS_SIZE;
            float u1 = x1 / ATLAS_SIZE;
            float v1 = y1 / ATLAS_SIZE;

            // Offsets und Advance
            float offX = bc.xoff();
            float offY = bc.yoff();
            float advX = bc.xadvance();
            // Optional: vertikaler Advance, falls nötig
            float advY = 0f;

            // *** HIER: Vector2f-Objekte erzeugen ***
            Vector2f uv0     = new Vector2f(u0, v0);
            Vector2f uv1     = new Vector2f(u1, v1);
            Vector2f offset  = new Vector2f(offX, offY);
            Vector2f advance = new Vector2f(advX, advY);

            Glyph glyph = new Glyph(atlasTexID, uv0, uv1, offset, advance);
            glyphs.put(c, glyph);
        }

        cdata.free();
    }

    public Glyph getGlyph(char c) {
        return glyphs.getOrDefault(c, glyphs.get(' '));
    }

    public int getAtlasTexID() {
        return atlasTexID;
    }
}
