package mechanics;

import org.json.JSONArray;

/**
 * Created by vladislav on 20.04.16.
 */
public class GameUser {

    private final String username;
    private int health;
    private JSONArray cards;
    private final JSONArray deck;

    private int power;
    private JSONArray roundCards;

    private boolean waiting;
     private boolean connected;

    GameUser(String username, JSONArray cards) {
        this.username = username;
        //noinspection MagicNumber
        this.health = 50;
        connected = true;
        deck = new JSONArray(cards.toString());
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

    public JSONArray getCards() {
        return cards;
    }

    public void setCards(JSONArray cards) {
        this.cards = cards;
    }

    public JSONArray getDeck() {
        return deck;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
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
