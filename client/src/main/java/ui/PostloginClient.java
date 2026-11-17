package ui;

import client.ServerFacade;
import client.ResponseException;

public class PostloginClient {
    private final ServerFacade facade;
    private final String authToken;
    private final String username;
    private boolean loggedOut = false;

    public PostloginClient(ServerFacade facade, String authToken, String username) {
        this.facade = facade;
        this.authToken = authToken;
        this.username = username;
    }

    public String username() { return username; }
    public boolean isLoggedOut() { return loggedOut; }

    public String eval(String input) {
        String[] tokens = input.trim().split("\\s+");
        String cmd = tokens[0].toLowerCase();
        return switch (cmd) {
            default -> help();
        };
    }

    public String help() {
        return """
                  create <NAME> - create a new game
                  list - list all games
                  play <NUMBER> <WHITE|BLACK> - join a game as a player
                  observe <NUMBER> - watch a game
                  logout - log out
                  help - show possible commands
                """;
    }
    private String logout() {
        try {
            facade.logout(authToken);
        } catch (ResponseException e) {
            if (e.statusCode() == 0) {
                return "Couldn't reach the server. Is it running?\n";
            }
        }
        loggedOut = true;
        return "Logged out.\n";
    }
}