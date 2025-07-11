package de.Roboter007.voxiety.core.config.values;

import de.Roboter007.voxiety.core.config.holder.StringHolder;

public class ByteConfigValue extends VoxelConfigValue<Byte> {


    public ByteConfigValue(Byte value) {
        super(value);
    }

    public ByteConfigValue(StringHolder stringHolder) {
        super(stringHolder);
    }

    @Override
    public String toString() {
        return "byte$" + this.getValue();
    }

    @Override
    public Byte fromString(String str) {
        String[] splitData = str.split("\\$");
        String data = splitData[1];
        if(data.equals("null")) {
            return null;
        } else {
            return Byte.parseByte(data);
        }
    }
}
