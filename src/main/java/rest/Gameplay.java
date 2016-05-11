package rest;

import main.ValidationHelper;
import mechanics.GameMechanicsImpl;
import org.json.JSONArray;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created by vladislav on 11.05.16.
 */
@Singleton
@Path("/gameplay")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Gameplay {
    @GET
    @Path("cards")
    public Response getCards(@Context HttpServletRequest request) {
        final Map<String, String[]> params = request.getParameterMap();
        final int count = params.containsKey("count") ? Integer.valueOf(params.get("count")[0]) : 0;

        if (count > 0) {
            final JSONArray jsonArray = GameMechanicsImpl.getRandomCards(count);
            return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(ValidationHelper.EMPTY_JSON).build();
        }
    }
}
