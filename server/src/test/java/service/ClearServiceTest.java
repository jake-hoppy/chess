package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {

    @Test
    public void clearRemovesEverything() throws Exception {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        userDAO.createUser(new UserData("hoppy", "pw", "h@byu.edu"));
        authDAO.createAuth(new AuthData("token", "hoppy"));
        gameDAO.createGame("my game");

        new ClearService(userDAO, authDAO, gameDAO).clear();

        assertNull(userDAO.getUser("hoppy"));
        assertNull(authDAO.getAuth("token"));
        assertTrue(gameDAO.listGames().isEmpty());
    }
}