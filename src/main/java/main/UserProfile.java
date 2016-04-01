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

    @SuppressWarnings("InstanceVariableNamingConvention") //name need for API
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
        id = other.id;
    }

    public UserProfile(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.id = -1;
    }

    public String getLogin() { return login; }

    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

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