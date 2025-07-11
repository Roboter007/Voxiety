package de.Roboter007.voxiety.core.config.values;

import de.Roboter007.voxiety.core.config.holder.StringHolder;

public class StringConfigValue extends VoxelConfigValue<String> {


    public StringConfigValue(String value) {
        super(value);
    }

    public StringConfigValue(StringHolder stringHolder) {
        super(stringHolder);
    }

    @Override
    public String toString() {
        return "string$" + this.getValue();
    }

    @Override
    public String fromString(String str) {
        String[] splitData = str.split("\\$");
        if(str.equals("null")) {
            return null;
        } else {
            return splitData[1];
        }
    }
}
