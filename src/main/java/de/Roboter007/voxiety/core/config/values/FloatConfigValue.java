package de.Roboter007.voxiety.core.config.values;

import de.Roboter007.voxiety.core.config.holder.StringHolder;

public class FloatConfigValue extends VoxelConfigValue<Float> {


    public FloatConfigValue(Float value) {
        super(value);
    }

    public FloatConfigValue(StringHolder stringHolder) {
        super(stringHolder);
    }

    @Override
    public String toString() {
        return "float$" + this.getValue();
    }

    @Override
    public Float fromString(String str) {
        String[] splitData = str.split("\\$");
        String data = splitData[1];
        if(data.equals("null")) {
            return null;
        } else {
            return Float.parseFloat(data);
        }
    }
}
