package websocket;

import main.AccountService;
import msgsystem.Address;
import msgsystem.MessageSystem;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpSession;

/**
 * Created by vladislav on 19.04.16.
 */
public class GameWebSocketCreator implements WebSocketCreator {

    final AccountService accountService;
    final MessageSystem messageSystem;
    final Address addressGM;

    GameWebSocketCreator(AccountService accountService, MessageSystem messageSystem, Address addressGM) {
        this.accountService = accountService;
        this.messageSystem = messageSystem;
        this.addressGM = addressGM;
    }

    @Nullable
    @Override
    public GameWebSocket createWebSocket(ServletUpgradeRequest servletUpgradeRequest,
                                          ServletUpgradeResponse servletUpgradeResponse) {
        final HttpSession session = servletUpgradeRequest.getSession();
        if (session == null) {
            return null;
        }
        final String sessionId = session.getId();

        final GameWebSocket gameWebSocket = new GameWebSocket(sessionId, accountService, messageSystem, addressGM);
        gameWebSocket.start();
        return gameWebSocket;
    }
}
