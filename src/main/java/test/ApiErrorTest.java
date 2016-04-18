package test;

import com.github.javafaker.Faker;
import main.Main.AccountServiceAbstractBinder;
import main.AccountServiceDBImpl;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;
import rest.Session;
import rest.Users;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import test.ApiSuccessTest.ServletAbstractBinder;
import static junit.framework.TestCase.assertEquals;
import static main.Main.loadProperties;
import static org.mockito.Mockito.mock;
import static main.Main.getProperty;

/**
 * Created by vladislav on 31.03.16.
 */
@SuppressWarnings("unused")
public class ApiErrorTest extends JerseyTest {
    Faker faker;

    @Override
    protected Application configure() {
        faker = new Faker();
        if (!loadProperties())
            System.exit(1);

        final AccountServiceDBImpl accountService = new AccountServiceDBImpl();
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
            return null;
        }

        final ResourceConfig config = new ResourceConfig(Session.class, Users.class);
        config.register(new AccountServiceAbstractBinder(accountService));
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        final HttpSession httpSession = mock(HttpSession.class);
        final String sessionId = faker.lorem().fixedString(15);
        Mockito.when(httpServletRequest.getSession()).thenReturn(httpSession);
        Mockito.when(httpSession.getId()).thenReturn(sessionId);

        config.register(new ServletAbstractBinder(httpServletRequest));
        return config;
    }

    @Test
    public void testErrorSignUp() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", "admin");
        jsonObject.put("password", "nimda");
        jsonObject.put("email", "ad@$.rw");
        final Response json = target("user").request(MediaType.APPLICATION_JSON).put(Entity.json(jsonObject.toString()));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), json.getStatus());
    }

    @Test
    public void testErrorSighIn() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", "someuser");
        jsonObject.put("password", "nimda");
        final Response json = target("session").request(MediaType.APPLICATION_JSON).put(Entity.json(jsonObject.toString()));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), json.getStatus());
    }

    @Test
    public void testErrorCheckAuth() {
        final Response json = target("session").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), json.getStatus());
    }

    @Test
    public void testErrorChangeUserInfo() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", faker.name().firstName());
        jsonObject.put("password", faker.name().lastName());
        jsonObject.put("email", faker.internet().emailAddress());
        final Response json = target("user").path("1").request(MediaType.APPLICATION_JSON).post(Entity.json(jsonObject.toString()));
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), json.getStatus());
    }

    @Test
    public void testErrorGetUserById() {
        final Response json = target("user").path("1000").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), json.getStatus());
    }

    @Test
    public void testErrorLogout() {
        final Response json = target("session").request(MediaType.APPLICATION_JSON).delete();
        assertEquals(json.getStatus(), Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void testErrorDelete() {
        final Response json = target("user").path("1").request(MediaType.APPLICATION_JSON).delete();
        assertEquals(json.getStatus(), Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void testErrorDeleteID() {
        final Response json = target("user").path("gawgwa").request(MediaType.APPLICATION_JSON).delete();
        assertEquals(json.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
    }
}
