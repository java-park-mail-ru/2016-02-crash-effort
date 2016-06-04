package msgsystem;

import mechanics.GameMechanics;
import mechanics.GameMechanicsImpl;

/**
 * Created by vladislav on 22.05.16.
 */
public abstract class MsgToGM extends MsgBase {

    public MsgToGM(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Subscriber subscriber) {
        if (subscriber instanceof GameMechanicsImpl) {
            exec((GameMechanics) subscriber);
        }
    }

    abstract void exec(GameMechanics gameMechanics);
}
