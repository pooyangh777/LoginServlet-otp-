package application;


import dto.MyAppProperties;

import lombok.extern.slf4j.Slf4j;
import servlets.EmbeddedHttpServer;

import java.io.IOException;
import java.net.URISyntaxException;


@Slf4j
public class Main {
    public static void main(String[] args) {

//        String filePath = "/etc/java/talkback/settings.json";
        MyAppProperties config = null;
        try {
//            config = MyAppProperties.loadExternalConfig(filePath);
            config = MyAppProperties.loadResourceConfig();
        } catch (IOException e) {
            log.info("setting file is null");
        }
        log.info("***************************************************************************************");
        EmbeddedHttpServer server = new EmbeddedHttpServer();
        server.start(config);
    }
}
