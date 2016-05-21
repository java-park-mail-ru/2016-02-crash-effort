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
public class GameWebSocket implements Subscriber, Runnable {

    final AccountService accountService;
    final String sessionId;
    String username;
    Session currentSession;

    final MessageSystem messageSystem;
    final Address address;
    final Address addressGM;

    GameWebSocket(String sessionId, AccountService accountService, MessageSystem messageSystem, Address addressGM) {
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
        currentSession = session;
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

    public void disconnect() {
        currentSession.close();
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public void start() {
        (new Thread(this)).start();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            messageSystem.execForSubscriber(this);
            try {
                Thread.sleep(MessageSystem.IDLE_TIME);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
