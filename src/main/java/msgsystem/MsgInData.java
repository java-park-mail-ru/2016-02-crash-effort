package msgsystem;

import mechanics.GameMechanics;

/**
 * Created by vladislav on 22.05.16.
 */
public class MsgInData extends MsgToGM {
    final String username;
    final String data;

    public MsgInData(Address from, Address to, String username, String data) {
        super(from, to);
        this.username = username;
        this.data = data;
    }

    @Override
    void exec(GameMechanics gameMechanics) {
        gameMechanics.onMessage(username, data);
    }
}
