package rest;

import org.json.JSONObject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Date;
import java.util.Map;

/**
 * Created by e.shubin on 25.02.2016.
 */
@Singleton
@Path("/user")
public class Users {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") String id, @Context HttpHeaders headers) {
        Map<String, Cookie> map = headers.getCookies();
        final UserProfile user = UserData.getAccountService().getUser(id);
        if (user == null || !(map.containsKey(Session.COOKIE_SESSION))) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {
            return Response.status(Response.Status.OK).entity(user.toJsonInfo()).build();
        }
    }

    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editUserById(UserProfile inUser, @PathParam("id") String id, @Context HttpHeaders headers) {
        UserProfile user = UserData.getAccountService().getUser(id);
        Map<String, Cookie> map = headers.getCookies();
        if(user == null || !(map.containsKey(Session.COOKIE_SESSION)
                && String.valueOf(UserData.getAccountService().getIdBySession(map.get(Session.COOKIE_SESSION).getValue())).equals(id))){
            return Response.status(Response.Status.FORBIDDEN).build();
        } else {
            user.setLogin(inUser.getLogin());
            user.setPassword(inUser.getPassword());
            user.setEmail(inUser.getEmail());
            return Response.status(Response.Status.OK).entity(user.getJsonId()).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserById(UserProfile inUser, @PathParam("id") String id, @Context HttpHeaders headers) {
        UserProfile user = UserData.getAccountService().getUser(id);
        Map<String, Cookie> map = headers.getCookies();
        if (user == null || !(map.containsKey(Session.COOKIE_SESSION)
                && String.valueOf(UserData.getAccountService().getIdBySession(map.get(Session.COOKIE_SESSION).getValue())).equals(id))){
            return Response.status(Response.Status.FORBIDDEN).build();
        } else {
            UserData.getAccountService().deleteUser(Long.valueOf(id));
            NewCookie cookie = new NewCookie(Session.COOKIE_SESSION, "", "/", "", 0, "", -1, new Date(0), false, false);
            return Response.status(Response.Status.OK).cookie(cookie).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserProfile inUser, @Context HttpHeaders headers) {
        if(UserData.getAccountService().addUser(inUser.getLogin(), inUser)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", UserProfile.getLastId());
            return Response.status(Response.Status.OK).entity(jsonObject.toString()).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}
