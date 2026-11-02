package dataaccess;

import model.UserData;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
    public SQLUserDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        String sql = """
                CREATE TABLE IF NOT EXISTS user(
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (username)
                )""";
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to make user table", e);
        }
    }
}