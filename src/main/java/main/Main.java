package main;

import mechanics.GameMechanicsImpl;
import msgsystem.MessageSystem;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import rest.Scoreboard;
import rest.Session;
import rest.Users;
import websocket.GameWebSocketServlet;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author esin88
 */
public class Main {

    public static class AccountServiceAbstractBinder extends AbstractBinder {
        private final AccountService accountService;

        public AccountServiceAbstractBinder(AccountService accountService) {
            this.accountService = accountService;
        }

        @Override
        protected void configure() {
            bind(accountService).to(AccountService.class);
        }
    }

    private static final String CONFIG = "cfg/server.properties";

    @SuppressWarnings({"OverlyBroadThrowsClause", "IOResourceOpenedButNotSafelyClosed"})
    public static void main(String[] args) throws Exception {
        final Configuration configuration;
        try {
            configuration = new Configuration(CONFIG);

        } catch (IOException | NumberFormatException e) {
            System.out.println("Properties error:");
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        final AccountService accountService;
        try {
            accountService = new AccountServiceImpl(configuration.getDbName(), configuration.getDbHost(), configuration.getDbPort(),
                    configuration.getDbUsername(), configuration.getDbPassword());
        } catch (SQLException | IOException e) {
            System.out.println("Database error:");
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        final MessageSystem messageSystem = new MessageSystem();

        final GameMechanicsImpl gameMechanics;
        try {
            gameMechanics = new GameMechanicsImpl(messageSystem, accountService);
            gameMechanics.start();
        } catch (IOException e) {
            System.out.println("Game Mechanics error:");
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        System.out.append("Starting at port: ").append(String.valueOf(configuration.getPort())).append('\n');

        final Server server = new Server(configuration.getPort());
        //noinspection IOResourceOpenedButNotSafelyClosed,resource
        final ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setPort(configuration.getPort());

        final HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());

        final SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath("cfg/keystore.jks");
        sslContextFactory.setKeyStorePassword("123456");
        sslContextFactory.setKeyManagerPassword("123456");

        //noinspection IOResourceOpenedButNotSafelyClosed,resource
        final ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(https));
        sslConnector.setPort(configuration.getSslPort());

        server.setConnectors(new Connector[] { serverConnector, sslConnector });

        final ServletContextHandler contextHandler = new ServletContextHandler(server, "/api", ServletContextHandler.SESSIONS);
        final ResourceConfig config = new ResourceConfig(Session.class, Users.class, Scoreboard.class);
        config.register(new AccountServiceAbstractBinder(accountService));
        contextHandler.addServlet(new ServletHolder(new ServletContainer(config)), "/*");
        contextHandler.addServlet(new ServletHolder(new GameWebSocketServlet(accountService, messageSystem, gameMechanics.getAddress())), "/gameplay");
        server.setHandler(contextHandler);

        server.start();
        server.join();

        serverConnector.close();
        sslConnector.close();
    }
}