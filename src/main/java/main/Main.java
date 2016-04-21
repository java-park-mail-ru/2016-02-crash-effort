package main;

import mechanics.GameMechanics;
import mechanics.GameMechanicsImpl;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import rest.Scoreboard;
import rest.Session;
import rest.Users;
import websocket.GameWebSocketServlet;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

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

    private static final Properties PROPERTIES = new Properties();

    public static String getProperty(String property) {
        return PROPERTIES.getProperty(property);
    }

    @SuppressWarnings({"OverlyBroadCatchBlock"})
    public static boolean loadProperties() {
        try (final FileInputStream fis = new FileInputStream("cfg/server.properties")) {
            PROPERTIES.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(String[] args) throws Exception {
        final boolean loaded = loadProperties();
        if (!loaded)
            System.exit(1);

        final int port = Integer.valueOf(getProperty("port"));
        final String dbName = getProperty("database");
        final String dbHost = getProperty("db_host");
        final int dbPort = Integer.valueOf(getProperty("db_port"));
        final String dbUsername = getProperty("db_username");
        final String dbPassword = getProperty("db_password");

        final AccountServiceDBImpl accountService = new AccountServiceDBImpl();

        try {
            accountService.initialize(dbName, dbHost, dbPort, dbUsername, dbPassword);
        } catch (SQLException e) {
            System.out.println("Database error:");
            System.out.println(e.getMessage());
            System.exit(1);
        }

        final GameMechanics gameMechanics = new GameMechanicsImpl();
        try {
            gameMechanics.loadCards();
        } catch (IOException e) {
            System.out.println("Game Mechanics error:");
            System.out.println(e.getMessage());
            System.exit(1);
        }

        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        final Server server = new Server(port);
        final ServletContextHandler contextHandler = new ServletContextHandler(server, "/api/", ServletContextHandler.SESSIONS);
        final ResourceConfig config = new ResourceConfig(Session.class, Users.class, Scoreboard.class);
        config.register(new AccountServiceAbstractBinder(accountService));
        contextHandler.addServlet(new ServletHolder(new ServletContainer(config)), "/*");
        contextHandler.addServlet(new ServletHolder(new GameWebSocketServlet(accountService, gameMechanics)), "/gameplay");
        server.setHandler(contextHandler);

        server.start();
        server.join();

        accountService.close();
    }
}