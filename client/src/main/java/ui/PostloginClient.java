package ui;

import client.ResponseException;
import client.ServerFacade;
import model.GameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PostloginClient {
    private final ServerFacade facade;
    private final String authToken;
    private final String username;
    private boolean loggedOut = false;
    private List<GameData> games = new ArrayList<>();   // list number N is games.get(N - 1)

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
            case "logout" -> logout();
            case "create" -> create(tokens);
            case "list" -> list();
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

    private String create(String[] tokens) {
        if (tokens.length < 2) {
            return "Usage: create <NAME>\n";
        }
        String name = String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length));
        try {
            facade.createGame(authToken, name);
            return "Created game \"" + name + "\". Type list to see it.\n";
        } catch (ResponseException e) {
            return errorMessage(e, "Creating the game");
        }
    }

    private String list() {
        try {
            games = new ArrayList<>(facade.listGames(authToken));
        } catch (ResponseException e) {
            return errorMessage(e, "Listing games");
        }
        if (games.isEmpty()) {
            return "No games yet. Create one with: create <NAME>\n";
        }
        games.sort(Comparator.comparingInt(GameData::gameID));

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < games.size(); i++) {
            GameData game = games.get(i);
            out.append(String.format("  %d. %s  (White: %s, Black: %s)%n",
                    i + 1, game.gameName(), playerOrOpen(game.whiteUsername()),
                    playerOrOpen(game.blackUsername())));
        }
        return out.toString();
    }

    private String playerOrOpen(String player) {
        return (player == null) ? "open" : player;
    }

    private String errorMessage(ResponseException e, String action) {
        return switch (e.statusCode()) {
            case 401 -> "Your session has expired. Please log out and log in again.\n";
            case 0 -> "Couldn't reach the server. Is it running?\n";
            default -> action + " failed. Please try again.\n";
        };
    }
}