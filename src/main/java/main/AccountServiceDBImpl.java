package main;

import db.Database;
import org.jetbrains.annotations.Nullable;
import java.sql.SQLException;

/**
 * Created by vladislav on 28.03.16.
 */
public class AccountServiceDBImpl implements AccountService {
    Database database;
    private static final int DUPLICATE_ENTRY = 1062;

    @Override
    public void initialize() throws SQLException {
        database = new Database();
    }

    @Override
    public void close() throws SQLException {
        database.close();
    }

    @Override
    @Nullable
    public UserProfile addUser(UserProfile userProfile) {
        try {
            final int id = database.execUpdate(String.format("INSERT INTO User (login, password, email) VALUES ('%s', '%s', '%s')",
                    userProfile.getLogin(), userProfile.getPassword(), userProfile.getEmail()));
            userProfile.setId(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return userProfile;
    }

    @Override
    @Nullable
    public UserProfile getUser(long id) {
        final UserProfile userProfile = new UserProfile();
        try {
            database.execQuery(String.format("SELECT * FROM User WHERE id=%d", id),
                    result -> {
                        result.next();
                        userProfile.setId(result.getLong("id"));
                        userProfile.setLogin(result.getString("login"));
                        userProfile.setPassword(result.getString("password"));
                        userProfile.setEmail(result.getString("email"));
                    });
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return userProfile;
    }

    @Override
    public boolean editUser(UserProfile user, UserProfile newData) {
        try {
            database.execUpdate(String.format("UPDATE User SET login='%s', password='%s', email='%s' WHERE id=%d",
                    newData.getLogin(), newData.getPassword(), newData.getEmail(), user.getId()));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteUser(long id) {
        try {
            database.execUpdate(String.format("DELETE FROM User WHERE id=%d", id));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean login(String hash, UserProfile userProfile) {
        try {
            database.execUpdate(String.format("INSERT INTO Session_User VALUES ('%s', %d)", hash, userProfile.getId()));
        } catch (SQLException e) {
            if (e.getErrorCode() == DUPLICATE_ENTRY) {
                try {
                    database.execUpdate(String.format("UPDATE Session_User SET user=%d WHERE session='%s'", userProfile.getId(), hash));
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                    return false;
                }
            } else {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean logout(String hash) {
        try {
            database.execUpdate(String.format("DELETE FROM Session_User WHERE session='%s'", hash));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean isLoggedIn(String hash) {
        try {
            database.execQuery(String.format("SELECT * FROM Session_User WHERE session='%s'", hash),
                    result -> {
                        if (!result.next())
                            throw new SQLException();
                    });
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    @Nullable
    public UserProfile getUserBySession(String hash) {
        final UserProfile userProfile = new UserProfile();
        try {
            database.execQuery(String.format("SELECT u.* FROM User u JOIN Session_User su ON u.id=su.user WHERE session='%s'", hash),
                    result -> {
                        result.next();
                        userProfile.setId(result.getLong("id"));
                        userProfile.setLogin(result.getString("login"));
                        userProfile.setPassword(result.getString("password"));
                        userProfile.setEmail(result.getString("email"));
                    });
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return userProfile;
    }

    @Override
    @Nullable
    public UserProfile getUserByLogin(String login) {
        final UserProfile userProfile = new UserProfile();
        try {
            database.execQuery(String.format("SELECT * FROM User WHERE login='%s'", login),
                    result -> {
                        result.next();
                        userProfile.setId(result.getLong("id"));
                        userProfile.setLogin(result.getString("login"));
                        userProfile.setPassword(result.getString("password"));
                        userProfile.setEmail(result.getString("email"));
                    });
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return userProfile;
    }
}
