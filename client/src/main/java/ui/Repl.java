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

            if (postlogin == null) {
                String result = prelogin.eval(line);
                if (result.equals("quit")) {
                    break;
                }
                System.out.print(result);
                if (prelogin.authToken() != null) {
                    postlogin = new PostloginClient(facade, prelogin.authToken(), prelogin.username());
                    prelogin.reset();
                    System.out.print(postlogin.help());
                }
            } else {
                System.out.print(postlogin.eval(line));
                if (postlogin.isLoggedOut()) {
                    postlogin = null;
                }
            }
        }
        System.out.println("Goodbye!");
    }
}