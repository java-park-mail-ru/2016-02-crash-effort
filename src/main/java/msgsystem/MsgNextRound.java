package msgsystem;

import org.json.JSONArray;
import org.json.JSONObject;
import websocket.GameWebSocket;

/**
 * Created by vladislav on 25.05.16.
 */
public class MsgNextRound extends MsgToWS {

    private final boolean turn;
    private final JSONArray newCards;

    public MsgNextRound(Address from, Address to, boolean turn, JSONArray newCards) {
        super(from, to);
        this.turn = turn;
        this.newCards = newCards;
    }

    @Override
    void exec(GameWebSocket gameWebSocket) {
        final JSONObject jsonObject = new JSONObject();

        jsonObject.put("command", "nextRound");
        jsonObject.put("turn", turn);
        jsonObject.put("newCards", newCards);

        gameWebSocket.sendMessage(jsonObject.toString());
    }
}