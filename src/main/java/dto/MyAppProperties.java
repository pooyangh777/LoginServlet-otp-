package dto;

import application.Main;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
//import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyAppProperties {
    private String serverUrl;
    private String clientId;
    private String clientSecret;
    private String apiToken;
    private String serverRedirectUrl;
    private String scope;
    private String otpSignature;
    private Integer serverPort;
    private Boolean isOracle;
    private String databaseHost;
    private String databasePort;
    private String databaseName;
    private String databaseUsername;
    private String databasePassword;

    public static MyAppProperties loadResourceConfig() throws IOException, NullPointerException, InvalidPathException {
        InputStream inputStream = Main.class.getResourceAsStream("/config.json");
        byte[] resourceFileBytes = new byte[0];
        if (inputStream != null) {
            resourceFileBytes = IOUtils.toByteArray(inputStream);
        }
        String fileContent = new String(resourceFileBytes);
        return new Gson().fromJson(fileContent, MyAppProperties.class);
    }

    public static MyAppProperties loadExternalConfig(String jsonConfigFilePath) throws IOException, URISyntaxException, InvalidPathException {
//        Path jarDirPath = Paths.get(Main.class.getProtectionDomain()
//                .getCodeSource()
//                .getLocation()
//                .toURI()).getParent();
//        Path path = Paths.get(jarDirPath.toUri().resolve( "config.json"));
        Path path = Paths.get(jsonConfigFilePath);
        if (!Files.exists(path)) {
            return null;
        }
        byte[] externalConfigContent = Files.readAllBytes(path);
        String fileContent = new String(externalConfigContent);
        System.out.println("Loading config file from external.");
        return new Gson().fromJson(fileContent, MyAppProperties.class);
    }
}


