package main;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author esin88
 */
public class UserProfile {
    private static final AtomicLong ID_GENETATOR = new AtomicLong();

    @NotNull
    private String login;
    @NotNull
    private String password;
    @NotNull
    private String email;

    @SuppressWarnings("all") //name need for API
    private long id;

    public UserProfile() {
        login = "";
        password = "";
        email = "";
        id = -1;
    }

    public UserProfile(UserProfile other) {
        login = other.login;
        password = other.password;
        email = other.email;
        id = ID_GENETATOR.getAndIncrement();
    }

    public UserProfile(@NotNull String login, @NotNull String password, @NotNull String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.id = ID_GENETATOR.getAndIncrement();
    }

    @NotNull
    public String getLogin() { return login; }

    public void setLogin(@NotNull String login) { this.login = login; }

    @NotNull
    public String getPassword() { return password; }

    public void setPassword(@NotNull String password) { this.password = password; }

    @NotNull
    public String getEmail() { return email; }

    public void setEmail(@NotNull String email) { this.email = email; }

    public long getId() { return id; }

    public void setId(long userId) { this.id = userId; }

    public String getJsonId() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        return jsonObject.toString();
    }

    public String toJsonInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("login", login);
        jsonObject.put("email", email);
        return jsonObject.toString();
    }
}