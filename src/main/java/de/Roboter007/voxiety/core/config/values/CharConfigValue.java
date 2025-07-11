package de.Roboter007.voxiety.core.config.values;

import de.Roboter007.voxiety.core.config.holder.StringHolder;

public class CharConfigValue extends VoxelConfigValue<Character> {


    public CharConfigValue(Character value) {
        super(value);
    }

    public CharConfigValue(StringHolder stringHolder) {
        super(stringHolder);
    }

    @Override
    public String toString() {
        return "char$" + this.getValue();
    }

    @Override
    public Character fromString(String str) {
        String[] splitData = str.split("\\$");
        String data = splitData[1];
        if(data.equals("null")) {
            return null;
        } else {
            return data.charAt(0);
        }
    }
}
