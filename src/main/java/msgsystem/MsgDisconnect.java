package msgsystem;

import websocket.GameWebSocket;

/**
 * Created by vladislav on 22.05.16.
 */
public class MsgDisconnect extends MsgToWS {

    public MsgDisconnect(Address from, Address to) {
        super(from, to);
    }

    @Override
    void exec(GameWebSocket gameWebSocket) {
        gameWebSocket.disconnect();
    }
}
