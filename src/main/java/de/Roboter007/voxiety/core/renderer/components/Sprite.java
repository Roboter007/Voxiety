package de.Roboter007.voxiety.core.renderer.components;

import de.Roboter007.voxiety.core.renderer.textures.SpriteTexture;
import de.Roboter007.voxiety.core.renderer.textures.Texture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Sprite {

    private final SpriteTexture texture;
    private final Vector2f[] texCoords;

    public Sprite(@NotNull SpriteTexture texture) {
        this(texture, new Vector2f[] {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        });
    }

    public Sprite(@NotNull SpriteTexture texture, Vector2f[] texCoords) {
        this.texture = texture;
        this.texCoords = texCoords;
    }

    public void setDimensions(int width, int height) {
        this.texture.setWidth(width);
        this.texture.setHeight(height);
    }

    public void setDimensions(int scale) {
        setDimensions(this.texture.getWidth() * scale, this.texture.getHeight() * scale);
    }

    public SpriteTexture texture() {
        return texture;
    }

    public Vector2f[] texCoords() {
        return texCoords;
    }

    @Override
    public String toString() {
        return "Sprite{" +
                "texture=" + texture.path() + "-w:" + texture.getWidth() + "-h:" + texture.getHeight() +
                ", texCoords=" + Arrays.toString(texCoords) +
                '}';
    }
}
