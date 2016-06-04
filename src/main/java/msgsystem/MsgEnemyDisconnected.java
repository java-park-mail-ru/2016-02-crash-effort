package msgsystem;

import org.json.JSONObject;
import websocket.GameWebSocket;

/**
 * Created by vladislav on 26.05.16.
 */
public class MsgEnemyDisconnected extends MsgToWS {

    public MsgEnemyDisconnected(Address from, Address to) {
        super(from, to);
    }

    @Override
    void exec(GameWebSocket gameWebSocket) {
        final JSONObject jsonObject= new JSONObject();
        jsonObject.put("command", "enemyDisconnected");
        gameWebSocket.sendMessage(jsonObject.toString());
    }
}
