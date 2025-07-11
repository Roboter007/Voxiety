package de.Roboter007.voxiety.main.world.blocks;

import de.Roboter007.voxiety.core.renderer.VoxElement;
import de.Roboter007.voxiety.core.renderer.components.Sprite;
import de.Roboter007.voxiety.core.renderer.components.SpriteRenderer;
import de.Roboter007.voxiety.core.renderer.textures.Texture;
import de.Roboter007.voxiety.core.renderer.textures.Transform;
import de.Roboter007.voxiety.main.world.registry.RegistryEntry;
import de.Roboter007.voxiety.main.world.registry.Resource;
import de.Roboter007.voxiety.utils.VoxAssets;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Block extends Resource {

    private final Vector2i blockPosition;
    private final RegistryEntry<Block> blockEntry;

    public Block(RegistryEntry<Block> blockEntry, Vector2i position) {
        super(position, blockEntry.width(), blockEntry.height(), blockEntry.zIndex());
        this.blockEntry = blockEntry;
        this.blockPosition = position;

        this.loadVoxElement();
    }

    public String name() {
        return blockEntry.name();
    }

    @Override
    public Sprite sprite() {
        String path = blockEntry.path();
        if(path == null) {
            return null;
        }
        Texture blockTexture = VoxAssets.getTexture(blockEntry.path());
        if(blockTexture == null) {
            System.out.println("Block Texture not found for path: " + blockEntry.path());
            return null;
        }

        return new Sprite(blockTexture);
    }

    public RegistryEntry<Block> getBlockEntry() {
        return blockEntry;
    }

    public Vector2i getBlockPosition() {
        return blockPosition;
    }
}
