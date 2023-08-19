package application;


import dto.MyAppProperties;
import servlets.EmbeddedHttpServer;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) {
        String filePath = "/etc/java/BackTalk/settings.json";
        MyAppProperties config = null;
        try {
            config = MyAppProperties.loadExternalConfig(filePath);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        System.out.println("******************************************");
        EmbeddedHttpServer server = new EmbeddedHttpServer();
        server.start(config);
    }
}
