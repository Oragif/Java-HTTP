package oragif.logger;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Logger {
    private String tag;
    public Logger(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private String logFormatter(String level, String... msg) {
        return "[" + level + "/" + this.tag + "] " + Arrays.stream(msg).collect(Collectors.joining(", "));
    }

    private void log(String level, String... msg) {
        System.out.println(logFormatter(level, msg));
    }
    public void log(String... msg) {
        this.log("LOG", msg);
    }

    public void info(String... msg) {
        this.log("INFO", msg);
    }

    public void warn(String... msg) {
        this.log("WARN", msg);
    }

    public void error(String... msg) {
        System.err.println(logFormatter("ERROR", msg));
    }
}
