package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    private final Gson gson = new Gson();

    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        String sql = """
        CREATE TABLE IF NOT EXISTS game (
        gameID INT NOT NULL AUTO_INCREMENT,
        whiteUsername VARCHAR(255),
        blackUsername VARCHAR(255),
        gameName VARCHAR(255) NOT NULL,
        game LONGTEXT NOT NULL,
        PRIMARY KEY (gameID)
        )""";
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to make game table", e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "TRUNCATE TABLE game";
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to clear game table", e);
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        String sql = "INSERT INTO game (gameName, game) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, gameName);
            stmt.setString(2, gson.toJson(new ChessGame()));
            stmt.executeUpdate();
            try (var keys = stmt.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to create game", e);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT * FROM game WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            try (var rs = stmt.executeQuery()) {
                return rs.next() ? readGame(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to read game", e);
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var games = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement("SELECT * FROM game");
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                games.add(readGame(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to list games", e);
        }
        return games;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        return new GameData(
                rs.getInt("gameID"),
                rs.getString("whiteUsername"),
                rs.getString("blackUsername"),
                rs.getString("gameName"),
                gson.fromJson(rs.getString("game"), ChessGame.class));
    }
}
