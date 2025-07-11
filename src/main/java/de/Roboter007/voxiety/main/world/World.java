package de.Roboter007.voxiety.main.world;


import de.Roboter007.voxiety.Voxiety;
import de.Roboter007.voxiety.main.world.blocks.Block;
import de.Roboter007.voxiety.main.world.entities.Entity;
import de.Roboter007.voxiety.main.world.gen.Perlin;
import de.Roboter007.voxiety.main.world.registry.RegistryEntry;
import de.Roboter007.voxiety.main.world.registry.VoxRegistry;
import de.Roboter007.voxiety.utils.VoxPaths;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class World implements Runnable {

    private static final double MEG = (Math.pow(1024, 2));

    public Thread thread;
    private final String name;
    private final WorldSettings worldSettings;
    public WorldGenStatus worldGenStatus;
    Perlin noise = new Perlin();

    public final float tps_limit = 100;
    public float tps = 0;

    //ToDo: verbessern des Block Caches für ein Gennerierungssystem
    public final int[][] blocks;
    public List<Entity> entities = new ArrayList<>();

    public World(String name, int maxXSize, int maxYSize, long seed) {
        this.name = name;
        this.worldSettings = new WorldSettings(this.name, seed, maxXSize, maxYSize);
        this.blocks = new int[maxXSize][maxYSize];
    }

    public World(String name, int maxXSize, int maxYSize) {
        this(name, maxXSize, maxYSize, new Random().nextLong());
    }

    public World(String name, WorldSettings worldSettings) {
        this.name = name;
        this.worldSettings = worldSettings;
        this.blocks = new int[worldSettings.maxXSize()][worldSettings.maxYSize()];
    }

    public String getName() {
        return name;
    }

    public int getMaxXSize() {
        return this.worldSettings.maxXSize();
    }

    public int getMaxYSize() {
        return this.worldSettings.maxYSize();
    }

    public long getSeed() {
        return this.worldSettings.seed();
    }

    public WorldSettings getWorldSettings() {
        return worldSettings;
    }

    @Nullable
    public RegistryEntry<Block> getBlockEntry(int x, int y) {
        int id = blocks[x][y];
        return VoxRegistry.getBlockEntryById(id);
    }

    @Nullable
    public Block getBlock(int x, int y) {
        RegistryEntry<Block> blockEntry = getBlockEntry(x, y);
        if(blockEntry == null) {
            return null;
        }
        return new Block(blockEntry, new Vector2i(x, y));
    }

    public Block[][] getBlocks() {
        Block[][] blocksInWorld = new Block[blocks.length][blocks.length];
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                Block block = getBlock(x, y);
                if(block != null) {
                    blocksInWorld[x][y] = block;
                }
            }
        }
        return blocksInWorld;
    }

    public void setBlock(int id, int x, int y) {
        blocks[x][y] = id;
    }

    public void setBlock(RegistryEntry<Block> blockEntry, int x, int y) {
        setBlock(blockEntry.id(), x, y);
    }

    public void setBlock(RegistryEntry<Block> blockEntry, Vector2i pos) {
        setBlock(blockEntry.id(), pos.x, pos.y);
    }

    public void spawnEntity(Entity entity) {
        entities.add(entity);
    }

    public void killEntity(Entity entity) {
        entities.remove(entity);
    }

    public Path getWorldFolder() {
        return Path.of(Worlds.WORLD_PATH + "/" + name);
    }

    public String getSaveLocation() {
        return getSavePath().toString();
    }

    public Path getSavePath() {
        if(getWorldFolder().toFile().mkdirs()) {
            System.out.println("Successfully created World Folder!");
        }

        return Path.of(getWorldFolder().toString(), "/" + "world" + ".json");
    }

    /*public void tick() {
        if(Entities.PLAYER.keyHandler.rightClick) {
            if (Entities.PLAYER.targetedBlock != null) {
                BlockEntry blockEntry = BlockRegistry.getBlockById(Entities.PLAYER.getBlockIdFromSelectedSlot());
                if (Entities.PLAYER.targetedBlock.getBlockEntry().getBlockId() == 0) {
                    Entities.PLAYER.world.setBlock(blockEntry, Entities.PLAYER.targetedBlock.getBlockPos());
                    save();
                    System.out.println("X: " + Entities.PLAYER.targetedBlock.getBlockPos().getX() + ", Y: " + Entities.PLAYER.targetedBlock.getBlockPos().getY());
                }
            }
        }
        if(Entities.PLAYER.keyHandler.leftClick) {
            if (Entities.PLAYER.targetedBlock != null) {
                BlockEntry blockEntry = BlockRegistry.getBlockById(0);
                if(Entities.PLAYER.targetedBlock.getBlockEntry() != blockEntry) {
                    Entities.PLAYER.world.setBlock(blockEntry, Entities.PLAYER.targetedBlock.getBlockPos());
                    save();
                    System.out.println("X: " + Entities.PLAYER.targetedBlock.getBlockPos().getX() + ", Y: " + Entities.PLAYER.targetedBlock.getBlockPos().getY());
                }
            }
        }

        /*if(Entities.PLAYER.keyHandler.middleClick) {
            if (Entities.PLAYER.targetedBlock != null) {
                BlockEntry blockEntry = Blocks.CHEST;
                if(Entities.PLAYER.targetedBlock.getBlockEntry() != blockEntry) {
                    Entities.PLAYER.world.setBlock(blockEntry, Entities.PLAYER.targetedBlock.getBlockPos());
                    save();
                    System.out.println("X: " + Entities.PLAYER.targetedBlock.getBlockPos().getX() + ", Y: " + Entities.PLAYER.targetedBlock.getBlockPos().getY());
                }
            }
        } */
    //}

    public void load() {
        try {
            if (getSavePath().toFile().exists()) {
                System.out.println("Loading " + this.getName() + "...");

                List<String> lines = VoxPaths.access().readLines(getSaveLocation());

                int x = 0;
                int y = 0;

                while (x < getMaxXSize() && y < getMaxYSize()) {
                    String line = lines.get(y);

                    while (x < getMaxXSize()) {
                        int number = Integer.parseInt(String.valueOf(line.charAt(x)));
                        blocks[x][y] = (byte) number;
                        x++;
                    }
                    if (x == getMaxXSize()) {
                        x = 0;
                        y++;
                    }
                }
            } else {
                System.out.println("Creating File... " + getSaveLocation());
                generate();
                if(getSavePath().toFile().createNewFile()) {
                    System.out.println("File created: " + getSaveLocation());
                }
                save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void save() {
        Thread saveThread = new Thread(() -> {
            //long startNano = System.nanoTime();
            try {
                Files.write(getSavePath(), getLines());
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*long endNano = System.nanoTime();
            long result = endNano - startNano;
            int resultInSec = (int) (result / 1000000000.0D);
            System.out.println("Save Time in nano: " + result);
            System.out.println("Save Time in sec: " + resultInSec); */

        });
        Voxiety.SERVICE.submit(saveThread);

    }

    private List<String> getLines() {
        List<String> lines = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for(int y = 0; y < this.getMaxYSize(); y++) {
            for(int x = 0; x < this.getMaxXSize(); x++) {
                stringBuilder.append(blocks[x][y]);
            }
            lines.add(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
        return lines;
    }

    public void generate() {
        Voxiety.VOX_PERF_TESTER.start(name);
        System.out.println("Start WorldGen for World: " + this.getName());

        float[][] seed =  Perlin.GenerateWhiteNoise(this.getMaxXSize(), this.getMaxYSize(), this.getSeed());

        float[][] floats = noise.GenerateSmoothNoise(seed, 4);

        for(int x = 0; x < this.getMaxXSize(); x++) {
            for(int y = 0; y < this.getMaxYSize(); y++) {
                float noise = floats[x][y];
                if(noise < 0.1) {
                    blocks[x][y] = 2;
                } else if(noise < 0.51) {
                    blocks[x][y] = 1;
                } else if (noise <= 0.6) {
                    blocks[x][y] = 3;
                } else if (noise <= 0.7) {
                    blocks[x][y] = 4;
                } else if (noise <= 0.9) {
                    blocks[x][y] = 5;
                } else {
                    blocks[x][y] = 6;
                }
            }
        }

        System.out.println("Finished World Generation");
        Voxiety.VOX_PERF_TESTER.end(name);

    }

    public void start() {
        if(thread == null) {
            thread = new Thread(this);
            Voxiety.SERVICE.submit(thread);
        }
    }

    public void deleteWorld(Runnable task) {
        VoxPaths.access().deleteFolder(getWorldFolder().toFile(), task);
    }

    public void stop() {
        save();
        this.worldSettings.saveToFile();
        this.thread = null;
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / this.tps_limit;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (this.thread != null) {
            //if (!VoxelUtils.isPaused()) {

                currentTime = System.nanoTime();

                delta += (currentTime - lastTime) / drawInterval;
                timer += currentTime - lastTime;

                lastTime = currentTime;

                if (delta >= 1) {
                    //this.tick();
                    delta--;
                    drawCount++;
                }

                if (timer >= 1000000000) {
                    this.tps = drawCount;

                    drawCount = 0;
                    timer = 0;
                }
            //}
        }
    }
}
