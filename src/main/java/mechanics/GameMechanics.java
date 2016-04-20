package mechanics;

import websocket.GameWebSocket;
import java.io.IOException;

/**
 * Created by vladislav on 19.04.16.
 */
public interface GameMechanics {
    void loadCards() throws IOException;
    void registerUser(String userName, GameWebSocket gameWebSocket);
    void unregisterUser(String userName);
    boolean isRegistered(String username);
    void onMessage(String username, String message);
}
