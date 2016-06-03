package msgsystem;

import org.json.JSONObject;
import websocket.GameWebSocket;

/**
 * Created by vladislav on 25.05.16.
 */
public class MsgEndGame extends MsgToWS {

    private final boolean win;
    private final boolean mana;
    private final boolean rounds;

    public MsgEndGame(Address from, Address to, boolean win, boolean mana, boolean rounds) {
        super(from, to);
        this.win = win;
        this.mana = mana;
        this.rounds = rounds;
    }

    @Override
    void exec(GameWebSocket gameWebSocket) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "endGame");
        jsonObject.put("win", win);
        jsonObject.put("mana", mana);
        jsonObject.put("rounds", rounds);
        gameWebSocket.sendMessage(jsonObject.toString());
    }
}
