package test;

import com.github.javafaker.Faker;
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
 * Created by vladislav on 06.04.16.
 */
@SuppressWarnings("unused")
public class AccountServiceSuccessTest extends Assert {

    Faker faker;
    AccountService accountService;
    private static final String CONFIG = "cfg/server.properties";

    @Before
    public void setUp() {
        faker = new Faker();

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
    public void testAddUser() {
        final UserProfile userProfile = new UserProfile(faker.name().firstName(), faker.name().lastName(), faker.internet().emailAddress());
        accountService.addUser(userProfile);
        assertNotNull(userProfile);
    }

    @Test
    public void testGetUser() {
        final long id = 1;
        final UserProfile userProfile = accountService.getUser(id);
        assertNotNull(userProfile);
        assertEquals("admin", userProfile.getLogin());
        assertEquals("admin", userProfile.getPassword());
    }

    @Test
    public void testEditUser() {
        final long id = 1;
        final UserProfile userProfile = accountService.getUser(id);
        assertNotNull(userProfile);
        UserProfile userProfile1 = new UserProfile("admin1", "admin1", "admin1@admin.com");
        assertTrue(accountService.editUser(userProfile, userProfile1));
        userProfile1 = new UserProfile("admin", "admin", "admin@admin.com");
        assertTrue(accountService.editUser(userProfile, userProfile1));
    }

    @Test
    public void testDeleteUser() {
        final long id = 1;
        assertTrue(accountService.deleteUser(id));
    }

    @Test
    public void testLogin() {
        final long id = 1;
        final UserProfile userProfile = accountService.getUser(id);
        assertNotNull(userProfile);
        final String hash = "12345";
        assertTrue(accountService.login(hash, userProfile));
    }

    @Test
    public void testLogout() {
        testLogin();
        final String hash = "12345";
        assertTrue(accountService.logout(hash));
    }

    @Test
    public void testLoggedIn() {
        testLogin();
        final String hash = "12345";
        assertTrue(accountService.isLoggedIn(hash));
    }

    @Test
    public void testGetUserBySession() {
        testLogin();
        final String hash = "12345";
        final UserProfile userProfile = accountService.getUserBySession(hash);
        assertNotNull(userProfile);
        assertEquals("admin", userProfile.getLogin());
        assertEquals("admin", userProfile.getPassword());
    }

    @Test
    public void testGetUserByLogin() {
        testLogin();
        final String login = "admin";
        final UserProfile userProfile = accountService.getUserByLogin(login);
        assertNotNull(userProfile);
        assertEquals(login, userProfile.getLogin());
        assertEquals(login, userProfile.getPassword());
    }
}
