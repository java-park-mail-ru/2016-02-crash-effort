package main;

import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author esin88
 */
public class AccountServiceImpl implements AccountService {
    private Map<String, UserProfile> sessionToProfile = new ConcurrentHashMap<>();
    private Map<Long, UserProfile> idToProfile = new ConcurrentHashMap<>();
    private Map<String, UserProfile> loginToProfile = new ConcurrentHashMap<>();

    public AccountServiceImpl() {
        UserProfile example1 = new UserProfile(0, "admin", "admin", "admin@admin.com");
        UserProfile example2 = new UserProfile(1, "guest", "12345", "guest@guest.com");
        idToProfile.put(0L, example1);
        idToProfile.put(1L, example2);
        loginToProfile.put(example1.getLogin(), example1);
        loginToProfile.put(example2.getLogin(), example2);
    }

    @Override
    @Nullable
    public UserProfile addUser(UserProfile userProfile) {
        UserProfile user = getUserByLogin(userProfile.getLogin());
        if (user != null)
            return null;
        long id = -1;//UserProfile.getLastId();
        user = new UserProfile(userProfile);
        idToProfile.put(id, user);
        loginToProfile.put(user.getLogin(), user);
        return user;
    }

    @Override
    @Nullable
    public UserProfile getUser(long id) {
          return idToProfile.get(id);
    }

    @Override
    @Nullable
    public UserProfile getUserByLogin(String login) {
        return loginToProfile.get(login);
    }

    @Override
    public boolean editUser(UserProfile user, UserProfile newData) {
        loginToProfile.remove(user.getLogin());

        user.setLogin(newData.getLogin());
        user.setEmail(newData.getEmail());
        user.setPassword(newData.getPassword());

        loginToProfile.put(user.getLogin(), user);
        return true;
    }

    @Override
    public boolean deleteUser(long id) {
        String login = idToProfile.get(id).getLogin();
        idToProfile.remove(id);
        loginToProfile.remove(login);
        return true;
    }

    @Override
    public boolean login(String hash, UserProfile userProfile) {
        sessionToProfile.put(hash, userProfile);
        return true;
    }

    @Override
    public UserProfile getUserBySession(String hash) { return sessionToProfile.get(hash); }

    @Override
    public boolean isLoggedIn(String hash) {
        return sessionToProfile.containsKey(hash);
    }

    @Override
    public boolean logout(String hash) {
        sessionToProfile.remove(hash);
        return true;
    }
}
