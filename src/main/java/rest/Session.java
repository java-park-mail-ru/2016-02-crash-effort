package rest;

import org.json.JSONObject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Cookie;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

/**
 * Created by vladislav on 28.02.16.
 */
@Singleton
@Path("/session")
public class Session {
    public static final String COOKIE_SESSION = "session";

    private String getHash(HttpHeaders headers, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String toHash = headers.getHeaderString("User-Agent") + password;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(toHash.getBytes("UTF-8"));

        return (new HexBinaryAdapter()).marshal(md.digest());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAuth(@Context HttpHeaders headers) {
        Map<String, Cookie> map = headers.getCookies();
        Response.ResponseBuilder responseBuilder;
        if (map.containsKey(COOKIE_SESSION)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", UserData.getAccountService().getIdBySession(map.get(COOKIE_SESSION).getValue()));
            responseBuilder = Response.status(Response.Status.OK).entity(jsonObject.toString());
        } else {
            responseBuilder = Response.status(Response.Status.UNAUTHORIZED);
        }

        return responseBuilder.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(UserProfile inuser, @Context HttpHeaders headers) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UserProfile user = UserData.getAccountService().getUserByLogin(inuser.getLogin());
        if (user != null && inuser.getPassword().equals(user.getPassword())) {
            String hash = getHash(headers, user.getPassword());
            UserData.getAccountService().addSession(hash, user.getId());
            NewCookie cookie = new NewCookie(COOKIE_SESSION, hash, "/", "", "", -1, false);
            return Response.status(Response.Status.OK).entity(user.getJsonId()).cookie(cookie).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response logoutUser(@Context HttpHeaders headers) {
        Map<String, Cookie> map = headers.getCookies();
        Response.ResponseBuilder responseBuilder;
        if (map.containsKey(COOKIE_SESSION)) {
            NewCookie cookie = new NewCookie(COOKIE_SESSION, "", "/", "", 0, "", -1, new Date(0), false, false);
            UserData.getAccountService().deleteSession(map.get(COOKIE_SESSION).getValue());
            responseBuilder = Response.status(Response.Status.OK).cookie(cookie);
        } else {
            responseBuilder = Response.status(Response.Status.UNAUTHORIZED);
        }

        return responseBuilder.build();
    }
}
