package application;


import dto.MyAppProperties;
import servlets.LoginApplication;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) {
        System.out.println(args.toString());
        if (args.length == 0) {
            try {
                throw new Exception("You must set a config json file path.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        MyAppProperties config = null;
        try {
            config = MyAppProperties.loadExternalConfig(args[0]);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        System.out.println("******************************************");
        LoginApplication.start(config);
    }
}
