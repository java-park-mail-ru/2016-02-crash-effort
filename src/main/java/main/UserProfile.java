package main;

import org.json.JSONObject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author esin88
 */
public class UserProfile {
    @NotNull
    @Size(min=1)
    private String login;

    @NotNull
    @Size(min=1)
    private String password;

    @NotNull
    @Size(min=1)
    private String email;

    private long userId;

    public UserProfile() {
        login = "";
        password = "";
        email = "";
        userId = -1;
    }

    @SuppressWarnings("unused")
    public UserProfile(UserProfile other) {
        login = other.login;
        password = other.password;
        email = other.email;
        userId = other.userId;
    }

    public UserProfile(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.userId = -1;
    }

    public String getLogin() { return login; }

    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public long getId() { return userId; }

    public void setId(long userId) { this.userId = userId; }

    public String getJsonId() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", userId);
        return jsonObject.toString();
    }

    public String getJsonInfo() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", userId);
        jsonObject.put("login", login);
        jsonObject.put("email", email);
        return jsonObject.toString();
    }

    @Override
    public String toString() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", userId);
        jsonObject.put("login", login);
        jsonObject.put("password", password);
        jsonObject.put("email", email);
        return jsonObject.toString();
    }
}