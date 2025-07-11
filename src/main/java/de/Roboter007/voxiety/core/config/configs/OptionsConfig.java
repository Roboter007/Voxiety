package de.Roboter007.voxiety.core.config.configs;

import de.Roboter007.voxiety.core.config.VoxConfig;
import de.Roboter007.voxiety.core.config.VoxConfigOption;
import de.Roboter007.voxiety.core.config.values.IntConfigValue;
import de.Roboter007.voxiety.utils.JavaUtils;

public class OptionsConfig extends VoxConfig {

    public OptionsConfig() {
        super(getDefaultConfigPath() + "/options.json", JavaUtils.arrayListOf(
                //ToDo: Replace with screen_fps_limit
                new VoxConfigOption<>("fps", new IntConfigValue(165)),
                new VoxConfigOption<>("general_volume", new IntConfigValue(100)),
                new VoxConfigOption<>("music_volume", new IntConfigValue(100)),
                new VoxConfigOption<>("sound_effect_volume", new IntConfigValue(100))));
    }

}
