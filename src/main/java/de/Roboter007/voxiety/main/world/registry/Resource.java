package de.Roboter007.voxiety.main.world.registry;

import de.Roboter007.voxiety.core.renderer.VoxElement;
import de.Roboter007.voxiety.core.renderer.components.Sprite;
import de.Roboter007.voxiety.core.renderer.components.SpriteRenderer;
import de.Roboter007.voxiety.core.renderer.textures.Texture;
import de.Roboter007.voxiety.core.renderer.textures.Transform;
import de.Roboter007.voxiety.main.VoxWindow;
import de.Roboter007.voxiety.utils.VoxAssets;
import de.Roboter007.voxiety.utils.VoxPaths;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Arrays;

public abstract class Resource {

    protected Vector2f screenPosition;

    protected float width;
    protected float height;

    protected int zIndex;

    protected VoxElement voxElement;

    public Resource(Vector2i blockPosition, float width, float height, int zIndex) {
        this(new Vector2f(blockPosition.x * VoxWindow.tileSize(), blockPosition.y * VoxWindow.tileSize()), width, height, zIndex);
    }

    public Resource(Vector2f screenPosition, float width, float height, int zIndex) {
        this.screenPosition = screenPosition;
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
    }

    public Resource(float width, float height, int zIndex) {
        this(new Vector2f(), width, height, zIndex);
    }

    public Resource(float entityWidth, float entityHeight) {
        this((Vector2f) null, entityWidth, entityHeight, 0);
    }

    public void loadVoxElement() {
        System.out.println(sprite().texture().path());
        System.out.println(Arrays.toString(sprite().texCoords()));
        this.voxElement = new VoxElement(new Transform(screenPosition, new Vector2f(width, height)), zIndex);
        voxElement.addComponent(new SpriteRenderer(sprite()));
    }

    public abstract Sprite sprite();

    public void updateSprite() {
        //this.voxElement = new VoxElement(name, new Transform(screenPosition, new Vector2f(width, height)), zIndex);
        this.voxElement.transform = new Transform(screenPosition, new Vector2f(width, height));

        if(sprite() != null) {
            voxElement.getComponent(SpriteRenderer.class).setSprite(sprite());
        } else {
            System.out.println("Texture is Null! in Resource.class");
        }
    }

    public VoxElement voxElement() {
        return voxElement;
    }

    public void setVoxElement(VoxElement voxElement) {
        this.voxElement = voxElement;
    }

    public float width() {
        return width;
    }

    public float height() {
        return height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setScreenPosition(Vector2f screenPosition) {
        this.screenPosition = screenPosition;
    }

    public Vector2f screenPosition() {
        return screenPosition;
    }

    public int zIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }
}
