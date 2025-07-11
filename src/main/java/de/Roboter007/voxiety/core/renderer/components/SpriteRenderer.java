package de.Roboter007.voxiety.core.renderer.components;

import de.Roboter007.voxiety.core.renderer.textures.SpriteTexture;
import de.Roboter007.voxiety.core.renderer.textures.Texture;
import de.Roboter007.voxiety.core.renderer.textures.Transform;
import de.Roboter007.voxiety.core.renderer.VoxComponent;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends VoxComponent {

    private final Vector4f color;
    private Sprite sprite;

    private Transform lastTransform;
    private boolean isDirty;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = null;
        this.isDirty = true;
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1, 1, 1, 1);
        this.isDirty = true;
    }

    public Vector4f getColor() {
        return color;
    }

    public SpriteTexture getTexture() {
        if (sprite != null) {
            return sprite.texture();
        } else {
            return null;
        }
    }

    public Vector2f[] getTexCoords() {
        if(sprite == null) {
            return new Vector2f[] {
                    new Vector2f(1, 1),
                    new Vector2f(1, 0),
                    new Vector2f(0, 0),
                    new Vector2f(0, 1)
            };
        }
        return sprite.texCoords();
    }

    @Override
    public void start() {
        this.lastTransform = voxElement.transform.copy();
    }

    @Override
    public void update(float delta) {
        if(!this.lastTransform.equals(this.voxElement.transform)) {
            this.voxElement.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        isDirty = true;
    }

    public void setColor(Vector4f color) {
        if(!this.color.equals(color)) {
            this.color.set(color);
            this.isDirty = true;
        }
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setClean() {
        isDirty = false;
    }

    @Override
    public String toString() {
        return "SpriteRenderer{" +
                "color=" + color +
                ", sprite=" + sprite +
                ", lastTransform=" + lastTransform +
                ", isDirty=" + isDirty +
                '}';
    }
}
