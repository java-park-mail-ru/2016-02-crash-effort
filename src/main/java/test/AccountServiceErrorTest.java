package test;

import main.AccountService;
import main.AccountServiceDBImpl;
import main.Configuration;
import main.UserProfile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by vladislav on 08.04.16.
 */
@SuppressWarnings("unused")
public class AccountServiceErrorTest extends Assert {

    AccountService accountService;
    private static final String CONFIG = "cfg/server.properties";

    @Before
    public void setUp() {
        final String dbHost;
        final int dbPort;
        final String dbUsername;
        final String dbPassword;

        try {
            final Configuration configuration = new Configuration(CONFIG);

            dbHost = configuration.getString("db_host");
            dbPort = configuration.getInt("db_port");
            dbUsername = configuration.getString("db_username");
            dbPassword = configuration.getString("db_password");
        } catch (IOException | NotFoundException | NumberFormatException e) {
            System.out.println("Properties error:");
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }


        try {
            accountService = new AccountServiceDBImpl(dbHost, dbPort, dbUsername, dbPassword);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @After
    public void tearDown() {
        try {
            accountService.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Test
    public void testGetUser() {
        final long id = 0;
        final UserProfile userProfile = accountService.getUser(id);
        assertNull(userProfile);
    }

    @Test
    public void testLoggedIn() {
        final String hash = "12345";
        assertFalse(accountService.isLoggedIn(hash));
    }

    @Test
    public void testGetUserBySession() {
        final String hash = "12345";
        final UserProfile userProfile = accountService.getUserBySession(hash);
        assertNull(userProfile);
    }

    @Test
    public void testGetUserByLogin() {
        final String login = "$login$";
        final UserProfile userProfile = accountService.getUserByLogin(login);
        assertNull(userProfile);
    }
}
