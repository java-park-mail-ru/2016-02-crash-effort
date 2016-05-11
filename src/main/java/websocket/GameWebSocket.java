package websocket;

import main.AccountService;
import main.UserProfile;
import mechanics.GameMechanics;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

/**
 * Created by vladislav on 19.04.16.
 */
@WebSocket
public class GameWebSocket {

    private final GameMechanics gameMechanics;
    private final AccountService accountService;
    private final String sessionId;

    private String username;
    private Session currentSession;

    GameWebSocket(String sessionId, AccountService accountService, GameMechanics gameMechanics) {
        this.accountService = accountService;
        this.gameMechanics = gameMechanics;
        this.sessionId = sessionId;
    }

    @SuppressWarnings("unused")
    @OnWebSocketMessage
    public void onMessage(Session session, String text) {
        if (username == null) return;

        gameMechanics.onMessage(username, text);
    }

    @SuppressWarnings("unused")
    @OnWebSocketConnect
    public void onConnect(Session session) {
        currentSession = session;
        final UserProfile user = accountService.getUserBySession(sessionId);
        if (user == null) {
            session.close(Response.SC_FORBIDDEN, "Your access to this resource is denied");
            return;
        }
        username = user.getLogin();
        if (gameMechanics.isRegistered(username)) {
            session.close(Response.SC_FORBIDDEN, "You has already opened session connected to this resource");
            return;
        }

        gameMechanics.registerUser(username, this);
    }

    @SuppressWarnings("unused")
    @OnWebSocketClose
    public void onDisconnect(int statusCode, String reason) {
        System.out.println("User disconnected with code " + statusCode + " by reason: " + reason);
        if (username != null)
            gameMechanics.unregisterUser(username);
    }

    public void sendMessage(String message) {
        try {
            currentSession.getRemote().sendString(message);
        } catch (IOException e) {
            System.out.println("WebSocket error: " + e.getMessage());
        }
    }

    public void disconnect() {
        currentSession.close();
    }

}
