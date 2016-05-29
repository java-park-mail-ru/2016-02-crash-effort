package websocket;

import msgsystem.MessageSystem;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by vladislav on 26.05.16.
 */
public class GameWebSocketService implements Runnable {
    private final ConcurrentLinkedQueue<GameWebSocket> sockets;
    private final MessageSystem messageSystem;

    public GameWebSocketService(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        sockets = new ConcurrentLinkedQueue<>();
    }

    public void start() {
        (new Thread(this)).start();
    }

    public void add(GameWebSocket gameWebSocket) {
        sockets.add(gameWebSocket);
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                for (final Iterator<GameWebSocket> it = sockets.iterator(); it.hasNext();) {
                    final GameWebSocket ws = it.next();
                    if (ws.isConnected()) {
                        messageSystem.execForSubscriber(ws);
                    } else {
                        it.remove();
                    }
                }

                Thread.sleep(MessageSystem.IDLE_TIME);
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
