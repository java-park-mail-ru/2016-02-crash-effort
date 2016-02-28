package rest;

import main.AccountService;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Cookie;
import java.util.Date;
import java.util.Map;

/**
 * Created by vladislav on 28.02.16.
 */
@Singleton
@Path("/session")
public class Session {
    private AccountService accountService;
    final String cookieAuth = "auth";

    public Session(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAuth(@Context HttpHeaders headers) {
        Map<String, Cookie> map = headers.getCookies();
        Response.ResponseBuilder responseBuilder;
        if (map.containsKey(cookieAuth)) {
            String id = map.get(cookieAuth).getValue();
            String entity = "{ \"id\" : " + id + " }";
            responseBuilder = Response.status(Response.Status.OK).entity(entity);
        } else {
            responseBuilder = Response.status(Response.Status.UNAUTHORIZED);
        }

        return responseBuilder.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(UserProfile inuser, @Context HttpHeaders headers) {
        UserProfile user = accountService.getUserByLogin(inuser.getLogin());
        if (user != null && inuser.getPassword().equals(user.getPassword())) {
            NewCookie cookie = new NewCookie(cookieAuth, String.valueOf(user.getUserId()));

            return Response.status(Response.Status.OK).entity("{ \"id\" : " + user.getUserId() + " }").cookie(cookie).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response logoutUser(@Context HttpHeaders headers) {
        Map<String, Cookie> map = headers.getCookies();
        Response.ResponseBuilder responseBuilder;
        if (map.containsKey(cookieAuth)) {
            Cookie inCookie = map.get(cookieAuth);
            NewCookie cookie = new NewCookie(inCookie, "logout", -1, new Date(0), false, false);
            responseBuilder = Response.status(Response.Status.OK).cookie(cookie);
        } else {
            responseBuilder = Response.status(Response.Status.UNAUTHORIZED);
        }

        return responseBuilder.build();
    }
}
