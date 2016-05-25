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
    final AccountService accountService;
    final MessageSystem messageSystem;
    final Address addressGM;

    public GameWebSocketServlet(AccountService accountService, MessageSystem messageSystem, Address addressGM) {
        this.accountService = accountService;
        this.messageSystem = messageSystem;
        this.addressGM = addressGM;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        final GameWebSocketCreator gameWebSocketCreator = new GameWebSocketCreator(accountService, messageSystem, addressGM);
        gameWebSocketCreator.start();
        factory.setCreator(gameWebSocketCreator);
    }
}
