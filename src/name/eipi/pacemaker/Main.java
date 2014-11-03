package name.eipi.pacemaker;

import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import name.eipi.pacemaker.controllers.PacemakerApi;
import name.eipi.pacemaker.controllers.PacemakerImpl;
import name.eipi.pacemaker.views.PacemakerUI;

import java.io.IOException;

public class Main {

    // Performs logic as instructed by UI.
    private static final PacemakerApi api = new PacemakerImpl("pmdb");

    // PacemakerUI (Cliche), give it an API to use.
    private static final PacemakerUI ui = new PacemakerUI(api);

    public static void main(String[] args) throws IOException {

        Shell shell = ShellFactory.createConsoleShell("pm", "?help", ui);
        shell.commandLoop();
    }

}

