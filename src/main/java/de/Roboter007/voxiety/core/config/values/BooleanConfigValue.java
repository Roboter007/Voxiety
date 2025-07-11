package de.Roboter007.voxiety.core.config.values;

import de.Roboter007.voxiety.core.config.holder.StringHolder;

public class BooleanConfigValue extends VoxelConfigValue<Boolean> {


    public BooleanConfigValue(Boolean value) {
        super(value);
    }

    public BooleanConfigValue(StringHolder stringHolder) {
        super(stringHolder);
    }

    @Override
    public String toString() {
        return "boolean$" + this.getValue();
    }

    @Override
    public Boolean fromString(String str) {
        String[] splitData = str.split("\\$");
        String data = splitData[1];
        if(data.equals("null")) {
            return null;
        } else {
            return Boolean.parseBoolean(data);
        }
    }
}
