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
        if (user == null || !UserData.getAccountService().loggedIn(sessionId)) {
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
        UserProfile user = UserData.getAccountService().getUser(Long.valueOf(id));
        UserProfile loggedInUser = UserData.getAccountService().getUserBySession(sessionId);
        if (user != null && loggedInUser != null && user.equals(loggedInUser)) {
            user.setLogin(inUser.getLogin());
            user.setPassword(inUser.getPassword());
            user.setEmail(inUser.getEmail());
            return Response.status(Response.Status.OK).entity(user.getJsonId()).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserById(UserProfile inUser, @PathParam("id") String id, @Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        UserProfile user = UserData.getAccountService().getUser(Long.valueOf(id));
        UserProfile loggedInUser = UserData.getAccountService().getUserBySession(sessionId);

        if (user != null && loggedInUser != null && user.equals(loggedInUser)) {
            UserData.getAccountService().logout(sessionId);
            UserData.getAccountService().deleteUser(Long.valueOf(id));
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
