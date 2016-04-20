package mechanics;

import org.json.JSONArray;
import org.json.JSONObject;
import websocket.GameWebSocket;
import java.io.*;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by vladislav on 19.04.16.
 */
public class GameMechanicsImpl implements GameMechanics {

    private static final String CARDS = "cfg/cards.json";

    JSONArray cards;

    private final Map<String, Lobby> userToLobby = new ConcurrentHashMap<>();
    private final Map<String, GameWebSocket> userToSocket = new ConcurrentHashMap<>();
    Lobby vacantLobby;

    @SuppressWarnings("OverlyBroadThrowsClause")
    @Override
    public void loadCards() throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(CARDS))) {
            final StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            cards = new JSONArray(sb.toString());
        }
    }

    @Override
    public void registerUser(String userName, GameWebSocket gameWebSocket) {
        userToSocket.put(userName, gameWebSocket);
        if (vacantLobby == null) {
            vacantLobby = new Lobby(new GameUser(userName));
        } else {
            vacantLobby.setSecondUser(new GameUser(userName));
            userToLobby.put(vacantLobby.getFirstUser().getUsername(), vacantLobby);
            userToLobby.put(vacantLobby.getSecondUser().getUsername(), vacantLobby);

            sendStartGame();
            vacantLobby = null;
        }
    }

    @Override
    public void unregisterUser(String userName) {
        userToSocket.remove(userName);
        userToLobby.remove(userName);
    }

    @Override
    public boolean isRegistered(String username) {
        return userToSocket.containsKey(username);
    }

    @Override
    public void onMessage(String username, String message) {
        final Lobby lobby = userToLobby.get(username);
        final JSONObject jsonObject = new JSONObject(message);
        final String command = jsonObject.getString("command");
        switch (command) {
            case "nextTurn":
                nextTurn(lobby, jsonObject.getJSONArray("cards"));
                break;
            case "nextRound":
                nextRound(lobby, username);
                break;
            default:
                break;
        }
    }

    public void sendMessageToUser(String userName, String message) {
        userToSocket.get(userName).sendMessage(message);
    }

    private void sendStartGame() {
        final JSONObject jsonObject = new JSONObject();

        jsonObject.put("command", "start");
        jsonObject.put("turn", false);
        jsonObject.put("health", vacantLobby.getSecondUser().getHealth());
        final int cardsCount = 3;
        jsonObject.put("cards", getRandomCards(cardsCount));
        sendMessageToUser(vacantLobby.getSecondUser().getUsername(), jsonObject.toString());

        jsonObject.put("turn", true);
        jsonObject.put("health", vacantLobby.getFirstUser().getHealth());
        jsonObject.put("cards", getRandomCards(cardsCount));
        sendMessageToUser(vacantLobby.getFirstUser().getUsername(), jsonObject.toString());
    }

    private JSONArray getRandomCards(int count) {
        final JSONArray jsonArray = new JSONArray();
        final Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < count; ++i)
            jsonArray.put(cards.get(r.nextInt(cards.length())));
        return jsonArray;
    }

    private void nextTurn(Lobby lobby, JSONArray inCards) {
        setScore(lobby, inCards);

        final JSONObject jsonObject = new JSONObject();
        if (lobby.isFirstUserTurn()) {
            jsonObject.put("command", "nextTurn");
            jsonObject.put("turn", true);
            sendMessageToUser(vacantLobby.getSecondUser().getUsername(), jsonObject.toString());

            jsonObject.put("turn", false);
            jsonObject.put("newCards", getRandomCards(inCards.length()));
            sendMessageToUser(vacantLobby.getFirstUser().getUsername(), jsonObject.toString());
        } else if (lobby.isSecondUserTurn()) {
            jsonObject.put("command", "endRound");
            jsonObject.put("enemyCards", lobby.getSecondUser().getRoundCards());
            jsonObject.put("enemyScore", lobby.getSecondUser().getRoundScores());
            sendMessageToUser(vacantLobby.getFirstUser().getUsername(), jsonObject.toString());

            jsonObject.put("enemyCards", lobby.getFirstUser().getRoundCards());
            jsonObject.put("enemyScore", lobby.getFirstUser().getRoundScores());
            jsonObject.put("newCards", getRandomCards(inCards.length()));
            sendMessageToUser(vacantLobby.getSecondUser().getUsername(), jsonObject.toString());

            lobby.setAllWaiting();
            endRound(lobby);
        }
        lobby.nextTurn();
    }

    private void setScore(Lobby lobby, JSONArray inCards) {
        final GameUser gameUser = lobby.getCurrentUser();
        gameUser.setRoundScores(0);
        gameUser.setRoundCards(inCards);
        for (int i = 0; i < inCards.length(); ++i) {
            final JSONObject card = cards.getJSONObject(inCards.getInt(i));
            gameUser.setRoundScores(gameUser.getRoundScores() + card.getInt("power"));
        }
    }

    private void endRound(Lobby lobby) {
        final int score = lobby.getFirstUser().getRoundScores() - lobby.getSecondUser().getRoundScores();

        if (score > 0) {
            lobby.getSecondUser().setHealth(lobby.getSecondUser().getHealth() - score);
            if (lobby.getSecondUser().getHealth() < 1) {
                sendWin(lobby.getFirstUser());
                sendLose(lobby.getSecondUser());
            }
        } else {
            lobby.getFirstUser().setHealth(lobby.getFirstUser().getHealth() - score);
            if (lobby.getFirstUser().getHealth() < 1) {
                sendWin(lobby.getSecondUser());
                sendLose(lobby.getFirstUser());
            }
        }
    }

    private void nextRound(Lobby lobby, String username) {
        final GameUser gameUser = lobby.getUserbyName(username);
        if (gameUser == null) return;

        gameUser.setWaiting(false);
        if (!lobby.getFirstUser().isWaiting() && !lobby.getSecondUser().isWaiting())
            sendNextRound();
    }

    private void sendNextRound() {
        final JSONObject jsonObject = new JSONObject();

        jsonObject.put("command", "nextRound");
        jsonObject.put("turn", true);
        sendMessageToUser(vacantLobby.getFirstUser().getUsername(), jsonObject.toString());

        jsonObject.put("turn", false);
        sendMessageToUser(vacantLobby.getSecondUser().getUsername(), jsonObject.toString());
    }

    private void sendWin(GameUser user) {
        final JSONObject jsonObject = new JSONObject();

        jsonObject.put("command", "win");
        sendMessageToUser(user.getUsername(), jsonObject.toString());
        userToSocket.get(user.getUsername()).disconnect();
    }

    private void sendLose(GameUser user) {
        final JSONObject jsonObject = new JSONObject();

        jsonObject.put("command", "lose");
        sendMessageToUser(user.getUsername(), jsonObject.toString());
        userToSocket.get(user.getUsername()).disconnect();
    }
}
