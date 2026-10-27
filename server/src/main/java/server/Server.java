package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;
import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
        UserService userService = new UserService(userDAO, authDAO);

        UserHandler userHandler = new UserHandler(userService);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Routes
        javalin.delete("/db", ctx -> {
            clearService.clear();
            ctx.status(200).result("{}");
        });
        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);

        // Turn exceptions into HTTP status codes
        javalin.exception(BadRequestException.class, (e, ctx) -> sendError(ctx, 400, e.getMessage()));
        javalin.exception(UnauthorizedException.class, (e, ctx) -> sendError(ctx, 401, e.getMessage()));
        javalin.exception(AlreadyTakenException.class, (e, ctx) -> sendError(ctx, 403, e.getMessage()));
        javalin.exception(Exception.class, (e, ctx) -> sendError(ctx, 500, "Error: " + e.getMessage()));
    }

    private static void sendError(Context ctx, int status, String message) {
        ctx.status(status).result(new Gson().toJson(Map.of("message", message)));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}