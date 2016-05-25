package msgsystem;

import mechanics.GameMechanics;
import org.json.JSONObject;

/**
 * Created by vladislav on 22.05.16.
 */
public class MsgInData extends MsgToGM {
    final String username;
    final JSONObject data;

    public MsgInData(Address from, Address to, String username, String data) {
        super(from, to);
        this.username = username;
        this.data = new JSONObject(data);
    }

    @Override
    void exec(GameMechanics gameMechanics) {
        gameMechanics.onMessage(username, data);
    }
}
