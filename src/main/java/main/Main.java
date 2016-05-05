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

    @SuppressWarnings("OverlyBroadThrowsClause")
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
            accountService = new AccountServiceDBImpl(configuration.getDbName(), configuration.getDbHost(), configuration.getDbPort(),
                    configuration.getDbUsername(), configuration.getDbPassword());
        } catch (SQLException | IOException e) {
            System.out.println("Database error:");
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        final GameMechanics gameMechanics;
        try {
            gameMechanics = new GameMechanicsImpl();
        } catch (IOException e) {
            System.out.println("Game Mechanics error:");
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        System.out.append("Starting at port: ").append(String.valueOf(configuration.getPort())).append('\n');

        final Server server = new Server(configuration.getPort());
        final ServletContextHandler contextHandler = new ServletContextHandler(server, "/api", ServletContextHandler.SESSIONS);
        final ResourceConfig config = new ResourceConfig(Session.class, Users.class, Scoreboard.class);
        config.register(new AccountServiceAbstractBinder(accountService));
        contextHandler.addServlet(new ServletHolder(new ServletContainer(config)), "/*");
        contextHandler.addServlet(new ServletHolder(new GameWebSocketServlet(accountService, gameMechanics)), "/gameplay");
        server.setHandler(contextHandler);

        server.start();
        server.join();
    }
}