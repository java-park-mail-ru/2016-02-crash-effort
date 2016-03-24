package main;

import org.hibernate.validator.constraints.NotEmpty;
import org.jetbrains.annotations.Contract;
import org.json.JSONObject;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author esin88
 */
public class UserProfile {
    private static final AtomicLong ID_GENETATOR = new AtomicLong();

    @Contract(pure = true)
    public static long getLastId() { return ID_GENETATOR.get(); }

    @NotEmpty
    private String login;
    @NotEmpty
    private String password;
    @NotEmpty
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

    public UserProfile(@NotEmpty String login, @NotEmpty String password, @NotEmpty String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.id = ID_GENETATOR.getAndIncrement();
    }

    @NotEmpty
    public String getLogin() { return login; }

    public void setLogin(@NotEmpty String login) { this.login = login; }

    @NotEmpty
    public String getPassword() { return password; }

    public void setPassword(@NotEmpty String password) { this.password = password; }

    @NotEmpty
    public String getEmail() { return email; }

    public void setEmail(@NotEmpty String email) { this.email = email; }

    public long getId() { return id; }

    public void setId(long userId) { this.id = userId; }

    public String getJsonId() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        return jsonObject.toString();
    }

    public String getJsonInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("login", login);
        jsonObject.put("email", email);
        return jsonObject.toString();
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("login", login);
        jsonObject.put("password", password);
        jsonObject.put("email", email);
        return jsonObject.toString();
    }
}