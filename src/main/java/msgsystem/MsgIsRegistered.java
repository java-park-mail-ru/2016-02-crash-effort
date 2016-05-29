package msgsystem;

import org.eclipse.jetty.server.Response;
import websocket.GameWebSocket;

/**
 * Created by vladislav on 22.05.16.
 */
public class MsgIsRegistered extends MsgToWS {
    private final boolean result;

    public MsgIsRegistered(Address from, Address to, boolean result) {
        super(from, to);
        this.result = result;
    }

    @Override
    void exec(GameWebSocket gameWebSocket) {
        if (!result) {
            gameWebSocket.getCurrentSession().close(Response.SC_FORBIDDEN,
                    "You has already opened session connected to this resource");
        }
    }
}
