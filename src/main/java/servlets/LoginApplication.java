package servlets;

import dto.MyAppProperties;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.IOException;

import static dto.MyAppProperties.loadResourceConfig;

public class LoginApplication {
    public static Oauth2Service oauth2Service;
    public static void start() {
        MyAppProperties config = null;
        try {
            config = loadResourceConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assert config != null;
        oauth2Service = new Oauth2Service(config);
        Server server = new Server(config.getServerPort());
        ServletContextHandler handler = new ServletContextHandler(server, "/api/oauth2/otp");
        handler.addServlet(Handshake.class, "/handshake");
        handler.addServlet(Authorize.class, "/authorize");
        handler.addServlet(Verify.class, "/verify");
        handler.addServlet(Refresh.class, "/refresh");
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
