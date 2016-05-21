package msgsystem;

import websocket.GameWebSocket;

/**
 * Created by vladislav on 22.05.16.
 */
public abstract class MsgToWS extends MsgBase {
    public MsgToWS(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Subscriber subscriber) {
        if (subscriber instanceof GameWebSocket) {
            exec((GameWebSocket) subscriber);
        }
    }

    abstract void exec(GameWebSocket gameWebSocket);
}
