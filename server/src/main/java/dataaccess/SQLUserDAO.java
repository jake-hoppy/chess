package dataaccess;

import model.AuthData;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() throws DataAccessException{
        DatabaseManager.createDatabase();
        String sql = """
                CREATE TABLE IF NOT EXISTS auth(
                authToken VARCHAR(255) NOT NULL,
                username VARCHAR(255) NOT NULL,
                PRIMARY KEY (authToken)
                )""";
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to make auth table", e);
        }
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException{
        String sql = "INSERT INTO auth (authToken, username) VALUES (?,?)";
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,auth.authToken());
            stmt.setString(2,auth.username());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to update values", e);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException{
        String sql = "SELECT authToken, username FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,authToken);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(rs.getString("authToken"), rs.getString("username"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: authToken doesnt exist", e);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        String sql = "DELETE FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,authToken);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: authToken doesnt exist", e);
        }
    }
}