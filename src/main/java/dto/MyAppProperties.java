package dto;

import application.Main;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MyAppProperties {
    private String serverUrl;
    private String clientId;
    private String clientSecret;
    private String apiToken;
    private String serverRedirectUrl;
    private String scope;
    private String otpSignature;
    private Integer serverPort;
    private String KEYSTORE_PATH;
    private String KEYSTORE_PASSWORD;
    private int HTTP_PORT;
    private int HTTPS_PORT;
    private String[] HTTPS_INCLUDE_PROTOCOLS;
    private String[] HTTPS_EXCLUDE_PROTOCOLS;
    private String[] HTTPS_INCLUDE_CIPHER;
    private String[] HTTPS_EXCLUDE_CIPHER;
    private int HTTP_OUT_PUT_BUFFER_SIZE;
    private int HTTP_IDLE_TIMEOUT;
    private int HTTP_POOL_SIZE;
    private int HTTP_MIN_THREAD;
    private int HTTP_MAX_THREAD;
    private int HTTP_QUEUE_SIZE;


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
        log.info("Loading config file from external.");
        return new Gson().fromJson(fileContent, MyAppProperties.class);
    }
}


