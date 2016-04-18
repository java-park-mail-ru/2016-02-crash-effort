package rest;

import main.AccountService;
import main.UserProfile;
import main.ValidationHelper;
import org.jetbrains.annotations.Nullable;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import static main.ValidationHelper.isInvalid;

/**
 * Created by e.shubin on 25.02.2016.
 */
@Singleton
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
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
    public Response getUserById(@PathParam("id") String id, @Context HttpServletRequest request, @Context AccountService accountService) {
        final String sessionId = request.getSession().getId();
        final Long longId = parseId(id);
        if (longId == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(ValidationHelper.EMPTY_JSON).build();

        final UserProfile user = accountService.getUser(longId);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ValidationHelper.EMPTY_JSON).build();
        }
        else if (!accountService.isLoggedIn(sessionId)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ValidationHelper.EMPTY_JSON).build();
        } else {
            return Response.status(Response.Status.OK).entity(user.getJsonInfo()).build();
        }
    }

    @PUT
    public Response createUser(UserProfile inUser, @Context HttpServletRequest request, @Context AccountService accountService) {
        final String sessionId = request.getSession().getId();
        if (isInvalid(inUser))
            return Response.status(Response.Status.BAD_REQUEST).entity(ValidationHelper.EMPTY_JSON).build();

        final UserProfile user = accountService.addUser(inUser);
        if (user != null && accountService.login(sessionId, user)) {
            return Response.status(Response.Status.OK).entity(user.getJsonId()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(ValidationHelper.EMPTY_JSON).build();
        }
    }

    @POST
    @Path("{id}")
    public Response editUserById(UserProfile inUser, @PathParam("id") String id, @Context HttpServletRequest request, @Context AccountService accountService) {
        final String sessionId = request.getSession().getId();
        if (isInvalid(inUser))
            return Response.status(Response.Status.BAD_REQUEST).entity(ValidationHelper.EMPTY_JSON).build();

        final UserProfile loggedInUser = accountService.getUserBySession(sessionId);
        final Long longId = parseId(id);
        if (longId == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(ValidationHelper.EMPTY_JSON).build();

        if (loggedInUser != null && loggedInUser.getId() == longId &&
                accountService.editUser(loggedInUser, inUser)) {
            return Response.status(Response.Status.OK).entity(loggedInUser.getJsonId()).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ValidationHelper.EMPTY_JSON).build();
        }
    }

    @DELETE
    @Path("{id}")
    @SuppressWarnings("OverlyComplexBooleanExpression")
    public Response deleteUserById(@PathParam("id") String id, @Context HttpServletRequest request, @Context AccountService accountService) {
        final String sessionId = request.getSession().getId();
        final UserProfile loggedInUser = accountService.getUserBySession(sessionId);
        final Long longId = parseId(id);
        if (longId == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(ValidationHelper.EMPTY_JSON).build();

        if (loggedInUser != null && loggedInUser.getId() == longId &&
                accountService.logout(sessionId) && accountService.deleteUser(longId)) {
                return Response.status(Response.Status.OK).entity(ValidationHelper.EMPTY_JSON).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity(ValidationHelper.EMPTY_JSON).build();
        }
    }
}
