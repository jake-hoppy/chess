package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.*;

public class UserHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void register(Context ctx) throws Exception {
        RegisterRequest req = gson.fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult result = userService.register(req);
        ctx.status(200).result(gson.toJson(result));
    }

    public void login(Context ctx) throws Exception {
        LoginRequest req = gson.fromJson(ctx.body(), LoginRequest.class);
        LoginResult result = userService.login(req);
        ctx.status(200).result(gson.toJson(result));
    }

    public void logout(Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        userService.logout(authToken);
        ctx.status(200).result("{}");
    }
}