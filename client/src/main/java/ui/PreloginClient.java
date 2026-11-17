package ui;

import client.ServerFacade;

public class PreloginClient {
    private final ServerFacade facade;

    public PreloginClient(ServerFacade facade) {
        this.facade = facade;
    }

    /** Runs one command and returns the text to show the user. */
    public String eval(String input) {
        String[] tokens = input.trim().split("\\s+");
        String cmd = tokens[0].toLowerCase();
        return switch (cmd) {
            case "quit" -> "quit";
            default -> help();
        };
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