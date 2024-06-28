package it.polimi.ingsw.Networking;

import it.polimi.ingsw.view.Gui.GUIApplication;
import it.polimi.ingsw.view.TextualInterfaceUnit.Tui;
import it.polimi.ingsw.view.Ui;

import java.util.Scanner;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.*;
import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;
/**
 * Main class for starting the client application.
 */
public class MainClient
{
    /**
     * Main method to start the client application.
     *
     * @param args command line arguments (not used)
     * @throws Exception if there is an error during execution
     */
        public static void main(String[] args) throws Exception {

            Ui ui;
            int option = 0;
            Scanner in = new Scanner(System.in);
            do {
                print("""
                    Please choose UI
                        1  --> TUI
                        2  --> GUI
                    Enter your choice.
                    >\040""");
                try {
                    option = Integer.parseInt(in.nextLine());
                } catch (NumberFormatException e) {
                    print(ANSI_MUSHROOM + "Invalid input. Try again. (1 or 2)"+ANSI_RESET);
                }
            } while (option != 1 && option != 2);

            if(option == 1) {
                Tui tui  = new Tui();
                tui.init();
            } else {
                print("Starting Gui.....");
                GUIApplication.main(null);

            }

        }

}
