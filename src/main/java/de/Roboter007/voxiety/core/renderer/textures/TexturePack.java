package de.Roboter007.voxiety.core.renderer.textures;

import de.Roboter007.voxiety.core.renderer.Asset;
import de.Roboter007.voxiety.core.renderer.components.Spritesheet;
import de.Roboter007.voxiety.core.renderer.shader.Shader;
import de.Roboter007.voxiety.utils.VoxAssets;
import de.Roboter007.voxiety.utils.VoxPaths;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TexturePack {

    private final String path;
    private final Map<String, Asset> assets;

    public TexturePack(String path) {
        this.path = path;
        this.assets = new HashMap<>();
    }

    public String path() {
        return path;
    }

    public Map<String, Asset> assets() {
        return assets;
    }

    public void load() {
        load(path);
    }

    public void load(String path) {
        File file = new File(path);
        if(file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File fileInFolder : files) {
                    if (fileInFolder.isDirectory()) {
                        load(fileInFolder.getPath());
                    } else {
                        loadGameElements(fileInFolder);
                    }
                }
            }
            //ToDo: Zip Dateien als Texturepacks zulassen
        } /*else if (VoxPaths.access().isZipFile(file)) {
            ZipFile zipFile = VoxPaths.access().toZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if()
            }
        } */
    }

    public void loadGameElements(File file) {
        if(isShader(file)) {
            loadShader(file);
        } else if(isSpritesheet(file)) {
            addSpritesheet(file.getAbsolutePath(), new Spritesheet(file.getAbsolutePath(), 16, 16, 8, 0));
        } else if(isTexture(file)) {
            loadTexture(file);
        } else if (isMusicFile(file)) {
            //ToDo: implement & copy more stuff over from VoxelSociety Project
            return;
        } else {
            System.out.println("Unidentified Object: " + file.getPath() + " in Texturepack: " + path);
        }
    }

    public boolean isShader(File file) {
        return file.getPath().contains(".glsl");
    }

    public boolean isTexture(File file) {
        return file.getPath().contains(".png") || file.getPath().contains(".jpg") || file.getPath().contains(".jpeg");
    }

    public boolean isSpritesheet(File file) {
        return isTexture(file) && file.getPath().contains("spritesheet");
    }

    public boolean isMusicFile(File file) {
        return file.getPath().contains(".mp3");
    }

    public void loadShader(File file) {
        if(!assets.containsKey(file.getAbsolutePath())) {
            createShader(file);
        } else {
            if(!(assets.get(file.getAbsolutePath()) instanceof Shader)) {
                createShader(file);
            }
        }
    }

    public Shader getShader(String path) {
        File file = new File(path);

        if(assets.containsKey(path)) {
            Asset asset = assets.get(path);
            if(asset instanceof Shader shader) {
                return shader;
            } else {
                return createShader(file);
            }
        } else {
            return createShader(file);
        }
    }

    public Shader createShader(File file) {
        Shader shader = new Shader(file.getAbsolutePath());
        shader.compile();
        assets.put(file.getAbsolutePath(), shader);
        return shader;
    }

    //ToDo: jetzt anderes handeln über TexturePacks
    // ehemals: getShaderResource
    /*public Shader getVoxietyShader(String name) {
        return getShader(VoxPaths.access().getResourcePath(Voxiety.ID + "/" + "shaders" +"/" + name + ".glsl"));
    } */

    public void loadTexture(File file) {
        if(!assets.containsKey(file.getAbsolutePath())) {
            createTexture(file);
        } else {
            if(!(assets.get(file.getAbsolutePath()) instanceof Texture)) {
                createTexture(file);
            }
        }
    }

    public Texture getTexture(String path) {
        File file = new File(path);

        if(assets.containsKey(path)) {
            Asset asset = assets.get(path);
            if(asset instanceof Texture texture) {
                return texture;
            } else {
                return createTexture(file);
            }
        } else {
            return createTexture(file);
        }
    }

    public Texture createTexture(File file) {
        Texture texture = new Texture(file.getPath());
        System.out.println("Loaded Texture '" + file.getPath() + "' with texID: " + texture.getID());
        assets.put(file.getAbsolutePath(), texture);
        return texture;
    }


    public void addSpritesheet(String resourceName, Spritesheet spritesheet) {
        File file = new File(resourceName);
        if(!assets.containsKey(file.getAbsolutePath())) {
            assets.put(file.getAbsolutePath(), spritesheet);
        } else {
            Asset asset = assets.get(path);
            if(!(asset instanceof Spritesheet)) {
                assets.put(file.getAbsolutePath(), spritesheet);
            }
        }
    }

    public Spritesheet getSpritesheet(String resourceName) {
        File file = new File(resourceName);
        if(!assets.containsKey(file.getAbsolutePath())) {
            System.out.println("Error: Tried to access spritesheet '" + resourceName + "' and it has not been added to asset pool");
        }
        return (Spritesheet) assets.getOrDefault(file.getAbsolutePath(), null);
        //ToDo: Spritesheet for not defined texture
    }

    public Asset locateAsset(String name) {
        for(String path : assets.keySet()) {
            if(path.contains(name)) {
                return assets.get(path);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "TexturePack{" +
                "path='" + path + '\'' +
                ", assets=" + assets +
                '}';
    }
}
