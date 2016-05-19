package test;

import main.AccountService;
import main.AccountServiceImpl;
import main.Configuration;
import main.UserProfile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        final Configuration configuration;
        try {
            configuration = new Configuration(CONFIG);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Properties error:");
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        try {
            accountService = new AccountServiceImpl(configuration.getDbName(), configuration.getDbHost(), configuration.getDbPort(),
                    configuration.getDbUsername(), configuration.getDbPassword());
        } catch (SQLException | IOException e) {
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
