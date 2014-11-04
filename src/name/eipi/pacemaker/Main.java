package name.eipi.pacemaker;

import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import name.eipi.pacemaker.controllers.PacemakerApi;
import name.eipi.pacemaker.controllers.PacemakerImpl;
import name.eipi.pacemaker.views.PacemakerUI;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    // Performs logic as instructed by UI.
    private static PacemakerApi api = null;

    // The Cliche Shell.
    private static Shell shell = null;
    // PacemakerUI (Cliche), give it an API to use.
    private static PacemakerUI ui = null;

    private static boolean keepItAlive = false;

    public static void main(String[] args) {
        String ctxt = args.length > 0 ? args[0] : "pmdb";
        do {
            execute(ctxt);
        } while (keepItAlive);


    }

    public static void execute(String context) {
        api = new PacemakerImpl(context);
        ui = new PacemakerUI(api);

        shell = ShellFactory.createConsoleShell("pm", "?help", ui);
        try {
            shell.commandLoop();
            // After successful execution
            ui.store();
        } catch (Throwable t) {
            t.printStackTrace();
            System.err.println("Pacemaker encountered an error, continue? (y/n)");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String response = reader.readLine();
                if (response != null && !response.isEmpty()) {
                    String charAt = String.valueOf(response.charAt(0));
                    if ("Y".equalsIgnoreCase(charAt)) {
                        keepItAlive = true;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
    }

}

