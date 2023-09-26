package oragif.test;

import oragif.jxpress.JXpress;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JXpress jXpress = new JXpress("oragif.test");
        jXpress.listen(8080);
        jXpress.printRouteTree();
    }
}
