package mechanics;

import org.jetbrains.annotations.Nullable;

/**
 * Created by vladislav on 19.04.16.
 */
public class Lobby {
    private final GameUser firstUser;
    private GameUser secondUser;

    private boolean firstUserTurn;
    private int round;

    private boolean gameEnd;

    public Lobby(GameUser firstUser){
        this.firstUser = firstUser;
        firstUserTurn = true;
        round = 1;
        gameEnd = false;
    }

    public GameUser getFirstUser() {
        return firstUser;
    }

    public void setSecondUser(GameUser secondUser) {
        this.secondUser = secondUser;
    }

    public GameUser getSecondUser() {
        return secondUser;
    }

    public GameUser getCurrentUser() {
        return (firstUserTurn) ? firstUser : secondUser;
    }

    @Nullable
    public GameUser getUserbyName(String username) {
        if (username.equals(firstUser.getUsername()))
            return firstUser;
        else if (secondUser != null && username.equals(secondUser.getUsername()))
            return secondUser;
        else
            return null;
    }

    @Nullable
    public GameUser getEnemybyName(String username) {
        if (username.equals(firstUser.getUsername()))
            return secondUser;
        else if (secondUser != null && username.equals(secondUser.getUsername()))
            return firstUser;
        else
            return null;
    }

    public GameUser getUserWithMaxHealth() {
        return (firstUser.getHealth() < secondUser.getHealth()) ? secondUser : firstUser;
    }

    public boolean isFirstUserTurn() {
        return firstUserTurn;
    }

    public boolean isSecondUserTurn() {
        return !firstUserTurn;
    }

    public void nextTurn() {
        firstUserTurn = !firstUserTurn;
    }

    public void nextRound() { ++round; }

    public boolean isLastRound() {
        return round > 4;
    }

    public boolean isGameEnd() {
        return gameEnd;
    }

    public void setGameEnd(boolean gameEnd) {
        this.gameEnd = gameEnd;
    }

    public void setAllWaiting() {
        firstUser.setWaiting(true);
        secondUser.setWaiting(true);
    }
}
