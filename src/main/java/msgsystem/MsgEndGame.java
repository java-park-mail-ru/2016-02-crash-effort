package msgsystem;

import org.json.JSONObject;
import websocket.GameWebSocket;

/**
 * Created by vladislav on 25.05.16.
 */
public class MsgEndGame extends MsgToWS {

    private final boolean win;
    private final boolean mana;

    public MsgEndGame(Address from, Address to, boolean win, boolean mana) {
        super(from, to);
        this.win = win;
        this.mana = mana;
    }

    @Override
    void exec(GameWebSocket gameWebSocket) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "endGame");
        jsonObject.put("win", win);
        jsonObject.put("mana", mana);
        gameWebSocket.sendMessage(jsonObject.toString());
    }
}
