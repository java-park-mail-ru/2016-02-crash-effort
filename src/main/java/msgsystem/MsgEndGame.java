package msgsystem;

import org.json.JSONObject;
import websocket.GameWebSocket;

/**
 * Created by vladislav on 25.05.16.
 */
public class MsgEndGame extends MsgToWS {

    private final boolean win;

    public MsgEndGame(Address from, Address to, boolean win) {
        super(from, to);
        this.win = win;
    }

    @Override
    void exec(GameWebSocket gameWebSocket) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "endGame");
        jsonObject.put("win", win);
        gameWebSocket.sendMessage(jsonObject.toString());
    }
}
