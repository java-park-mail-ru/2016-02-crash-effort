package rest;

import main.Main;
import main.UserData;
import main.UserProfile;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Created by e.shubin on 25.02.2016.
 */
@Singleton
@Path("/user")
public class Users {

    @Nullable
    private Long parseId(@NotNull String rawId) {
        if (Main.isNumeric(rawId))
            return Long.valueOf(rawId);
        else
            return null;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") String id, @Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        final Long longId = parseId(id);
        if (longId == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        final UserProfile user = UserData.getAccountService().getUser(longId);
        if (user == null || !UserData.getAccountService().isLoggedIn(sessionId)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {
            return Response.status(Response.Status.OK).entity(user.toJsonInfo()).build();
        }
    }

    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editUserById(UserProfile inUser, @PathParam("id") String id, @Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        UserProfile loggedInUser = UserData.getAccountService().getUserBySession(sessionId);
        final Long longId = parseId(id);
        if (longId == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        if (loggedInUser != null && loggedInUser.getId() == longId) {
            UserData.getAccountService().editUser(loggedInUser, inUser);
            return Response.status(Response.Status.OK).entity(loggedInUser.getJsonId()).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserById(UserProfile inUser, @PathParam("id") String id, @Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        UserProfile loggedInUser = UserData.getAccountService().getUserBySession(sessionId);
        final Long longId = parseId(id);
        if (longId == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        if (loggedInUser != null && loggedInUser.getId() == longId) {
            UserData.getAccountService().logout(sessionId);
            UserData.getAccountService().deleteUser(longId);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserProfile inUser) {
        UserProfile user = UserData.getAccountService().addUser(inUser.getLogin(), inUser);
        if (user != null) {
            return Response.status(Response.Status.OK).entity(user.getJsonId()).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}
