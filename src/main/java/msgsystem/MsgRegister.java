package msgsystem;

import mechanics.GameMechanics;

/**
 * Created by vladislav on 22.05.16.
 */
public class MsgRegister extends MsgToGM {
    private final String username;

    public MsgRegister(Address from, Address to, String username) {
        super(from, to);
        this.username = username;
    }

    @Override
    void exec(GameMechanics gameMechanics) {
        boolean result = false;
        if (!gameMechanics.isRegistered(username)) {
            gameMechanics.registerUser(username, getFrom());
            result = true;
        }
        final MsgBase back = new MsgIsRegistered(getTo(), getFrom(), result);
        ((Subscriber)gameMechanics).getMessageSystem().sendMessage(back);
    }
}
