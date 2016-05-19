package websocket;

import main.AccountService;
import mechanics.GameMechanics;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpSession;

/**
 * Created by vladislav on 19.04.16.
 */
public class GameWebSocketCreator implements WebSocketCreator {

    private final AccountService accountService;
    private final GameMechanics gameMechanics;

    GameWebSocketCreator(AccountService accountService, GameMechanics gameMechanics) {
        this.accountService = accountService;
        this.gameMechanics = gameMechanics;
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

        return new GameWebSocket(sessionId, accountService, gameMechanics);
    }
}
