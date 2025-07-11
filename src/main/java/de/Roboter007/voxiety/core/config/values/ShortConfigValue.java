package de.Roboter007.voxiety.core.config.values;

import de.Roboter007.voxiety.core.config.holder.StringHolder;

public class ShortConfigValue extends VoxelConfigValue<Short> {


    public ShortConfigValue(Short value) {
        super(value);
    }

    public ShortConfigValue(StringHolder stringHolder) {
        super(stringHolder);
    }

    @Override
    public String toString() {
        return "short$" + this.getValue();
    }

    @Override
    public Short fromString(String str) {
        String[] splitData = str.split("\\$");
        String data = splitData[1];
        if(data.equals("null")) {
            return null;
        } else {
            return Short.parseShort(data);
        }
    }
}
