package de.Roboter007.voxiety.main.world.blocks;

import de.Roboter007.voxiety.main.world.registry.RegistryEntry;

import static de.Roboter007.voxiety.main.world.registry.VoxRegistry.register;

public class Blocks {

    public static void load() {}

    public static RegistryEntry<Block> AIR = register(Block.class, null, "Air", 1);
    public static RegistryEntry<Block> DIRT = register(Block.class, "voxiety/block/dirt.png", "Dirt", 1);
    public static RegistryEntry<Block> GRASS_BLOCK = register(Block.class, "voxiety/block/grass_block.png", "Grass Block", 1);
    public static RegistryEntry<Block> STONE_BLOCK = register(Block.class, "voxiety/block/stone.png", "Stone", 1);
    public static RegistryEntry<Block> SAND_BLOCK = register(Block.class, "voxiety/block/sand.png", "Sand", 1);
    public static RegistryEntry<Block> GRAVEL_BLOCK = register(Block.class, "voxiety/block/gravel.png", "Gravel", 1);


}
