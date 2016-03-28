package rest;

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
        Long id;
        try {
            id = Long.valueOf(rawId);
        } catch (NumberFormatException e) {
            id = null;
        }
        return id;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") String id, @Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        final Long longId = parseId(id);
        if (longId == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(RestApplication.EMPTY_JSON).build();

        final UserProfile user = RestApplication.getAccountService().getUser(longId);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(RestApplication.EMPTY_JSON).build();
        }
        else if (!RestApplication.getAccountService().isLoggedIn(sessionId)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(RestApplication.EMPTY_JSON).build();
        } else {
            return Response.status(Response.Status.OK).entity(user.getJsonInfo()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserProfile inUser, @Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        if (!RestApplication.validate(inUser))
            return Response.status(Response.Status.BAD_REQUEST).entity(RestApplication.EMPTY_JSON).build();

        UserProfile user = RestApplication.getAccountService().addUser(inUser);
        if (user != null) {
            RestApplication.getAccountService().login(sessionId, user);
            return Response.status(Response.Status.OK).entity(user.getJsonId()).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity(RestApplication.EMPTY_JSON).build();
        }
    }

    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editUserById(UserProfile inUser, @PathParam("id") String id, @Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        if (!RestApplication.validate(inUser))
            return Response.status(Response.Status.BAD_REQUEST).entity(RestApplication.EMPTY_JSON).build();

        UserProfile loggedInUser = RestApplication.getAccountService().getUserBySession(sessionId);
        final Long longId = parseId(id);
        if (longId == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(RestApplication.EMPTY_JSON).build();

        if (loggedInUser != null && loggedInUser.getId() == longId) {
            RestApplication.getAccountService().editUser(loggedInUser, inUser);
            return Response.status(Response.Status.OK).entity(loggedInUser.getJsonId()).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity(RestApplication.EMPTY_JSON).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserById(@PathParam("id") String id, @Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        UserProfile loggedInUser = RestApplication.getAccountService().getUserBySession(sessionId);
        final Long longId = parseId(id);
        if (longId == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(RestApplication.EMPTY_JSON).build();

        if (loggedInUser != null && loggedInUser.getId() == longId) {
            RestApplication.getAccountService().logout(sessionId);
            RestApplication.getAccountService().deleteUser(longId);
            return Response.status(Response.Status.OK).entity(RestApplication.EMPTY_JSON).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity(RestApplication.EMPTY_JSON).build();
        }
    }
}
