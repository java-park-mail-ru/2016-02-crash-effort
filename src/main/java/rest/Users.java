package rest;

import main.UserData;
import main.UserProfile;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Created by e.shubin on 25.02.2016.
 */
@Singleton
@Path("/user")
public class Users {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") String id, @Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        final UserProfile user = UserData.getAccountService().getUser(Long.valueOf(id));
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
        if (loggedInUser != null && loggedInUser.getId() == Long.valueOf(id)) {
            loggedInUser.setLogin(inUser.getLogin());
            loggedInUser.setPassword(inUser.getPassword());
            loggedInUser.setEmail(inUser.getEmail());
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
        long idLong = Long.valueOf(id);

        if (loggedInUser != null && loggedInUser.getId() == idLong) {
            UserData.getAccountService().logout(sessionId);
            UserData.getAccountService().deleteUser(idLong);
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
