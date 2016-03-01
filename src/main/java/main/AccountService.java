package main;

import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author esin88
 */
public class AccountService {
    private Map<String, UserProfile> namesToProfile = new ConcurrentHashMap<>();
    private Map<Long, UserProfile> idToProfile = new ConcurrentHashMap<>();

    public AccountService() {
        idToProfile.put(UserProfile.getLastId(), new UserProfile("admin", "admin", "admin@admin.com"));
        idToProfile.put(UserProfile.getLastId(),new UserProfile("guest", "12345", "guest@guest.com"));
    }

    @Nullable
    public UserProfile addUser(String userName, UserProfile userProfile) {
        UserProfile user = getUserByLogin(userName);
        if (user != null)
            return null;
        long id = UserProfile.getLastId();
        user = new UserProfile(userProfile);
        idToProfile.put(id, user);
        return user;
    }

    @Nullable
    public UserProfile getUser(long id) {
          return idToProfile.get(id);
    }

    @Nullable
    public UserProfile getUserByLogin(String login) {
        for(Map.Entry<Long, UserProfile> entry : idToProfile.entrySet()) {
            UserProfile value = entry.getValue();
            if (value.getLogin().equals(login))
                return value;
        }
        return null;
    }

    public void deleteUser(long id) {
        idToProfile.remove(id);
    }

    public void login(String hash, UserProfile userProfile) { namesToProfile.put(hash, userProfile); }

    public UserProfile getUserBySession(String hash) { return namesToProfile.get(hash); }

    public boolean isLoggedIn(String hash) {
        return namesToProfile.containsKey(hash);
    }

    public void logout(String hash) {
        namesToProfile.remove(hash);
    }
}
