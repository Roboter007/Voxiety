package de.Roboter007.voxiety.utils;

import de.Roboter007.voxiety.Voxiety;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class VoxPaths {

    private static VoxPaths voxPaths;

    public VoxPaths() {
    }

    public static VoxPaths access() {
        if(voxPaths == null) {
            voxPaths = new VoxPaths();
        }
        return voxPaths;
    }

    public Path getVoxelGamePath() {
        Path path = Path.of(System.getProperty("user.home") + "/AppData/Roaming/" + Voxiety.NAME);
        if(path.toFile().mkdirs()) {
            System.out.println("Created Game Path!");
        }
        return path;
    }

    public List<String> readLines(String path) {
        List<String> list = new ArrayList<>();
        try {
            final BufferedReader in = new BufferedReader(
                    new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                list.add(line);
            }
            in.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteFolder(File folder, Runnable taskOnDeletion) {
        File[] files = folder.listFiles();
        if(files!=null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f, null);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();

        if(taskOnDeletion != null) {
            taskOnDeletion.run();
        }
    }


    public File getResourceFile(String path) {
        URL url = this.getClass().getClassLoader().getResource(path);
        File file = null;
        if(url != null) {
            try {
                file = new File(url.toURI());
            } catch (URISyntaxException e) {
                file = new File(url.getPath());
            }
        }
        return file;
    }

    public String getResourcePath(String path) {
        if(path == null) {
            return null;
        }
        return getResourceFile(path).getPath();
    }

    public boolean isZipFile(File file) {
        if ((file == null) || !file.exists() || !file.isFile()) {
            return false;
        }
        try (ZipFile zipFile = new ZipFile(file)) {
            return true;

        } catch (ZipException zexc) {
            return false;

        } catch (IOException ioe) {
            throw new RuntimeException("File " + file + " did fail to open or close", ioe);
        }
    }

    public ZipFile toZipFile(File file) {
        if ((file == null) || !file.exists() || !file.isFile()) {
            return null;
        }
        try (ZipFile zipFile = new ZipFile(file)) {
            return zipFile;

        } catch (ZipException zexc) {
            return null;

        } catch (IOException ioe) {
            throw new RuntimeException("File " + file + " did fail to open or close", ioe);
        }
    }


}
