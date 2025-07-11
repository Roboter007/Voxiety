package de.Roboter007.voxiety.core.renderer.textures;

import de.Roboter007.voxiety.core.renderer.Asset;
import de.Roboter007.voxiety.utils.VoxPaths;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBTruetype.*;

public class Glyph implements SpriteTexture {

    private final char character;
    private final float fontSize;
    protected final String path;
    protected final int texID;
    protected int width, height;

    public Glyph(char character, float fontSize) {
        this.path = "";
        this.texID = glGenTextures();
        this.character = character;
        this.fontSize = fontSize;

        loadTexture();
    }

    public void loadTexture() {
        System.out.println("Character: " + character);
        STBTTFontinfo font = STBTTFontinfo.create();
        ByteBuffer fontBuffer = loadFontToBuffer(VoxPaths.access().getResourcePath("voxiety/fonts/youthanasia_font.ttf"));

        if (fontBuffer == null || !stbtt_InitFont(font, fontBuffer)) {
            throw new RuntimeException("Could not init font");
        }

        float scale = stbtt_ScaleForPixelHeight(font, fontSize);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer xOffset = BufferUtils.createIntBuffer(1);
        IntBuffer yOffset = BufferUtils.createIntBuffer(1);

        ByteBuffer image = stbtt_GetCodepointBitmap(font, scale, scale, character, width, height, xOffset, yOffset);

        if (image == null) {
            System.out.println("Kein Bitmap vorhanden für Zeichen: '" + character + "' (Codepoint: " + (int) character + ")");
            // Optional: Breite berechnen, um trotzdem layouten zu können
            IntBuffer advanceWidth = BufferUtils.createIntBuffer(1);
            IntBuffer leftBearing = BufferUtils.createIntBuffer(1);
            stbtt_GetCodepointHMetrics(font, character, advanceWidth, leftBearing);

            this.width = (int) (advanceWidth.get(0) * scale);
            this.height = 0;

            return; // Kein Texture-Upload nötig
        }

        this.width = width.get(0);
        this.height = height.get(0);

        bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_R, GL_ZERO);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_G, GL_ZERO);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_B, GL_ZERO);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_A, GL_RED);

        // OpenGL-Tex-Parameter setzen
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Single-channel Alpha-Texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, width.get(0), height.get(0), 0, GL_RED, GL_UNSIGNED_BYTE, image);

        stbtt_FreeBitmap(image);
    }

    @Nullable
    public static ByteBuffer loadFontToBuffer(String path) {
        try {
            byte[] data = Files.readAllBytes(Paths.get(path));
            ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
            buffer.put(data);
            buffer.flip();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getID() {
        return texID;
    }

    public void deleteTexture() {
        glDeleteTextures(texID);
    }


    @Override
    public boolean equals(Object o) {
        return o instanceof Texture texture && this.texID == texture.texID &&
                this.width == texture.width && this.height == texture.height &&
                Objects.equals(this.path, texture.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, texID, width, height);
    }

    @Override
    public String toString() {
        return "Glyph{" +
                "character=" + character +
                ", fontSize=" + fontSize +
                ", path='" + path + '\'' +
                ", texID=" + texID +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public String path() {
        return path;
    }
}
