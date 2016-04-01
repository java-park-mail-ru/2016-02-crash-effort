package main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import rest.Session;
import rest.Users;
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

    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(String[] args) throws Exception {
        int port = -1;
        try {
            port = Integer.valueOf(args[0]);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Specify port");
            System.exit(1);
        }

        try (AccountServiceDBImpl accountService = new AccountServiceDBImpl()) {
            System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

            final Server server = new Server(port);
            final ServletContextHandler contextHandler = new ServletContextHandler(server, "/api/", ServletContextHandler.SESSIONS);
            final ResourceConfig config = new ResourceConfig(Session.class, Users.class);
            config.register(new AccountServiceAbstractBinder(accountService));
            final ServletHolder servletHolder = new ServletHolder(new ServletContainer(config));
            contextHandler.addServlet(servletHolder, "/*");

            final ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirectoriesListed(true);
            resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
            resourceHandler.setResourceBase("public_html");

            final HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[] { resourceHandler, contextHandler });
            server.setHandler(handlers);

            server.start();
            server.join();
        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}