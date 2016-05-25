package msgsystem;

import org.json.JSONObject;
import websocket.GameWebSocket;

/**
 * Created by vladislav on 25.05.16.
 */
public class MsgNextTurn extends MsgToWS {

    private final boolean turn;
    private final int cards;

    public MsgNextTurn(Address from, Address to, boolean turn, int cards) {
        super(from, to);
        this.turn = turn;
        this.cards = cards;
    }

    @Override
    void exec(GameWebSocket gameWebSocket) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "nextTurn");
        jsonObject.put("turn", turn);
        jsonObject.put("cards", cards);
        gameWebSocket.sendMessage(jsonObject.toString());
    }
}
