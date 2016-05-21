package msgsystem;

import websocket.GameWebSocket;

/**
 * Created by vladislav on 22.05.16.
 */
public class MsgSendData extends MsgToWS {
    final String message;

    public MsgSendData(Address from, Address to, String message) {
        super(from, to);
        this.message = message;
    }

    @Override
    void exec(GameWebSocket gameWebSocket) {
        gameWebSocket.sendMessage(message);
    }
}
