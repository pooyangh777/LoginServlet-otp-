package servlets;

import dto.MyAppProperties;
import filter.OtpFilter;
import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.File;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
public class EmbeddedHttpServer {
    private Server server;
    private QueuedThreadPool pool;
    private ArrayBlockingQueue queue = new ArrayBlockingQueue(2000);
    public Oauth2Service oauth2Service;

    public void start(MyAppProperties config) {
        assert config != null;
        oauth2Service = new Oauth2Service(config);
        pool = new QueuedThreadPool(
                config.getHTTP_MAX_THREAD(),
                config.getHTTP_MIN_THREAD(),
                config.getHTTP_IDLE_TIMEOUT(),
                queue);
        server = new Server(pool);
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(config.getHTTPS_PORT());
        http_config.setOutputBufferSize(config.getHTTP_OUT_PUT_BUFFER_SIZE());
        http_config.setSendServerVersion(false);

        List<Connector> connectors = new ArrayList<>();
        ServerConnector httpConnector = new ServerConnector(server, new HttpConnectionFactory(http_config));
        httpConnector.setPort(config.getHTTP_PORT());
        httpConnector.setIdleTimeout(config.getHTTP_IDLE_TIMEOUT());
        connectors.add(httpConnector);
        log.info("Async HTTP connector started. HTTP port: " + config.getHTTP_PORT());

//        File keyFile = new File(getClass().getClassLoader().getResource("keystore.jks").getFile());
        File keyFile = new File(config.getKEYSTORE_PATH());
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
                log.info("*** Available ciphers ***\nDefault\tCipher");
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
                sslContextFactory.setKeyStorePassword(config.getKEYSTORE_PASSWORD());
                sslContextFactory.setIncludeCipherSuites(config.getHTTPS_INCLUDE_CIPHER());
                sslContextFactory.setExcludeCipherSuites(config.getHTTPS_EXCLUDE_CIPHER());
                sslContextFactory.setIncludeProtocols(config.getHTTPS_INCLUDE_PROTOCOLS());
                sslContextFactory.setExcludeProtocols(config.getHTTPS_EXCLUDE_PROTOCOLS());
                sslContextFactory.setRenegotiationAllowed(false);   //prevent attack

                HttpConfiguration https_config = new HttpConfiguration(http_config);
                https_config.addCustomizer(new SecureRequestCustomizer());
                https_config.setSendServerVersion(false);
                // Add SSL/TLS connection factory

                ServerConnector httpsConnector = new ServerConnector(server,
                        new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                        new HttpConnectionFactory(https_config));
                httpsConnector.setPort(config.getHTTPS_PORT());
                httpsConnector.setAcceptQueueSize(config.getHTTP_POOL_SIZE());
                httpsConnector.setIdleTimeout(config.getHTTP_IDLE_TIMEOUT());
                connectors.add(httpsConnector);
                log.info("Async HTTPS connector started. HTTPS port: " + config.getHTTPS_PORT());

                // Add connector to server
                server.setConnectors(connectors.toArray(new Connector[0]));
                server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", 1000000000);
                ServletContextHandler handler = new ServletContextHandler(server, "/api/oauth2/otp");
                handler.addFilter(OtpFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
                handler.addServlet(Handshake.class, "/handshake");
                handler.addServlet(Authorize.class, "/authorize");
                handler.addServlet(Verify.class, "/verify");
                handler.addServlet(Refresh.class, "/refresh");
                handler.addServlet(Status.class, "/status");
            } catch (Exception ex) {
                log.info("Can not initialize SSL" + ex);
            }
        } else {
            log.info("Async HTTPS not initialized because keystore is not available");
        }

        try {
            server.start();
            log.info("backTalk is running ...");
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        try {
            server.stop();
        } catch (Exception e) {
            log.info("An exception occurred " + e);
        }
    }

    QueuedThreadPool getPool() {
        return pool;
    }

    public ArrayBlockingQueue getQueue() {
        return queue;
    }
}
