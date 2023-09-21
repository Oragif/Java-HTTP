package oragif.jxpress.worker.middleware;

import oragif.jxpress.http.Request;
import oragif.jxpress.http.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;

public class FileReader extends Middleware {
    private Path filePath;
    private String mimeType;
    public FileReader(Path filePath) {
        this.filePath = filePath;
    }

    private String readFile() {
        try {
            File file = this.filePath.toFile();
            Scanner scanner = new Scanner(file);
            StringBuilder fileContents = new StringBuilder();
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine());
            }
            scanner.close();
            this.mimeType = getMimeType(file);
            return fileContents.toString();
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public void handle(Request request, Response response) {
        String fileContents = this.readFile();
        if (fileContents == null) return;
        response.setResponseCode(200);
        response.setContentType(this.mimeType);
        response.send(fileContents);
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

    private static String getMimeType(File file) {
        String fileType = getFileExtension(file);

        return switch (fileType) {
            case ("html") -> "text/html";
            case ("js") -> "text/javascript";
            case ("json") -> "application/json";
            case ("png") -> "image/png";
            default -> "text/plain";
        };
    }
}
