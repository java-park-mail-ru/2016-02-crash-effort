package msgsystem;

import org.json.JSONArray;
import org.json.JSONObject;
import websocket.GameWebSocket;

/**
 * Created by vladislav on 25.05.16.
 */
public class MsgEndRound extends MsgToWS {

    private final int health;
    private final int enemyHealth;
    private final int power;
    private final int enemyPower;
    private final int enemyTransferCards;
    private final JSONArray enemyCards;

    public MsgEndRound(Address from, Address to, int health, int enemyHealth, int power, int enemyPower, int enemyTransferCards, JSONArray enemyCards) {
        super(from, to);
        this.health = health;
        this.enemyHealth = enemyHealth;
        this.power = power;
        this.enemyPower = enemyPower;
        this.enemyTransferCards = enemyTransferCards;
        this.enemyCards = enemyCards;
    }

    @Override
    void exec(GameWebSocket gameWebSocket) {
        final JSONObject jsonObject = new JSONObject();

        jsonObject.put("command", "endRound");
        jsonObject.put("health", health);
        jsonObject.put("enemyHealth", enemyHealth);
        jsonObject.put("power", power);
        jsonObject.put("enemyPower", enemyPower);
        jsonObject.put("enemyTransferCards", enemyTransferCards);
        jsonObject.put("enemyCards", enemyCards);

        gameWebSocket.sendMessage(jsonObject.toString());
    }
}
