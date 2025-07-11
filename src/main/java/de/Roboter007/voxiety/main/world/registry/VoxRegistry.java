package de.Roboter007.voxiety.main.world.registry;

import de.Roboter007.voxiety.main.world.blocks.Block;
import de.Roboter007.voxiety.utils.VoxPaths;

import java.util.ArrayList;
import java.util.List;

public class VoxRegistry {
    
    private static final List<RegistryEntry<? extends Resource>> RESOURCE_REGISTRY = new ArrayList<>();

    public static <R extends Resource> RegistryEntry<R> register(Class<R> clazz, String path, String name, int zIndex) {
        RegistryEntry<R> registryEntry = new RegistryEntry<>(clazz, VoxPaths.access().getResourcePath(path), name, zIndex);
        RESOURCE_REGISTRY.add(registryEntry);
        return registryEntry;
    }

    @SuppressWarnings("unchecked")
    public static RegistryEntry<Block> getBlockEntryById(int id) {
        for(RegistryEntry<? extends Resource> entry : RESOURCE_REGISTRY) {
            if(entry.clazz() == Block.class && entry.id() == id) {
                return (RegistryEntry<Block>) entry;
            }
        }
        return null;
    }
}
