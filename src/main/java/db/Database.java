package db;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by vladislav on 18.03.16.
 */
public class Database implements AutoCloseable {

    final BasicDataSource dataSource;

    public Database(String name, String host, int port, String username, String password) throws SQLException {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(String.format("jdbc:mysql://%s:%d/%s", host, port, name));
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        initDatabase();
    }

    private void initDatabase() throws SQLException {
        this.execUpdate("CALL init_database");
    }

    public void execQuery(String query, ResultHandler handler) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(query);
                ResultSet result = stmt.getResultSet();
                handler.handle(result);
                result.close();
            }
        }
    }

    public int execUpdate(String update) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(update, Statement.RETURN_GENERATED_KEYS);
                int res = -1;

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) { res = rs.getInt(1); }
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
