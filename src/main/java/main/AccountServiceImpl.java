package main;

import db.Database;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vladislav on 28.03.16.
 */
public class AccountServiceImpl implements AccountService {
    private Database database;

    public AccountServiceImpl(String name, String host, int port, String username, String password) throws SQLException, IOException {
        database = new Database(name, host, port, username, password);
    }

    @Override
    @Nullable
    public UserProfile addUser(UserProfile userProfile) {
        try {
            if (!userProfile.getImgData().isEmpty()) {
                final String filename = String.format("avatars/%s.png", userProfile.getLogin());
                try {
                    FileHelper.base64ToImage(userProfile.getImgData(), filename);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    return null;
                }
            }

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
                        resultToUserProfile(userProfile, result);
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
            database.execUpdate(String.format("INSERT INTO Session_User VALUES ('%s', %d) ON DUPLICATE KEY UPDATE user=%2$d", hash, userProfile.getId()));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
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
            return database.execQuery(String.format("SELECT 1 FROM Session_User WHERE session='%s'", hash), ResultSet::next);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    @Nullable
    public UserProfile getUserBySession(String hash) {
        final UserProfile userProfile = new UserProfile();
        try {
            database.execQuery(String.format("SELECT u.* FROM User u JOIN Session_User su ON u.id=su.user WHERE session='%s'", hash),
                    result -> {
                        result.next();
                        resultToUserProfile(userProfile, result);
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
                        resultToUserProfile(userProfile, result);
                    });
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return userProfile;
    }

    @Nullable
    @Override
    public Map<UserProfile, Integer> getScoreboard() {
        final Map<UserProfile, Integer> scoreboard = new HashMap<>();
        try {
            database.execQuery("SELECT * FROM User ORDER BY score DESC LIMIT 20",
                    result -> {
                        while (result.next()) {
                            final UserProfile userProfile = new UserProfile();
                            resultToUserProfile(userProfile, result);
                            scoreboard.put(userProfile, result.getInt("score"));
                        }
                    });
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return scoreboard;
    }

    @Override
    public boolean addUserScore(String login, int score) {
        try {
            database.execUpdate(String.format("UPDATE User SET score=score+%d WHERE login='%s'", score, login));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }


    private void resultToUserProfile(UserProfile userProfile, ResultSet resultSet) throws SQLException {
        userProfile.setId(resultSet.getLong("id"));
        userProfile.setLogin(resultSet.getString("login"));
        userProfile.setPassword(resultSet.getString("password"));
        userProfile.setEmail(resultSet.getString("email"));
    }
}
