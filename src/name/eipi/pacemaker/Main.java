package name.eipi.pacemaker;

import asg.cliche.ShellFactory;
import name.eipi.pacemaker.controllers.PacemakerAPI;
import name.eipi.pacemaker.views.PacemakerUI;

import java.io.IOException;

public class Main {

    // Performs logic as instructed by UI.
    private static final PacemakerAPI api = new PacemakerAPI("pmdb");

    // PacemakerUI (Cliche), give it an API to use.
    private static final PacemakerUI ui = new PacemakerUI(api);

    public static void main(String[] args) throws IOException {
        ShellFactory.createConsoleShell("pm", "?help", ui).commandLoop();
    }

}

