package rest;

import main.AccountService;
import main.UserProfile;
import main.ValidationHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created by vladislav on 20.04.16.
 */
@Singleton
@Path("/scoreboard")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Scoreboard {
    @GET
    public Response scoreboard(@Context AccountService accountService) {
        final Map<UserProfile, Integer> scoreboard = accountService.getScoreboard();

        if (scoreboard != null) {
            final JSONArray jsonArray = new JSONArray();
            for (Map.Entry<UserProfile, Integer> entry : scoreboard.entrySet()) {
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", entry.getKey().getLogin());
                jsonObject.put("score", entry.getValue());
                jsonArray.put(jsonObject);
            }

            return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(ValidationHelper.EMPTY_JSON).build();
        }
    }
}
