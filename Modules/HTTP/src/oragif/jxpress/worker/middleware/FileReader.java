package oragif.jxpress.worker.middleware;

import oragif.jxpress.http.Request;
import oragif.jxpress.http.Response;

import java.nio.file.Path;

public class FileReader extends Middleware {
    private Path filePath;
    public FileReader(Path filePath) {
        this.filePath = filePath;
    }

    private String readFile() {
        java.io.FileReader fileReader = new java.io.FileReader();
    }

    @Override
    public void handle(Request request, Response response) {
        String fileContents = this.readFile();
        if (fileContents == null) return;
        response.setResponseCode(200);
        response.setContentType();
        response.send(fileContents);
    }
}
