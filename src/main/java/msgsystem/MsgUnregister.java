package msgsystem;

import mechanics.GameMechanics;

/**
 * Created by vladislav on 22.05.16.
 */
public class MsgUnregister extends MsgToGM {
    private final String username;

    public MsgUnregister(Address from, Address to, String username) {
        super(from, to);
        this.username = username;
    }

    @Override
    void exec(GameMechanics gameMechanics) {
        gameMechanics.unregisterUser(username);
    }
}
