package dataaccess.sql;

import dataaccess.*;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTest {
    private SQLGameDAO dao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        dao = new SQLGameDAO();
        dao.clear();
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        int id = dao.createGame("g");
        assertNotNull(dao.getGame(id));
    }

    @Test
    public void createGameFail() {
        assertThrows(DataAccessException.class, () -> dao.createGame(null));
    }

    @Test
    public void getGameSuccess() throws DataAccessException {
        int id = dao.createGame("g");
        assertEquals("g", dao.getGame(id).gameName());
    }

    @Test
    public void getGameFail() throws DataAccessException {
        assertNull(dao.getGame(9999));
    }

    @Test
    public void listGamesSuccess() throws DataAccessException {
        dao.createGame("g");
        assertEquals(1, dao.listGames().size());
    }

    @Test
    public void listGamesFail() throws DataAccessException {
        assertTrue(dao.listGames().isEmpty());
    }

    @Test
    public void updateGameSuccess() throws DataAccessException {
        int id = dao.createGame("g");
        dao.updateGame(new GameData(id, "a", null, "g", new ChessGame()));
        assertEquals("a", dao.getGame(id).whiteUsername());
    }

    @Test
    public void updateGameFail() {
        GameData fake = new GameData(9999, "a", null, "g", new ChessGame());
        assertThrows(DataAccessException.class, () -> dao.updateGame(fake));
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        dao.createGame("g");
        dao.clear();
        assertTrue(dao.listGames().isEmpty());
    }
}