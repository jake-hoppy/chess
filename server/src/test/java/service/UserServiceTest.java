package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    public void registerSuccess() throws Exception {
        RegisterResult result = userService.register(new RegisterRequest("hoppy", "pw", "h@byu.edu"));

        assertEquals("hoppy", result.username());
        assertNotNull(result.authToken());
        assertNotNull(userDAO.getUser("hoppy"));
        assertNotNull(authDAO.getAuth(result.authToken()));
    }

    @Test
    public void registerAlreadyTaken() throws Exception {
        userService.register(new RegisterRequest("hoppy", "pw", "h@byu.edu"));

        assertThrows(AlreadyTakenException.class,
                () -> userService.register(new RegisterRequest("hoppy", "other", "x@byu.edu")));
    }

    @Test
    public void registerMissingField() {
        assertThrows(BadRequestException.class,
                () -> userService.register(new RegisterRequest("hoppy", null, "h@byu.edu")));
    }

    @Test
    public void loginSuccess() throws Exception {
        userDAO.createUser(new UserData("hoppy", "pw", "h@byu.edu"));

        LoginResult result = userService.login(new LoginRequest("hoppy", "pw"));

        assertEquals("hoppy", result.username());
        assertNotNull(authDAO.getAuth(result.authToken()));
    }

    @Test
    public void loginWrongPassword() throws Exception {
        userDAO.createUser(new UserData("hoppy", "pw", "h@byu.edu"));

        assertThrows(UnauthorizedException.class,
                () -> userService.login(new LoginRequest("hoppy", "wrong")));
    }

    @Test
    public void loginMissingField() {
        assertThrows(BadRequestException.class,
                () -> userService.login(new LoginRequest(null, "pw")));
    }

    @Test
    public void logoutSuccess() throws Exception {
        RegisterResult reg = userService.register(new RegisterRequest("hoppy", "pw", "h@byu.edu"));

        userService.logout(reg.authToken());

        assertNull(authDAO.getAuth(reg.authToken()));
    }

    @Test
    public void logoutBadToken() {
        assertThrows(UnauthorizedException.class, () -> userService.logout("not-a-real-token"));
    }
}