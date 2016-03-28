package main;

/**
 * Created by vladislav on 28.03.16.
 */
public interface AccountService {
    UserProfile addUser(UserProfile userProfile);
    UserProfile getUser(long id);
    void editUser(UserProfile user, UserProfile newData);
    void deleteUser(long id);
    void login(String hash, UserProfile userProfile);
    void logout(String hash);
    boolean isLoggedIn(String hash);
    UserProfile getUserBySession(String hash);
    UserProfile getUserByLogin(String login);
}
