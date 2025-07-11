package de.Roboter007.voxiety.core.config.values;

import de.Roboter007.voxiety.core.config.holder.StringHolder;

public class LongConfigValue extends VoxelConfigValue<Long> {

    public LongConfigValue(Long value) {
        super(value);
    }

    public LongConfigValue(StringHolder stringHolder) {
        super(stringHolder);
    }

    @Override
    public String toString() {
        return "long$" + this.getValue();
    }

    @Override
    public Long fromString(String str) {
        String[] splitData = str.split("\\$");
        String data = splitData[1];
        if(data.equals("null")) {
            return null;
        } else {
            return Long.parseLong(data);
        }
    }
}
