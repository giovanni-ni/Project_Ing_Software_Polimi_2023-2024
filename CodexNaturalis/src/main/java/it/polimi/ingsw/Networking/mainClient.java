package it.polimi.ingsw.Networking;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.view.Gui.GUIApplication;
import it.polimi.ingsw.view.TextualInterfaceUnit.Tui;
import it.polimi.ingsw.view.Ui;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.*;
import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;
// todo javadoc
public class mainClient
{

        /**
         *
         * @param args
         * @throws IOException
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
