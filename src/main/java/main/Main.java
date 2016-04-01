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

    public static boolean isNumeric(String s) {
        return s.matches("\\d+");
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(String[] args) throws Exception {
        int port = -1;
        if (args.length == 1 && isNumeric(args[0])) {
            port = Integer.valueOf(args[0]);
        } else {
            System.err.println("Specify port");
            System.exit(1);
        }

        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        final Server server = new Server(port);
        final ServletContextHandler contextHandler = new ServletContextHandler(server, "/api/", ServletContextHandler.SESSIONS);

        AccountService accountService;
        try {
            accountService = new AccountServiceDBImpl();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        final ResourceConfig config = new ResourceConfig(Session.class, Users.class);
        config.register(new AccountServiceAbstractBinder(accountService));
        final ServletHolder servletHolder = new ServletHolder(new ServletContainer(config));

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
        resourceHandler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resourceHandler, contextHandler });
        server.setHandler(handlers);

        contextHandler.addServlet(servletHolder, "/*");
        server.start();
        server.join();
    }
}