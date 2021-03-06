package db;

import main.FileHelper;
import org.apache.commons.dbcp2.BasicDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by vladislav on 18.03.16.
 */
public class Database implements AutoCloseable {

    private static final int VERSION = 2;
    private final BasicDataSource dataSource;

    public Database(String name, String host, int port, String username, String password) throws SQLException, IOException {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(String.format("jdbc:mysql://%s:%d/%s?allowMultiQueries=true&useSSL=false", host, port, name));
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        initDatabase();
    }

    private void initDatabase() throws SQLException, IOException {
        execUpdate(getSQLScript(1));

        final int dbVersion = execQuery("SELECT version FROM Version",
                result -> {
                    result.next();
                    return result.getInt("version");
                });
        updateDatabase(dbVersion);
    }

    private void updateDatabase(int dbVersion) throws SQLException, IOException {
        for (int i = dbVersion + 1; i <= VERSION; ++i) {
            execUpdate(getSQLScript(i));
            execUpdate(String.format("UPDATE Version SET version=%d", i));
            System.out.printf("Database updated to version %d\n", i);
        }
    }

    private String getSQLScript(int num) throws IOException {
        return FileHelper.readAllText(String.format("sql/%d.sql", num));
    }

    public void execQuery(String query, ResultHandler handler) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(query);
                try (ResultSet result = stmt.getResultSet()) {
                    handler.handle(result);
                }
            }
        }
    }

    public <T> T execQuery(String query, TResultHandler<T> handler) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(query);
                try (ResultSet result = stmt.getResultSet()) {
                    return handler.handle(result);
                }
            }
        }
    }

    public int execUpdate(String update) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                int res = stmt.executeUpdate(update, Statement.RETURN_GENERATED_KEYS);

                try (ResultSet result = stmt.getGeneratedKeys()) {
                    if (result.next()) { res = result.getInt(1); }
                }
                return res;
            }
        }
    }

    @Override
    public void close() throws SQLException {
        dataSource.close();
    }
}
