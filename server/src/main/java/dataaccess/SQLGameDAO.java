package dataaccess;

import java.sql.SQLException;

public class SQLGameDAO implements GameDAO {

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
    }
}
