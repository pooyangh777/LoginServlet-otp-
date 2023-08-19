package servlets;

import dto.MyAppProperties;
import filter.OtpFilter;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.File;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class EmbeddedHttpServer {
    private Server server;
    private QueuedThreadPool pool;
    private ArrayBlockingQueue queue = new ArrayBlockingQueue(2000);
    public Oauth2Service oauth2Service;

    public void start(MyAppProperties config) {
        assert config != null;
        oauth2Service = new Oauth2Service(config);
        pool = new QueuedThreadPool(
                2000,
                1000,
                30000,
                queue);
        server = new Server(pool);
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(8043);
        http_config.setOutputBufferSize(32768);
        http_config.setSendServerVersion(false);

        List<Connector> connectors = new ArrayList<>();
        ServerConnector httpConnector = new ServerConnector(server, new HttpConnectionFactory(http_config));
        httpConnector.setPort(8080);
        httpConnector.setIdleTimeout(30000);
        connectors.add(httpConnector);
        System.out.println("Async HTTP connector started. HTTP port: " + 8080);

        File keyFile = new File(getClass().getClassLoader().getResource("keystore.jks").getFile());
        ;
        if (keyFile != null && keyFile.exists()) {
            try {
                // print available ciphers
                SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
                String[] defaultCiphers = ssf.getDefaultCipherSuites();
                String[] availableCiphers = ssf.getSupportedCipherSuites();
                TreeMap ciphers = new TreeMap();
                for (String availableCipher : availableCiphers) {
                    ciphers.put(availableCipher, Boolean.FALSE);
                }
                for (String defaultCipher : defaultCiphers) {
                    ciphers.put(defaultCipher, Boolean.TRUE);
                }
                System.out.println("*** Available ciphers ***\nDefault\tCipher");
                for (Object o : ciphers.entrySet()) {
                    Map.Entry cipher = (Map.Entry) o;
                    if (Boolean.TRUE.equals(cipher.getValue())) {
                        System.out.print('*');
                    } else {
                        System.out.print(' ');
                    }
                    System.out.print('\t');
                    System.out.println(cipher.getKey());
                }

                SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
                sslContextFactory.setKeyStorePath(keyFile.getAbsolutePath());
                sslContextFactory.setKeyStorePassword("123456");
                sslContextFactory.setIncludeCipherSuites("TLS_ECDHE_.*", "TLS_EMPTY_RENEGOTIATION_INFO_SCSV", "TLS_RSA_.*");
                sslContextFactory.setExcludeCipherSuites(".*NULL.*", ".*RC4.*", ".*MD5.*", ".*DES.*", ".*DSS.*", "TLS_DHE_.*");
                sslContextFactory.setIncludeProtocols("TLSv1", "TLSv1.1", "TLSv1.2", "SSLv2Hello");
                sslContextFactory.setExcludeProtocols("SSL", "SSLv3");
                sslContextFactory.setRenegotiationAllowed(false);   //prevent attack

                HttpConfiguration https_config = new HttpConfiguration(http_config);
                https_config.addCustomizer(new SecureRequestCustomizer());
                https_config.setSendServerVersion(false);
                // Add SSL/TLS connection factory

                ServerConnector httpsConnector = new ServerConnector(server,
                        new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                        new HttpConnectionFactory(https_config));
                httpsConnector.setPort(8043);
                httpsConnector.setAcceptQueueSize(100);
                httpsConnector.setIdleTimeout(30000);
                connectors.add(httpsConnector);
                System.out.println("Async HTTPS connector started. HTTPS port: " + 8043);

                // Add connector to server
                server.setConnectors(connectors.toArray(new Connector[0]));
                server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", 1000000000);
                ServletContextHandler handler = new ServletContextHandler(server, "/api/oauth2/otp");
                handler.addFilter(OtpFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
                handler.addServlet(Handshake.class, "/handshake");
                handler.addServlet(Authorize.class, "/authorize");
                handler.addServlet(Verify.class, "/verify");
                handler.addServlet(Refresh.class, "/refresh");
            } catch (Exception ex) {
                System.out.println("Can not initialize SSL" + ex);
            }
        } else {
            System.out.println("Async HTTPS not initialized because keystore is not available");
        }

        try {
            server.start();
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        try {
            server.stop();
        } catch (Exception e) {
            System.out.println("An exception occurred " + e);
        }
    }
    QueuedThreadPool getPool() {
        return pool;
    }

    public ArrayBlockingQueue getQueue() {
        return queue;
    }
}
