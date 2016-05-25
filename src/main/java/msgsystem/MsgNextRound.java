package msgsystem;

import org.json.JSONObject;
import websocket.GameWebSocket;

/**
 * Created by vladislav on 25.05.16.
 */
public class MsgNextRound extends MsgToWS {

    private final boolean turn;

    public MsgNextRound(Address from, Address to, boolean turn) {
        super(from, to);
        this.turn = turn;
    }

    @Override
    void exec(GameWebSocket gameWebSocket) {
        final JSONObject jsonObject = new JSONObject();

        jsonObject.put("command", "nextRound");
        jsonObject.put("turn", turn);

        gameWebSocket.sendMessage(jsonObject.toString());
    }
}
