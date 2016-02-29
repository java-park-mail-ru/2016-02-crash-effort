package main;

import org.jetbrains.annotations.Nullable;
import rest.UserProfile;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author esin88
 */
public class AccountService {
    private Map<Long, UserProfile> users = new ConcurrentHashMap<>();
    private Map<String, Long> hashes = new ConcurrentHashMap<>();

    public AccountService() {
        users.put(0L, new UserProfile("admin", "admin", "admin@admin.com"));
        users.put(1L, new UserProfile("guest", "12345", "guest@guest.com"));
    }

    public boolean addUser(String userName, UserProfile userProfile) {
        UserProfile user = getUserByLogin(userName);
        if (user != null)
            return false;
        user = new UserProfile(userProfile);
        users.put(user.getId(), user);
        return true;
    }

    public UserProfile getUser(String id) {
        return users.get(Long.valueOf(id));
    }

    @Nullable
    public UserProfile getUserByLogin(String login) {
        for(Map.Entry<Long, UserProfile> entry : users.entrySet()) {
            UserProfile value = entry.getValue();
            if (value.getLogin().equals(login))
                return value;
        }
        return null;
    }

    public void deleteUser(long id) {
        users.remove(id);
    }

    public void addSession(String hash, long id) {
        hashes.put(hash, id);
    }

    public void deleteSession(String hash) {
        hashes.remove(hash);
    }

    public long getIdBySession(String hash) {
        return hashes.get(hash);
    }

}
