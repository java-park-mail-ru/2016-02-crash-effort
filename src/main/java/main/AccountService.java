package main;

import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author esin88
 */
public class AccountService {
    private Map<String, UserProfile> sessionToProfile = new ConcurrentHashMap<>();
    private Map<Long, UserProfile> idToProfile = new ConcurrentHashMap<>();
    private Map<String, UserProfile> loginToProfile = new ConcurrentHashMap<>();

    public AccountService() {
        long id1 = UserProfile.getLastId();
        UserProfile example1 = new UserProfile("admin", "admin", "admin@admin.com");
        long id2 = UserProfile.getLastId();
        UserProfile example2 = new UserProfile("guest", "12345", "guest@guest.com");
        idToProfile.put(id1, example1);
        idToProfile.put(id2, example2);
        loginToProfile.put(example1.getLogin(), example1);
        loginToProfile.put(example2.getLogin(), example2);
    }

    @Nullable
    public UserProfile addUser(String userName, UserProfile userProfile) {
        UserProfile user = getUserByLogin(userName);
        if (user != null)
            return null;
        long id = UserProfile.getLastId();
        user = new UserProfile(userProfile);
        idToProfile.put(id, user);
        loginToProfile.put(user.getLogin(), user);
        return user;
    }

    @Nullable
    public UserProfile getUser(long id) {
          return idToProfile.get(id);
    }

    @Nullable
    public UserProfile getUserByLogin(String login) {
        return loginToProfile.get(login);
    }

    public void editUser(UserProfile user, UserProfile newData) {
        loginToProfile.remove(user.getLogin());

        user.setLogin(newData.getLogin());
        user.setEmail(newData.getEmail());
        user.setPassword(newData.getPassword());

        loginToProfile.put(user.getLogin(), user);
    }

    public void deleteUser(long id) {
        String login = idToProfile.get(id).getLogin();
        idToProfile.remove(id);
        loginToProfile.remove(login);
    }

    public void login(String hash, UserProfile userProfile) { sessionToProfile.put(hash, userProfile); }

    public UserProfile getUserBySession(String hash) { return sessionToProfile.get(hash); }

    public boolean isLoggedIn(String hash) {
        return sessionToProfile.containsKey(hash);
    }

    public void logout(String hash) {
        sessionToProfile.remove(hash);
    }
}
