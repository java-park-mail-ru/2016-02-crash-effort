package msgsystem;

import org.json.JSONArray;
import org.json.JSONObject;
import websocket.GameWebSocket;

/**
 * Created by vladislav on 25.05.16.
 */
public class MsgStartGame extends MsgToWS {

    private final boolean turn;
    private final int health;
    private final int enemyHealth;
    private final JSONArray cards;

    public MsgStartGame(Address from, Address to, boolean turn, int health, int enemyHealth, JSONArray cards) {
        super(from, to);
        this.turn = turn;
        this.health = health;
        this.enemyHealth = enemyHealth;
        this.cards = cards;
    }

    @Override
    void exec(GameWebSocket gameWebSocket) {
        final JSONObject jsonObject = new JSONObject();

        jsonObject.put("command", "start");
        jsonObject.put("turn", turn);
        jsonObject.put("health", health);
        jsonObject.put("enemyHealth", enemyHealth);
        jsonObject.put("cards", cards);
        gameWebSocket.sendMessage(jsonObject.toString());
    }
}
