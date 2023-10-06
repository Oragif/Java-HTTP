package com.oragif.test;

import com.oragif.jxpress.JXpress;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JXpress jXpress = new JXpress("com.oragif.test");
        jXpress.listen(8080);
        jXpress.printRouteTree();
    }
}
