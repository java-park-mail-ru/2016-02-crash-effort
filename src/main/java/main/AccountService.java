package main;

import rest.UserProfile;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author esin88
 */
public class AccountService {
    private Map<Long, UserProfile> users = new ConcurrentHashMap<>();

    public AccountService() {
        long id = Incrementor.getNext();
        users.put(id, new UserProfile("admin", "admin", "admin@admin.com", id));
        id = Incrementor.getNext();
        users.put(id, new UserProfile("guest", "12345", "guest@guest.com", id));
    }

    public Collection<UserProfile> getAllUsers() {
        return users.values();
    }

    public boolean addUser(String userName, UserProfile userProfile) {
        UserProfile user = getUserByLogin(userName);
        if (user != null)
            return false;
        long newid = Incrementor.getNext();
        userProfile.setUserId(newid);
        users.put(newid, userProfile);
        return true;
    }

    public UserProfile getUser(String id) {
        return users.get(Long.valueOf(id));
    }

    public UserProfile getUserByLogin(String login) {
        for(Map.Entry<Long, UserProfile> entry : users.entrySet()) {
            UserProfile value = entry.getValue();
            if (value.getLogin().equals(login))
                return value;
        }
        return null;
    }
}
