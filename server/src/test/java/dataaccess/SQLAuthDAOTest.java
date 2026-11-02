package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthDAOTest {
    private SQLAuthDAO dao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        dao = new SQLAuthDAO();
        dao.clear();
    }

    @Test
    public void createAuthSuccess() throws DataAccessException {
        dao.createAuth(new AuthData("t", "a"));
        assertNotNull(dao.getAuth("t"));
    }

    @Test
    public void createAuthFail() throws DataAccessException {
        dao.createAuth(new AuthData("t", "a"));
        assertThrows(DataAccessException.class, () -> dao.createAuth(new AuthData("t", "a")));
    }

    @Test
    public void getAuthSuccess() throws DataAccessException {
        dao.createAuth(new AuthData("t", "a"));
        assertEquals("a", dao.getAuth("t").username());
    }

    @Test
    public void getAuthFail() throws DataAccessException {
        assertNull(dao.getAuth("fake"));
    }

    @Test
    public void deleteAuthSuccess() throws DataAccessException {
        dao.createAuth(new AuthData("t", "a"));
        dao.deleteAuth("t");
        assertNull(dao.getAuth("t"));
    }

    @Test
    public void deleteAuthFail() throws DataAccessException {
        dao.createAuth(new AuthData("t", "a"));
        dao.deleteAuth("fake");
        assertNotNull(dao.getAuth("t"));
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        dao.createAuth(new AuthData("t", "a"));
        dao.clear();
        assertNull(dao.getAuth("t"));
    }
}