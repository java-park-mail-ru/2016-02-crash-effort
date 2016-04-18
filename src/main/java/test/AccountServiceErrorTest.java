package test;

import main.AccountServiceDBImpl;
import main.UserProfile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import static main.Main.getProperty;
import static main.Main.loadProperties;

/**
 * Created by vladislav on 08.04.16.
 */
@SuppressWarnings("unused")
public class AccountServiceErrorTest extends Assert {

    AccountServiceDBImpl accountService;

    @Before
    public void setUp() {
        if (!loadProperties())
            System.exit(1);

        accountService = new AccountServiceDBImpl();
        final String dbName = getProperty("database");
        final String dbHost = getProperty("db_host");
        final int dbPort = Integer.valueOf(getProperty("db_port"));
        final String dbUsername = getProperty("db_username");
        final String dbPassword = getProperty("db_password");
        try {
            accountService.initialize(dbName, dbHost, dbPort, dbUsername, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @After
    public void tearDown() {
        try {
            accountService.close();
        } catch (SQLException e) {
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
