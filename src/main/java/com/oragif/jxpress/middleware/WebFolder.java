package com.oragif.jxpress.middleware;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

public class WebFolder {

    public static String ensurePathFormat(String path) {
        if (path.length() == 1) { return "/"; }

        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        path = path.replace("//", "/");

        return path;
    }

    public static HashMap<String, FileReader> mapFolder(String path, File folder) {
        HashMap<String, FileReader> files = new HashMap<>();
        if (folder == null || !folder.isDirectory()) { return null; }

        for (File fileEntry : folder.listFiles()) {
            String newPath = path.endsWith("/") ? path + fileEntry.getName() : path + "/" + fileEntry.getName();

            if (fileEntry.isDirectory()) {
                files.putAll(mapFolder(newPath, fileEntry));
            } else {
                String filePath = folder.getPath() + "/" + fileEntry.getName();
                files.put(ensurePathFormat(newPath), new FileReader(Path.of(filePath)));
            }
        }
        return files;
    }
}
