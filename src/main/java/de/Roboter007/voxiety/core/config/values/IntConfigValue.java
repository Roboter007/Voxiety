package de.Roboter007.voxiety.core.config.values;

import de.Roboter007.voxiety.core.config.holder.StringHolder;

public class IntConfigValue extends VoxelConfigValue<Integer> {


    public IntConfigValue(Integer value) {
        super(value);
    }

    public IntConfigValue(StringHolder stringHolder) {
        super(stringHolder);
    }

    @Override
    public String toString() {
        return "integer$" + this.getValue();
    }

    @Override
    public Integer fromString(String str) {
        String[] splitData = str.split("\\$");
        String data = splitData[1];
        if(data.equals("null")) {
            return null;
        } else {
            return Integer.parseInt(data);
        }
    }
}
