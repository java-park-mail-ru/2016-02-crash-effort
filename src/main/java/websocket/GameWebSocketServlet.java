package websocket;

import main.AccountService;
import msgsystem.Address;
import msgsystem.MessageSystem;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import javax.servlet.annotation.WebServlet;

/**
 * Created by vladislav on 19.04.16.
 */
@WebServlet(name = "GameWebSocketServlet", urlPatterns = {"/gameplay"})
public class GameWebSocketServlet extends WebSocketServlet {
    private static final int SOCKET_IDLE_TIME = 5 * 1000 * 60;

    private final AccountService accountService;
    private final MessageSystem messageSystem;
    private final Address addressGM;

    public GameWebSocketServlet(AccountService accountService, MessageSystem messageSystem, Address addressGM) {
        this.accountService = accountService;
        this.messageSystem = messageSystem;
        this.addressGM = addressGM;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.setCreator(new GameWebSocketCreator(accountService, messageSystem, addressGM));
        factory.getPolicy().setIdleTimeout(SOCKET_IDLE_TIME);
    }
}
