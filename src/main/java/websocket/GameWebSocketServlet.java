package websocket;

import main.AccountService;
import mechanics.GameMechanics;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import javax.servlet.annotation.WebServlet;

/**
 * Created by vladislav on 19.04.16.
 */
@WebServlet(name = "GameWebSocketServlet", urlPatterns = {"/gameplay"})
public class GameWebSocketServlet extends WebSocketServlet {
    private final AccountService accountService;
    private final GameMechanics gameMechanics;

    public GameWebSocketServlet(AccountService accountService, GameMechanics gameMechanics) {
        this.accountService = accountService;
        this.gameMechanics = gameMechanics;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.setCreator(new GameWebSocketCreator(accountService, gameMechanics));
    }
}
