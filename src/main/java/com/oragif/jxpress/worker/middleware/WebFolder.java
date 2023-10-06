package com.oragif.jxpress.worker.middleware;

import com.oragif.jxpress.routing.Router;

import java.io.File;
import java.nio.file.Path;

public class WebFolder {

    public static String ensurePathFormat(String path) {
        if (path.length() == 1) return "/";
        if (path.lastIndexOf("/") == path.length() - 1) {
            path = path.substring(0, path.length() - 2);
        }
        path = path.replace("//", "/");
        return path;
    }

    public static void mapFolder(String path, File folder, Router router) {
        try {
            for (File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    String newPath = path + fileEntry.getName();
                    if (path.lastIndexOf("/") != path.length() - 1) newPath = path + "/" + fileEntry.getName();
                    mapFolder(newPath, fileEntry, router);
                } else {
                    router.use(ensurePathFormat(path + "/" + fileEntry.getName()), new FileReader(Path.of(folder.getPath() + "/" + fileEntry.getName())));
                }
            }
        } catch (NullPointerException e) {
            //
        }
    }
}
