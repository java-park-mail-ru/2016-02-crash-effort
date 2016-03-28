package main;

/**
 * Created by vladislav on 28.03.16.
 */
public interface AccountService {
    UserProfile addUser(UserProfile userProfile);
    UserProfile getUser(long id);
    boolean editUser(UserProfile user, UserProfile newData);
    boolean deleteUser(long id);
    boolean login(String hash, UserProfile userProfile);
    boolean logout(String hash);
    boolean isLoggedIn(String hash);
    UserProfile getUserBySession(String hash);
    UserProfile getUserByLogin(String login);
}
