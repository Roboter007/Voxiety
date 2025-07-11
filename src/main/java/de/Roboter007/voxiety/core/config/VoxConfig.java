package de.Roboter007.voxiety.core.config;

import de.Roboter007.voxiety.Voxiety;
import de.Roboter007.voxiety.core.config.values.VoxelConfigValue;
import de.Roboter007.voxiety.utils.VoxPaths;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class VoxConfig {

    private final List<VoxConfigOption<?>> defaultOptions;
    private List<VoxConfigOption<?>> options;
    private final String path;
    private final File file;

    public VoxConfig(String path, List<VoxConfigOption<?>> defaultOptions) {
        this.defaultOptions = defaultOptions;
        this.path = path;
        this.file = new File(this.path);
        this.options = read();
        saveToFile();
    }

    public List<VoxConfigOption<?>> getDefaultOptions() {
        return defaultOptions;
    }

    public List<VoxConfigOption<?>> getOptions() {
        return options;
    }

    public File getFile() {
        return file;
    }

    public String getPath() {
        return path;
    }

    public void setOptions(List<VoxConfigOption<?>> options) {
        this.options = options;
    }


    public <T> void setOption(int opId, T newData) {
        VoxConfigOption<T> configOption = getConfigOption(opId);

        VoxelConfigValue<T> data = configOption.getConfigValue();
        data.setValue(newData);

        configOption.setConfigValue(data);

        this.options.set(opId, configOption);

        saveToFile();
    }

    public <T> void setOption(String optionKey, T newData) {
        int opId = getOpId(optionKey);
        if(opId == -1) {
            System.out.println("Error: the OptionKey - " + optionKey + " does not exist");
        } else {
            VoxConfigOption<T> configOption = getConfigOption(opId);

            VoxelConfigValue<T> data = configOption.getConfigValue();
            data.setValue(newData);

            configOption.setConfigValue(data);

            this.options.set(opId, configOption);

            saveToFile();
        }
    }

    public int getOpId(String optionKey){
        for(VoxConfigOption<?> voxConfigOption : this.options) {
            if(voxConfigOption.getName().equals(optionKey)) {
                return this.options.indexOf(voxConfigOption);
            }
        }
        return -1;
    }



    @SuppressWarnings("unchecked")
    public <T> T getValue(int opId) {
        return (T) this.options.get(opId).getConfigValue().getValue();
    }

    @SuppressWarnings("unchecked")
    public <T> VoxConfigOption<T> getConfigOption(int opId) {
        return (VoxConfigOption<T>) this.options.get(opId);
    }

    public <T> T getOptionWithFallback(int opId, T fallback) {
        T data = getValue(opId);

        if(data == null) {
            return fallback;
        } else {
            return data;
        }
    }

    public <T> T getOptionWithFallback(String optionKey, T fallback) {
        int opId = getOpId(optionKey);
        if(opId == -1) {
            System.out.println("Error: the OptionKey - " + optionKey + " does not exist");
            return fallback;
        } else {
            T data = getValue(opId);

            if (data == null) {
                return fallback;
            } else {
                return data;
            }
        }
    }

    public List<VoxConfigOption<?>> read() {
        List<VoxConfigOption<?>> options = new ArrayList<>();

        if(file != null) {
            List<String> dataList = VoxPaths.access().readLines(file.getPath());

            if (dataList.isEmpty()) {
                return defaultOptions;
            } else if (dataList.size() != defaultOptions.size()) {
                return upgradeConfig(dataList);
            } else {
                for (String data : dataList) {
                    VoxConfigOption<?> voxConfigOption = VoxConfigOption.newConfigValue(data);
                    if (voxConfigOption != null) {
                        options.add(voxConfigOption);
                    } else {
                        System.out.println("Wrong Format!");
                    }
                }
            }
        }

        return options;
    }

    public List<VoxConfigOption<?>> upgradeConfig(List<String> dataList) {
        System.out.println("Upgrade broken Config File...");
        if(dataList.size() < defaultOptions.size()) {
            List<VoxConfigOption<?>> optionsFromFile = new ArrayList<>();
            List<String> optionsNameFromFile = new ArrayList<>();
            for(String data : dataList) {
                VoxConfigOption<?> option = VoxConfigOption.newConfigValue(data);
                if(option != null) {
                    optionsFromFile.add(option);
                    optionsNameFromFile.add(option.getName());
                }
            }

            for(VoxConfigOption<?> voxConfigOption : defaultOptions) {
                String name = voxConfigOption.getName();
                if(!optionsNameFromFile.contains(name)) {
                    optionsFromFile.add(voxConfigOption);
                }
            }

            return optionsFromFile;
        } else {
            List<VoxConfigOption<?>> options = new ArrayList<>();

            HashMap<String, VoxConfigOption<?>> nameToOptionMap = new HashMap<>();
            for(String data : dataList) {
                VoxConfigOption<?> option = VoxConfigOption.newConfigValue(data);
                if(option != null) {
                    nameToOptionMap.put(option.getName(), option);
                }
            }

            for(VoxConfigOption<?> voxConfigOption : defaultOptions) {
                String name = voxConfigOption.getName();
                if(nameToOptionMap.containsKey(name)) {
                    VoxConfigOption<?> option = nameToOptionMap.get(name);
                    options.add(option);
                }
            }

            return options;
        }
    }



    public void saveToFile() {
        if(!file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                System.out.println("File could not be created!!!");
            }
        }

        List<String> lines = new ArrayList<>();


        for(VoxConfigOption<?> value : this.options) {
            lines.add(value.toString());
        }

        try {
            Files.write(Path.of(this.getPath()), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Path getDefaultConfigPath() {
        Path path = Path.of(System.getProperty("user.home") + "/AppData/Roaming/" + Voxiety.NAME + "/config");
        if(path.toFile().mkdirs()) {
            System.out.println("Created Config Path!");
        }
        return path;
    }


}
