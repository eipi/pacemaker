package name.eipi.pacemaker.controllers;

import asg.cliche.ShellFactory;
import name.eipi.pacemaker.views.UI;

import java.io.IOException;

public class Main {

    // Performs logic as instructed by UI.
    private static final PacemakerAPI api = new PacemakerAPI();

    // UI (Cliche), give it an API to use.
    private static final UI ui = new UI(api);

    public static void main(String[] args) throws IOException {
        ShellFactory.createConsoleShell("home", "?help", ui).commandLoop();
    }

}

