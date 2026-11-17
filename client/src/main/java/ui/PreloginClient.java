package ui;

import client.ServerFacade;
import client.ResponseException;

public class PreloginClient {
    private final ServerFacade facade;
    private String authToken;
    private String username;

    public String authToken() { return authToken; }
    public String username() { return username; }

    public PreloginClient(ServerFacade facade) {
        this.facade = facade;

    }

    public String eval(String input) {
        String[] tokens = input.trim().split("\\s+");
        String cmd = tokens[0].toLowerCase();
        return switch (cmd) {
            case "quit" -> "quit";
            case "register" -> register(tokens);
            case "login" -> login(tokens);
            default -> help();
        };
    }
    private String register(String[] tokens) {
        if (tokens.length != 4) {
            return "Usage: register <USERNAME> <PASSWORD> <EMAIL>\n";
        }
        try {
            var result = facade.register(tokens[1], tokens[2], tokens[3]);
            authToken = result.authToken();
            username = result.username();
            return "Registered and logged in as " + username + ".\n";
        } catch (ResponseException e) {
            return switch (e.statusCode()) {
                case 403 -> "That username is already taken.\n";
                case 0 -> "Couldn't reach the server. Is it running?\n";
                default -> "Registration failed. Please try again.\n";
            };
        }
    }

    private String login(String[] tokens) {
        if (tokens.length != 3) {
            return "Usage: login <USERNAME> <PASSWORD>\n";
        }
        try {
            var result = facade.login(tokens[1], tokens[2]);
            authToken = result.authToken();
            username = result.username();
            return "Logged in as " + username + ".\n";
        } catch (ResponseException e) {
            return switch (e.statusCode()) {
                case 401 -> "Wrong username or password.\n";
                case 0 -> "Couldn't reach the server. Is it running?\n";
                default -> "Login failed. Please try again.\n";
            };
        }
    }

    public String help() {
        return """
                  register <USERNAME> <PASSWORD> <EMAIL> - create an account
                  login <USERNAME> <PASSWORD> - log in to play
                  quit - exit the program
                  help - show possible commands
                """;
    }
}