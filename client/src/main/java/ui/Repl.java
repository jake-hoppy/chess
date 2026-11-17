package ui;

import client.ServerFacade;
import java.util.Scanner;

public class Repl {
    private final ServerFacade facade;
    private final PreloginClient prelogin;
    private PostloginClient postlogin;   // null while logged out

    public Repl(ServerFacade facade) {
        this.facade = facade;
        this.prelogin = new PreloginClient(facade);
    }

    public void run() {
        System.out.println("♕ Welcome to 240 Chess. Type help to get started.");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String prompt = (postlogin == null) ? "[LOGGED_OUT]" : "[" + postlogin.username() + "]";
            System.out.print("\n" + prompt + " >>> ");
            if (!scanner.hasNextLine()) {
                break;   // Ctrl-D: exit cleanly instead of crashing
            }
            String line = scanner.nextLine();

        }
        System.out.println("Goodbye!");
    }
}