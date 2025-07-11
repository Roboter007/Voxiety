package de.Roboter007.voxiety.main.world.entities;

import de.Roboter007.voxiety.core.renderer.components.Spritesheet;
import de.Roboter007.voxiety.main.world.World;
import de.Roboter007.voxiety.main.world.entities.animation.Direction;
import de.Roboter007.voxiety.main.world.registry.Resource;
import de.Roboter007.voxiety.utils.VoxAssets;
import org.joml.Vector2f;
import org.joml.Vector2i;

public abstract class Entity extends Resource {

    protected final String name;

    protected Vector2i position;
    protected World world;
    protected int speed;

    protected Direction direction = Direction.DOWN;
    protected int animationState = 1;

    protected int animationCooldown = 0;
    protected final int maxAnimationCooldown;

    public Entity(String name, Vector2f screenPos, float entityWidth, float entityHeight, int zIndex, int maxAnimationCooldown) {
        super(screenPos, entityWidth, entityHeight, zIndex);
        this.name = name;
        this.position = new Vector2i(2, 2);
        this.maxAnimationCooldown = maxAnimationCooldown;
    }

    public Entity(String name, float entityWidth, float entityHeight, int zIndex, int maxAnimationCooldown) {
        super(entityWidth, entityHeight, zIndex);
        this.name = name;
        this.position = new Vector2i(20, 20);
        this.maxAnimationCooldown = maxAnimationCooldown;
    }

    public void update() {
        animationCooldown++;
        if (animationCooldown > maxAnimationCooldown) {
            if (animationState == 1) {
                animationState = 2;
            } else {
                animationState = 1;
            }
            animationCooldown = 0;
        }
    }

    public Spritesheet spritesheet(String name) {
        return (Spritesheet) VoxAssets.getAsset(name);
    }

    public String name() {
        return name;
    }

    public World world() {
        return world;
    }

    public Vector2i getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        this.position.set(x, y);
    }

    public void summon(World world, Vector2i pos) {
        this.world = world;
        position = pos;
    }

    public void tick() {

    }
}
