package ui;

import client.ServerFacade;
import java.util.Scanner;

public class Repl {
    private final PreloginClient prelogin;

    public Repl(ServerFacade facade) {
        this.prelogin = new PreloginClient(facade);
    }

    public void run() {
        System.out.println("♕ Welcome to 240 Chess. Type help to get started.");
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            System.out.print("\n[LOGGED_OUT] >>> ");
            String line = scanner.nextLine();
            result = prelogin.eval(line);
            if (!result.equals("quit")) {
                System.out.print(result);
            }
        }
        System.out.println("Goodbye!");
    }
}