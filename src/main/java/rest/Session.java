package rest;

import main.*;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Context;

/**
 * Created by vladislav on 28.02.16.
 */
@Singleton
@Path("/session")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Session {

    @GET
    public Response checkAuth(@Context HttpServletRequest request, @Context AccountService accountService) {
        final String sessionId = request.getSession().getId();
        UserProfile user = accountService.getUserBySession(sessionId);
        if (user != null) {
            return Response.status(Response.Status.OK).entity(user.getJsonId()).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(RestApplication.EMPTY_JSON).build();
        }
    }

    @PUT
    public Response loginUser(UserProfile inuser, @Context HttpServletRequest request, @Context AccountService accountService) {
        final String sessionId = request.getSession().getId();

        UserProfile user = accountService.getUserByLogin(inuser.getLogin());
        if (user != null && inuser.getPassword().equals(user.getPassword()) &&
                accountService.login(sessionId, user)) {
            return Response.status(Response.Status.OK).entity(user.getJsonId()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(RestApplication.EMPTY_JSON).build();
        }
    }

    @DELETE
    public Response logoutUser(@Context HttpServletRequest request, @Context AccountService accountService) {
        final String sessionId = request.getSession().getId();
        UserProfile user = accountService.getUserBySession(sessionId);
        if (user != null && accountService.logout(sessionId)) {
            return Response.status(Response.Status.OK).entity(RestApplication.EMPTY_JSON).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(RestApplication.EMPTY_JSON).build();
        }
    }
}
