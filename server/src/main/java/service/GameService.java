package service;

import dataaccess.*;
import model.*;
import java.util.ArrayList;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    private AuthData authenticate(String authToken) throws UnauthorizedException, DataAccessException {
        AuthData auth = (authToken == null) ? null : authDAO.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return auth;
    }

    public ListGamesResult listGames(String authToken) throws UnauthorizedException, DataAccessException {
        authenticate(authToken);
        return new ListGamesResult(new ArrayList<>(gameDAO.listGames()));
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest req)
            throws UnauthorizedException, BadRequestException, DataAccessException {
        authenticate(authToken);
        if (req == null || req.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        int id = gameDAO.createGame(req.gameName());
        return new CreateGameResult(id);
    }

    public void joinGame(String authToken, JoinGameRequest req)
            throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        AuthData auth = authenticate(authToken);

        if (req == null || req.gameID() == null || req.playerColor() == null) {
            throw new BadRequestException("Error: bad request");
        }
        GameData game = gameDAO.getGame(req.gameID());
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }

        GameData updated;
        if (req.playerColor().equals("WHITE")) {
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            updated = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
        } else if (req.playerColor().equals("BLACK")) {
            if (game.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            updated = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
        } else {
            throw new BadRequestException("Error: bad request");
        }

        gameDAO.updateGame(updated);
    }
}