package client;

import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDatabase() throws ResponseException {
        facade.clear();
    }

    private String registerPlayer() throws ResponseException {
        return facade.register("player1", "password", "p1@email.com").authToken();
    }

    // register

    @Test
    void registerSuccess() throws ResponseException {
        var result = facade.register("player1", "password", "p1@email.com");
        assertEquals("player1", result.username());
        assertTrue(result.authToken().length() > 10);
    }

    @Test
    void registerDuplicateUsername() throws ResponseException {
        registerPlayer();
        var ex = assertThrows(ResponseException.class,
                () -> facade.register("player1", "other", "other@email.com"));
        assertEquals(403, ex.statusCode());
    }

    // login

    @Test
    void loginSuccess() throws ResponseException {
        registerPlayer();
        var result = facade.login("player1", "password");
        assertEquals("player1", result.username());
        assertNotNull(result.authToken());
    }

    @Test
    void loginWrongPassword() throws ResponseException {
        registerPlayer();
        var ex = assertThrows(ResponseException.class, () -> facade.login("player1", "wrong"));
        assertEquals(401, ex.statusCode());
    }

    @Test
    void clearRemovesUsers() throws ResponseException {
        registerPlayer();
        facade.clear();
        assertThrows(ResponseException.class, () -> facade.login("player1", "password"));
    }

    // logout

    @Test
    void logoutSuccess() throws ResponseException {
        String auth = registerPlayer();
        facade.logout(auth);
        // the token should no longer work after logging out
        var ex = assertThrows(ResponseException.class, () -> facade.logout(auth));
        assertEquals(401, ex.statusCode());
    }

    @Test
    void logoutBadToken() {
        var ex = assertThrows(ResponseException.class, () -> facade.logout("not-a-real-token"));
        assertEquals(401, ex.statusCode());
    }

    // clear

    @Test
    void clearOnEmptyDatabase() {
        assertDoesNotThrow(() -> facade.clear());
    }
}
