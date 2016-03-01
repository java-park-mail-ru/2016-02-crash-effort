package rest;

import main.UserData;
import main.UserProfile;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Created by vladislav on 28.02.16.
 */
@Singleton
@Path("/session")
public class Session {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAuth(@Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        UserProfile user = UserData.getAccountService().getUserBySession(sessionId);
        if (user != null) {
            return Response.status(Response.Status.OK).entity(user.getJsonId()).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(UserProfile inuser, @Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        UserProfile user = UserData.getAccountService().getUserByLogin(inuser.getLogin());
        if (user != null && inuser.getPassword().equals(user.getPassword())) {
            UserData.getAccountService().login(sessionId, user);
            return Response.status(Response.Status.OK).entity(user.getJsonId()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response logoutUser(@Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        UserProfile user = UserData.getAccountService().getUserBySession(sessionId);
        if (user != null) {
            UserData.getAccountService().logout(sessionId);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
