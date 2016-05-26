package mechanics;

import org.jetbrains.annotations.Nullable;

/**
 * Created by vladislav on 19.04.16.
 */
public class Lobby {
    private final GameUser firstUser;
    private GameUser secondUser;

    boolean firstUserTurn;

    public Lobby(GameUser firstUser){
        this.firstUser = firstUser;
        firstUserTurn = true;
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

    public boolean isFirstUserTurn() {
        return firstUserTurn;
    }

    public boolean isSecondUserTurn() {
        return !firstUserTurn;
    }

    public void nextTurn() {
        firstUserTurn = !firstUserTurn;
    }

    public void setAllWaiting() {
        firstUser.setWaiting(true);
        secondUser.setWaiting(true);
    }
}
