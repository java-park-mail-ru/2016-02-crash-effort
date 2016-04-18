package tests;

import com.github.javafaker.Faker;
import main.AccountServiceDBImpl;
import main.Main;
import main.Main.AccountServiceAbstractBinder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
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
import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;

/**
 * Created by vladislav on 30.03.16.
 */
public class ApiSuccessTest extends JerseyTest {

    public static class ServletAbstractBinder extends AbstractBinder {
        private final HttpServletRequest httpServletRequest;

        ServletAbstractBinder(HttpServletRequest httpServletRequest) {
            this.httpServletRequest = httpServletRequest;
        }

        @Override
        protected void configure() {
            bind(httpServletRequest).to(HttpServletRequest.class);
        }
    }

    Faker faker;

    @Override
    protected Application configure() {
        faker = new Faker();
        AccountServiceDBImpl accountService = new AccountServiceDBImpl();
        try {
            accountService.initialize();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }

        final ResourceConfig config = new ResourceConfig(Session.class, Users.class);
        config.register(new AccountServiceAbstractBinder(accountService));
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession httpSession = mock(HttpSession.class);
        final String sessionId = faker.lorem().fixedString(15);
        Mockito.when(httpServletRequest.getSession()).thenReturn(httpSession);
        Mockito.when(httpSession.getId()).thenReturn(sessionId);

        config.register(new ServletAbstractBinder(httpServletRequest));
        return config;
    }

    @Test
    public void testSignUp() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", faker.name().firstName());
        jsonObject.put("password", faker.name().lastName());
        jsonObject.put("email", faker.internet().emailAddress());
        final String json = target("user").request(MediaType.APPLICATION_JSON).put(Entity.json(jsonObject.toString()), String.class);
        assertFalse(json.equals(Main.EMPTY_JSON));
        assert(json.contains("id"));
    }

    @Test
    public void testSignIn() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", "admin");
        jsonObject.put("password", "admin");
        final String json = target("session").request(MediaType.APPLICATION_JSON).put(Entity.json(jsonObject.toString()), String.class);
        assertFalse(json.equals(Main.EMPTY_JSON));
        JSONObject id = new JSONObject(json);
        assert(id.has("id"));

        final String json1 = target("user").path(String.valueOf(id.getInt("id"))).request(MediaType.APPLICATION_JSON).get(String.class);
        assertFalse(json1.equals(Main.EMPTY_JSON));
        JSONObject jsonObject1 = new JSONObject(json1);
        assertEquals(jsonObject1.get("login"), "admin");
    }

    @Test
    public void testLogout() {
        testSignIn();
        final Response json = target("session").request(MediaType.APPLICATION_JSON).delete();
        assertEquals(json.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testEditUser() {
        testSignIn();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", faker.name().firstName());
        jsonObject.put("password", faker.name().lastName());
        jsonObject.put("email", faker.internet().emailAddress());
        final String json = target("user").path("1").request(MediaType.APPLICATION_JSON).post(Entity.json(jsonObject.toString()), String.class);
        assert(json.contains("id") && json.contains("1"));
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("login", "admin");
        jsonObject1.put("password", "admin");
        jsonObject1.put("email", "admin@admin.com");
        final String json1 = target("user").path("1").request(MediaType.APPLICATION_JSON).post(Entity.json(jsonObject1.toString()), String.class);
        assert(json1.contains("id") && json1.contains("1"));
    }

    @Test
    public void testCheckAuth() {
        testSignIn();
        final String json = target("session").request(MediaType.APPLICATION_JSON).get(String.class);
        assertNotNull(json);
        assert(json.contains("id") && json.contains("1"));
    }

    @Test
    public void testDelete() {
        testSignIn();
        final Response json = target("user").path("1").request(MediaType.APPLICATION_JSON).delete();
        assertEquals(json.getStatus(), Response.Status.OK.getStatusCode());
    }

}

