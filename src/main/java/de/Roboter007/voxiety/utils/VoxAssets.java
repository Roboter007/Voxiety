package de.Roboter007.voxiety.utils;

import de.Roboter007.voxiety.Voxiety;
import de.Roboter007.voxiety.core.renderer.Asset;
import de.Roboter007.voxiety.core.renderer.components.Spritesheet;
import de.Roboter007.voxiety.core.renderer.textures.Texture;
import de.Roboter007.voxiety.core.renderer.textures.TexturePack;
import de.Roboter007.voxiety.main.VoxWindow;

import java.io.File;
import java.util.*;

public class VoxAssets {

    public static final String DEFAULT_PACK_PATH =  /* VoxPaths.access().getResourcePath(Voxiety.ID); */ VoxPaths.access().getVoxelGamePath() + "/texturepacks/voxiety";
    public static final String TEXTURE_PACK_FOLDER_PATH = VoxPaths.access().getVoxelGamePath() + "/texturepacks";

    private static final Map<String, TexturePack> texturePacks = new HashMap<>();

    public static void reloadTexturePacks() {
        if (!texturePacks.isEmpty()) {
            for(TexturePack texturePack : texturePacks.values()) {
                for(Asset asset : texturePack.assets().values()) {
                    if(asset instanceof Texture texture) {
                        texture.deleteTexture();
                    } else if (asset instanceof Spritesheet sheet) {
                        sheet.deleteSpritesheet();
                    }
                }

            }

            texturePacks.clear();
        }

        File texturePackFolder = new File(TEXTURE_PACK_FOLDER_PATH);
        if (!texturePackFolder.exists()) {
            if (texturePackFolder.mkdirs()) {
                System.out.println("Created Texture Pack Folder!");
            }
        } else {
            File[] files = texturePackFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        TexturePack texturePack = new TexturePack(file.getPath());
                        texturePack.load();
                        texturePacks.put(file.getPath(), texturePack);
                    }
                }
            }
        }

        TexturePack defaultPack = new TexturePack(DEFAULT_PACK_PATH);
        defaultPack.load();

        if(texturePacks.containsKey(DEFAULT_PACK_PATH)) {
            texturePacks.replace(DEFAULT_PACK_PATH, defaultPack);
        } else {
            texturePacks.put(DEFAULT_PACK_PATH, defaultPack);
        }

        VoxWindow.reloadFrame();
    }

    public static TexturePack getDefaultTP() {
        return texturePacks.get(DEFAULT_PACK_PATH);
    }

    //ToDo: die Methode so bearbeiten, dass die Texturen im TP-Ordner bevorzugt
    public static Asset getAsset(String name) {
        for(TexturePack texturePack : texturePacks.values()) {
            for(Asset asset : texturePack.assets().values()) {
                if(asset.path().contains(name)) {
                    File file = new File(asset.path());
                    if(file.exists() && file.isFile()) {
                        //System.out.println("Found Asset: " + asset.path());
                        return asset;
                    }
                }
            }
        }
        System.out.println("Missing Asset! Name: " + name);
        //ToDo: Texture for missing Textures
        return getDefaultTP().locateAsset("test.png");
    }

    public static Texture getTexture(String name) {
        for(TexturePack texturePack : texturePacks.values()) {
             return texturePack.getTexture(name);
        }
        return (Texture) getDefaultTP().locateAsset("test.png");
    }


}
