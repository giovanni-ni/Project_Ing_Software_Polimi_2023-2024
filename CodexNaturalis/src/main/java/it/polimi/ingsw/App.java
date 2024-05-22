package it.polimi.ingsw;

import it.polimi.ingsw.view.Gui.GUIApplication;
import it.polimi.ingsw.view.TextualInterfaceUnit.Tui;
import it.polimi.ingsw.view.Ui;
import javafx.application.Application;

import java.awt.*;
import java.util.*;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {
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
            GUIApplication guiApplication = new GUIApplication();
            guiApplication.main(null);

        }

    }

}
