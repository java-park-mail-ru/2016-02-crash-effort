package websocket;

import main.AccountService;
import main.UserProfile;
import msgsystem.*;
import org.eclipse.jetty.server.Response;
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
public class GameWebSocket implements Subscriber {
    private static final int SOCKET_IDLE_TIME = 5 * 1000 * 60;

    private final AccountService accountService;
    private final String sessionId;
    private String username;
    private Session currentSession;
    private boolean connected;

    private final MessageSystem messageSystem;
    private final Address address;
    private final Address addressGM;

    public GameWebSocket(String sessionId, AccountService accountService, MessageSystem messageSystem, Address addressGM) {
        this.accountService = accountService;
        this.messageSystem = messageSystem;
        this.sessionId = sessionId;
        address = new Address();
        this.addressGM = addressGM;
    }

    @SuppressWarnings("unused")
    @OnWebSocketMessage
    public void onMessage(Session session, String data) {
        if (username == null) return;

        final MsgBase msgData = new MsgInData(address, addressGM, username, data);
        messageSystem.sendMessage(msgData);
    }

    @SuppressWarnings("unused")
    @OnWebSocketConnect
    public void onConnect(Session session) {
        connected = true;
        currentSession = session;
        currentSession.setIdleTimeout(SOCKET_IDLE_TIME);
        final UserProfile user = accountService.getUserBySession(sessionId);
        if (user == null) {
            session.close(Response.SC_FORBIDDEN, "Your access to this resource is denied");
            return;
        }
        username = user.getLogin();

        final MsgBase messageRegister = new MsgRegister(address, addressGM, username);
        messageSystem.sendMessage(messageRegister);
    }

    @SuppressWarnings("unused")
    @OnWebSocketClose
    public void onDisconnect(int statusCode, String reason) {
        connected = false;

        System.out.println("User disconnected with code " + statusCode + " by reason: " + reason);
        if (username != null) {
            final MsgBase messageUnregister = new MsgUnregister(address, addressGM, username);
            messageSystem.sendMessage(messageUnregister);
        }
    }

    public void sendMessage(String message) {
        try {
            currentSession.getRemote().sendString(message);
        } catch (IOException e) {
            System.out.println("WebSocket error: " + e.getMessage());
        }
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public boolean isConnected() {
        return connected;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

}
