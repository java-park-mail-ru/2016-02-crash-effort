package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import rest.Session;
import rest.Users;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.sql.SQLException;
import java.util.Set;

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

    public static final String EMPTY_JSON = "{}";
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> boolean isInvalid(T object) {
        final Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(object);

        final int size = constraintViolations.size();
        if (size > 0) {
            System.out.println(object);
            System.out.println(String.format("Error count: %d", size));

            for (ConstraintViolation<T> cv : constraintViolations)
                System.out.println(String.format(
                        "ERROR! property: [%s], value: [%s], message: [%s]",
                        cv.getPropertyPath(), cv.getInvalidValue(), cv.getMessage()));

            System.out.println();
        }
        return !constraintViolations.isEmpty();
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

        final AccountService accountService = new AccountServiceDBImpl();
        try {
            accountService.initialize();
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR:");
            System.out.println(e.getMessage());
            System.exit(1);
        }

        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        final Server server = new Server(port);
        final ServletContextHandler contextHandler = new ServletContextHandler(server, "/api/", ServletContextHandler.SESSIONS);
        final ResourceConfig config = new ResourceConfig(Session.class, Users.class);
        config.register(new AccountServiceAbstractBinder(accountService));
        contextHandler.addServlet(new ServletHolder(new ServletContainer(config)), "/*");
        server.setHandler(contextHandler);

        server.start();
        server.join();

        accountService.close();
    }
}