package mechanics;

import org.json.JSONArray;

/**
 * Created by vladislav on 20.04.16.
 */
public class GameUser {

    private final String username;
    private int health;

    private int roundScores;
    private JSONArray roundCards;

    private boolean waiting;
     private boolean connected;

    GameUser(String username) {
        this.username = username;
        //noinspection MagicNumber
        this.health = 50;
        connected = true;
    }

    public String getUsername() {
        return username;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getRoundScores() {
        return roundScores;
    }

    public void setRoundScores(int roundScores) {
        this.roundScores = roundScores;
    }

    public JSONArray getRoundCards() {
        return roundCards;
    }

    public void setRoundCards(JSONArray roundCards) {
        this.roundCards = roundCards;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public void disconnect() {
        this.connected = false;
    }

    public boolean isConnected() {
        return connected;
    }
}
