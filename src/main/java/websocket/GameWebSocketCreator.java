package websocket;

import main.AccountService;
import msgsystem.Address;
import msgsystem.MessageSystem;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by vladislav on 19.04.16.
 */
public class GameWebSocketCreator implements WebSocketCreator, Runnable {

    final AccountService accountService;
    final MessageSystem messageSystem;
    final Address addressGM;

    final ConcurrentLinkedQueue<GameWebSocket> sockets;

    GameWebSocketCreator(AccountService accountService, MessageSystem messageSystem, Address addressGM) {
        this.accountService = accountService;
        this.messageSystem = messageSystem;
        this.addressGM = addressGM;
        sockets = new ConcurrentLinkedQueue<>();
    }

    public void start() {
        (new Thread(this)).start();
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
        sockets.add(gameWebSocket);
        return gameWebSocket;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            for (final Iterator<GameWebSocket> it = sockets.iterator(); it.hasNext();) {
                final GameWebSocket ws = it.next();
                if (ws.isConnected()) {
                    messageSystem.execForSubscriber(ws);
                } else {
                    it.remove();
                }
            }

            try {
                Thread.sleep(MessageSystem.IDLE_TIME);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
