package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTest {
    private SQLUserDAO dao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        dao = new SQLUserDAO();
        dao.clear();
    }

    @Test
    public void createUserSuccess() throws DataAccessException {
        dao.createUser(new UserData("a", "pw", "e"));
        assertNotNull(dao.getUser("a"));
    }

    @Test
    public void createUserFail() throws DataAccessException {
        dao.createUser(new UserData("a", "pw", "e"));
        assertThrows(DataAccessException.class, () -> dao.createUser(new UserData("a", "pw", "e")));
    }

    @Test
    public void getUserSuccess() throws DataAccessException {
        dao.createUser(new UserData("a", "pw", "e"));
        assertEquals("e", dao.getUser("a").email());
    }

    @Test
    public void getUserFail() throws DataAccessException {
        assertNull(dao.getUser("nobody"));
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        dao.createUser(new UserData("a", "pw", "e"));
        dao.clear();
        assertNull(dao.getUser("a"));
    }
}