package de.Roboter007.voxiety.core.config;


import de.Roboter007.voxiety.core.config.values.VoxelConfigValue;

import java.util.Objects;

public class VoxConfigOption<T> {

    private String name;
    private VoxelConfigValue<T> configValue;

    public VoxConfigOption(String name, VoxelConfigValue<T> configValue) {
        this.configValue = configValue;
        this.name = name;
    }

    public VoxConfigOption(String name) {
        this.name = name;
    }

    public VoxelConfigValue<T> getConfigValue() {
        return configValue;
    }

    public String getName() {
        return name;
    }


    public void setConfigValue(VoxelConfigValue<T> value) {
        this.configValue = value;
    }


    public static VoxConfigOption<?> newConfigValue(String data) {
        String[] dataList = data.split("=");

        if(dataList.length == 2) {
            System.out.println("Success!");
            return new VoxConfigOption<>(dataList[0], VoxelConfigValue.newFromString(dataList[1]));
        } else {
            System.out.println("Wrong F!");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void fromString(String data) {
        String[] dataList = data.split("=");

        this.name = dataList[0];
        System.out.println("Test 10");
        this.configValue = (VoxelConfigValue<T>) VoxelConfigValue.newFromString(dataList[1]);
    }

    @Override
    public String toString() {
        return name + "=" + configValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VoxConfigOption<?> that = (VoxConfigOption<?>) o;
        return name.equalsIgnoreCase(that.name) && configValue.toString().equalsIgnoreCase(that.configValue.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, configValue);
    }
}
