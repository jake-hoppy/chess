package dataaccess;

import model.AuthData;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() throws DataAccessException{
        DatabaseManager.createDatabase();
        String sql = """
                CREATE TABLE IF NOT EXISTS auth(
                authToken VARCHAR(255) NOT NULL,
                userToken VARCHAR(255) NOT NULL,
                PRIMARY KEY (authToken)
                )""";
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: DataAccessException", e);
        }
    }
}