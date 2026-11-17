package ui;

import client.ServerFacade;

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
}