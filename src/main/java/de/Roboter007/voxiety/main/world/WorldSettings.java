package de.Roboter007.voxiety.main.world;

import de.Roboter007.voxiety.utils.VoxPaths;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WorldSettings {

    private List<Object> options = new ArrayList<>();
    private final String path;
    private final File file;

    public WorldSettings(String name, long seed, int maxXSize, int maxYSize) {
        this.path = Worlds.WORLD_PATH + "/" + name + "/world.info";
        this.file = new File(this.path);
        options.add(seed);
        options.add(maxXSize);
        options.add(maxYSize);
    }

    public WorldSettings(String name, List<Object> options) {
        this.path = Worlds.WORLD_PATH + "/" + name + "/world.info";
        this.file = new File(this.path);
        this.options = options;
    }


    public File getFile() {
        return file;
    }

    public String getPath() {
        return path;
    }

    public List<Object> getOptions() {
        return options;
    }

    public long seed() {
        Object value = this.getOptions().getFirst();
        if(value instanceof String string) {
            return Long.parseLong(string);
        } else if (value instanceof Long l) {
            return l;
        } else {
            System.out.println("Failed to get seed");
            return 0;
        }
    }

    public int maxXSize() {
        Object value = this.getOptions().get(1);
        if(value instanceof String string) {
            return Integer.parseInt(string);
        } else if (value instanceof Integer i) {
            return i;
        } else {
            System.out.println("Failed to get maxXSize");
            return 0;
        }
    }

    public int maxYSize() {
        Object value = this.getOptions().get(2);
        if(value instanceof String string) {
            return Integer.parseInt(string);
        } else if (value instanceof Integer i) {
            return i;
        } else {
            System.out.println("Failed to get maxYSize");
            return 0;
        }
    }

    public void saveToFile() {
        if(!file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        List<String> lines = new ArrayList<>();

        for(Object object : this.getOptions()) {
            lines.add(object.toString());
        }

        try {
            Files.write(Path.of(this.getPath()), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static WorldSettings fromFile(String worldName, File file) {
        if(file.exists()) {
            List<String> lines = VoxPaths.access().readLines(file.getPath());
            List<Object> worldOptions = new ArrayList<>();
            for(String line : lines) {
                int index = lines.indexOf(line);
                worldOptions.add(index, line);
            }
            return new WorldSettings(worldName, worldOptions);
        } else {
            System.out.println("Couldn't find file: " + file.getPath());
        }

        return null;
    }
}
