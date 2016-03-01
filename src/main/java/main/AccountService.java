package main;

import org.jetbrains.annotations.Nullable;
import rest.UserProfile;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author esin88
 */
public class AccountService {
    private Map<String, UserProfile> loggedInUsers = new ConcurrentHashMap<>();
    private Vector<UserProfile> registeredUsers = new Vector<>();

    public AccountService() {
        registeredUsers.add(new UserProfile("admin", "admin", "admin@admin.com"));
        registeredUsers.add(new UserProfile("guest", "12345", "guest@guest.com"));
    }

    @Nullable
    public UserProfile addUser(String userName, UserProfile userProfile) {
        UserProfile user = getUserByLogin(userName);
        if (user != null)
            return null;
        user = new UserProfile(userProfile);
        registeredUsers.add(user);
        return user;
    }

    @Nullable
    public UserProfile getUser(long id) {
        for(UserProfile value : registeredUsers) {
            if (value.getId() == id)
                return value;
        }
        return null;
    }

    @Nullable
    public UserProfile getUserByLogin(String login) {
        for(UserProfile value : registeredUsers) {
            if (value.getLogin().equals(login))
                return value;
        }
        return null;
    }

    public void deleteUser(long id) {
        int k = 0;
        for(UserProfile value : registeredUsers) {
            if (value.getId() == id) {
                registeredUsers.removeElementAt(k);
                break;
            }
            ++k;
        }
    }

    public void login(String hash, UserProfile userProfile) {
        loggedInUsers.put(hash, userProfile);
    }

    public UserProfile getUserBySession(String hash) {
        return loggedInUsers.get(hash);
    }

    public boolean loggedIn(String hash) {
        return loggedInUsers.containsKey(hash);
    }

    public void logout(String hash) {
        loggedInUsers.remove(hash);
    }
}
