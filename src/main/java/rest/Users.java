package rest;

import main.AccountService;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.Map;

/**
 * Created by e.shubin on 25.02.2016.
 */
@Singleton
@Path("/user")
public class Users {
    private AccountService accountService;
    final String cookieAuth = "auth";

    public Users(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        final Collection<UserProfile> allUsers = accountService.getAllUsers();
        return Response.status(Response.Status.OK).entity(allUsers.toArray(new UserProfile[allUsers.size()])).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") String id) {
        final UserProfile user = accountService.getUser(id);
        if(user == null){
            return Response.status(Response.Status.FORBIDDEN).build();
        } else {
            return Response.status(Response.Status.OK).entity(user).build();
        }
    }

    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editUserById(UserProfile inUser, @PathParam("id") String id, @Context HttpHeaders headers) {
        UserProfile user = accountService.getUser(id);
        Map<String, Cookie> map = headers.getCookies();
        if(user == null || !(map.containsKey(cookieAuth) && map.get(cookieAuth).getValue().equals(id))){
            return Response.status(Response.Status.FORBIDDEN).build();
        } else {
            user.setLogin(inUser.getLogin());
            user.setPassword(inUser.getPassword());
            user.setEmail(inUser.getEmail());
            return Response.status(Response.Status.OK).entity("{ \"id\" : " + user.getId() + " }").build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserById(UserProfile inUser, @PathParam("id") String id, @Context HttpHeaders headers) {
        UserProfile user = accountService.getUser(id);
        Map<String, Cookie> map = headers.getCookies();
        if(user == null || !(map.containsKey(cookieAuth) && map.get(cookieAuth).getValue().equals(id))){
            return Response.status(Response.Status.FORBIDDEN).build();
        } else {
            accountService.deleteUser(Long.valueOf(id));
            return Response.status(Response.Status.OK).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserProfile inUser, @Context HttpHeaders headers) {
        if(accountService.addUser(inUser.getLogin(), inUser)){
            return Response.status(Response.Status.OK).entity("{ \"id\" : " + inUser.getId() + " }").build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}
