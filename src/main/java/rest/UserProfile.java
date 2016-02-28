package rest;

import org.jetbrains.annotations.NotNull;

/**
 * @author esin88
 */
public class UserProfile {
    @NotNull
    private String login;
    @NotNull
    private String password;
    @NotNull
    private String email;

    private long userId;

    public UserProfile() {
        login = "";
        password = "";
        email = "";
        userId = -1;
    }

    public UserProfile(@NotNull String login, @NotNull String password, @NotNull String email, long id) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.userId = id;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull String login) {
        this.login = login;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    @NotNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public long getUserId() { return userId; }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}