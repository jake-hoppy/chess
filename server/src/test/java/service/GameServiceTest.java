package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private GameDAO gameDAO;
    private GameService gameService;
    private String token;

    @BeforeEach
    public void setUp() throws Exception {
        gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        gameService = new GameService(gameDAO, authDAO);

        token = "test-token";
        authDAO.createAuth(new AuthData(token, "hoppy"));
    }

    @Test
    public void createGameSuccess() throws Exception {
        CreateGameResult result = gameService.createGame(token, new CreateGameRequest("my game"));

        GameData game = gameDAO.getGame(result.gameID());
        assertNotNull(game);
        assertEquals("my game", game.gameName());
    }

    @Test
    public void createGameUnauthorized() {
        assertThrows(UnauthorizedException.class,
                () -> gameService.createGame("bad-token", new CreateGameRequest("my game")));
    }

    @Test
    public void createGameMissingName() {
        assertThrows(BadRequestException.class,
                () -> gameService.createGame(token, new CreateGameRequest(null)));
    }

    @Test
    public void listGamesSuccess() throws Exception {
        gameService.createGame(token, new CreateGameRequest("game 1"));
        gameService.createGame(token, new CreateGameRequest("game 2"));

        ListGamesResult result = gameService.listGames(token);

        assertEquals(2, result.games().size());
    }

    @Test
    public void listGamesUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> gameService.listGames(null));
    }

    @Test
    public void joinGameWhiteSuccess() throws Exception {
        int id = gameService.createGame(token, new CreateGameRequest("my game")).gameID();

        gameService.joinGame(token, new JoinGameRequest("WHITE", id));

        GameData game = gameDAO.getGame(id);
        assertEquals("hoppy", game.whiteUsername());
        assertNull(game.blackUsername());
    }

    @Test
    public void joinGameBlackSuccess() throws Exception {
        int id = gameService.createGame(token, new CreateGameRequest("my game")).gameID();

        gameService.joinGame(token, new JoinGameRequest("BLACK", id));

        assertEquals("hoppy", gameDAO.getGame(id).blackUsername());
    }

    @Test
    public void joinGameColorTaken() throws Exception {
        int id = gameService.createGame(token, new CreateGameRequest("my game")).gameID();
        gameService.joinGame(token, new JoinGameRequest("WHITE", id));

        assertThrows(AlreadyTakenException.class,
                () -> gameService.joinGame(token, new JoinGameRequest("WHITE", id)));
    }

    @Test
    public void joinGameBadColor() throws Exception {
        int id = gameService.createGame(token, new CreateGameRequest("my game")).gameID();

        assertThrows(BadRequestException.class,
                () -> gameService.joinGame(token, new JoinGameRequest("PURPLE", id)));
    }

    @Test
    public void joinGameBadId() {
        assertThrows(BadRequestException.class,
                () -> gameService.joinGame(token, new JoinGameRequest("WHITE", 999)));
    }

    @Test
    public void joinGameUnauthorized() throws Exception {
        int id = gameService.createGame(token, new CreateGameRequest("my game")).gameID();

        assertThrows(UnauthorizedException.class,
                () -> gameService.joinGame("bad-token", new JoinGameRequest("WHITE", id)));
    }
}