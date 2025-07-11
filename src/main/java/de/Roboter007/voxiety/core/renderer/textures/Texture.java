package de.Roboter007.voxiety.core.renderer.textures;

import de.Roboter007.voxiety.core.renderer.Asset;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture implements SpriteTexture {

    protected final String path;
    protected final int texID;
    protected int width, height;

    public Texture(String path) {
        this.path = path;
        this.texID = glGenTextures();

        loadTexture();
    }

    public void loadTexture() {
        bind();

        // Set texture parameters
        // Repeat image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // When stretching the image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // When shrinking an image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(path, width, height, channels, 0);

        if(image != null) {
            this.width = width.get(0);
            this.height = height.get(0);

            if(channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if(channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                System.out.println("Error: (Texture) Unknown number of channel '" + channels.get(0) + "'");
            }
            int err = glGetError();
            if (err != GL_NO_ERROR) {
                System.out.println("OpenGL Error after glTexImage2D: " + err);
            }
            stbi_image_free(image);
        } else {
            System.out.println("Error: (Texture) Could not load image '" + path + "'");
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

    public String path() {
        return path;
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
        return "Texture{" +
                "path='" + path + '\'' +
                ", texID=" + texID +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}