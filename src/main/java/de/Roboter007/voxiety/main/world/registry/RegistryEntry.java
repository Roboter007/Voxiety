package de.Roboter007.voxiety.main.world.registry;

import de.Roboter007.voxiety.main.VoxWindow;

import java.util.HashMap;

public record RegistryEntry<R extends Resource>(Class<R> clazz, int id, String path, String name, float width, float height, int zIndex) {

    private static final HashMap<Class<? extends Resource>, Integer> ids = new HashMap<>();

    private static int id(Class<? extends Resource> clazz) {
        Integer id = ids.get(clazz);
        if(id == null) {
            id = 0;
        }
        if(!ids.containsKey(clazz)) {
            ids.put(clazz, id + 1);
        } else {
            ids.replace(clazz, id + 1);
        }
        System.out.println("Clazz: " + clazz + ", ID: " + id);
        return id;
    }

    public RegistryEntry(Class<R> clazz, String path, String name, int zIndex) {
        this(clazz, id(clazz), path, name, VoxWindow.tileSize(), VoxWindow.tileSize(), zIndex);
    }

    public RegistryEntry(Class<R> clazz, String path, String name, float width, float height, int zIndex) {
        this(clazz, id(clazz), path, name, width, height, zIndex);
    }

    public RegistryEntry(Class<R> clazz, int id, String path, String name, float width, float height, int zIndex) {
        this.clazz = clazz;
        this.path = path;
        this.name = name;
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
        this.id = id;
    }
}
