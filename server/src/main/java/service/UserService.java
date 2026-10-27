package service;

import dataaccess.*;
import model.*;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest req)
            throws BadRequestException, AlreadyTakenException, DataAccessException {
        if (req == null || req.username() == null || req.password() == null || req.email() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (userDAO.getUser(req.username()) != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        userDAO.createUser(new UserData(req.username(), req.password(), req.email()));

        String token = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(token, req.username()));

        return new RegisterResult(req.username(), token);
    }
    public LoginResult login(LoginRequest req)
            throws BadRequestException, UnauthorizedException, DataAccessException {
        if (req == null || req.username() == null || req.password() == null) {
            throw new BadRequestException("Error: bad request");
        }

        UserData user = userDAO.getUser(req.username());
        if (user == null || !user.password().equals(req.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String token = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(token, user.username()));

        return new LoginResult(user.username(), token);
    }

    public void logout(String authToken) throws UnauthorizedException, DataAccessException {
        if (authToken == null || authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }
}