package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MemoryDAOTest {

    @Test
    public void memoryUserDAOWorks() throws DataAccessException {
        MemoryUserDAO dao = new MemoryUserDAO();
        dao.createUser(new UserData("a", "pw", "e"));
        assertNotNull(dao.getUser("a"));
        dao.clear();
        assertNull(dao.getUser("a"));
    }

    @Test
    public void memoryAuthDAOWorks() throws DataAccessException {
        MemoryAuthDAO dao = new MemoryAuthDAO();
        dao.createAuth(new AuthData("t", "a"));
        assertNotNull(dao.getAuth("t"));
        dao.deleteAuth("t");
        assertNull(dao.getAuth("t"));
        dao.createAuth(new AuthData("t", "a"));
        dao.clear();
        assertNull(dao.getAuth("t"));
    }

    @Test
    public void memoryGameDAOWorks() throws DataAccessException {
        MemoryGameDAO dao = new MemoryGameDAO();
        int id = dao.createGame("g");
        assertNotNull(dao.getGame(id));
        assertEquals(1, dao.listGames().size());
        dao.updateGame(new GameData(id, "a", null, "g", new ChessGame()));
        assertEquals("a", dao.getGame(id).whiteUsername());
        assertThrows(DataAccessException.class,
                () -> dao.updateGame(new GameData(999, "a", null, "g", new ChessGame())));
        dao.clear();
        assertTrue(dao.listGames().isEmpty());
    }
}