package de.Roboter007.voxiety.main.world;

public enum WorldGenStatus {

    NOT_STARTED(0),
    BLOCKS(1),
    FLOWERS(2),
    STRUCTURES(3),
    CIVILIZATIONS(4),
    FINISHED(5);


    private final int status;

    WorldGenStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public WorldGenStatus getStatus(int i) {
        return WorldGenStatus.values()[i];
    }
}
