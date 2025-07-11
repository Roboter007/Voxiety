package de.Roboter007.voxiety.core.config.values;

import de.Roboter007.voxiety.core.config.holder.StringHolder;

public abstract class VoxelConfigValue<V> {

    private V value;

    public VoxelConfigValue(V value) {
        this.value = value;
    }

    public VoxelConfigValue(StringHolder stringHolder) {
        this.value = fromString(stringHolder.string());
    }


    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public abstract String toString();
    public abstract V fromString(String str);

    public static VoxelConfigValue<?> newFromString(String str) {
        String[] splitData = str.split("\\$");
        String dataType = splitData[0];

        StringHolder string = new StringHolder(str);

        switch (dataType) {
            case "boolean" -> {
                return new BooleanConfigValue(string);
            }
            case "byte" -> {
                return new ByteConfigValue(string);
            }
            case "char" -> {
                return new CharConfigValue(string);
            }
            case "double" -> {
                return new DoubleConfigValue(string);
            }
            case "float" -> {
                return new FloatConfigValue(string);
            }
            case "integer" -> {
                return new IntConfigValue(string);
            }
            case "long" -> {
                return new LongConfigValue(string);
            }
            case "short" -> {
                return new ShortConfigValue(string);
            }
            case "string" -> {
                return new StringConfigValue(string);
            }
            default -> {
                System.out.println("Error! This Config Value does not exist!");
                return null;
            }
        }
    }
}
