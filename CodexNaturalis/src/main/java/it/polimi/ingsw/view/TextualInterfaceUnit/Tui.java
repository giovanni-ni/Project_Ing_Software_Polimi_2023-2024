package it.polimi.ingsw.view.TextualInterfaceUnit;

import it.polimi.ingsw.Networking.Listeners.ViewListener;
import it.polimi.ingsw.Networking.socket.Client;
import it.polimi.ingsw.view.View;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Tui implements View{

    private String username;

    private boolean isRMI = false;

    private boolean isSocket = false;

    private final Scanner in;
    private final PrintStream out;

    public Tui() {
        in = new Scanner(System.in);
        out = new PrintStream(System.out, true);
    }

    public void init() throws IOException {
        System.out.println(Print.Codex);
        askToContinue();
        askConnectionType();

        if(isRMI) {

        } else {
            //Client c = new Client("localhost",1234);
            try (Socket socket = new Socket("localhost", 1234);) //open a socket
            {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            }
        }


    }

    public void askConnectionType() {
        int option = 0;
        do {
            System.out.print("""
                    What would you like to use to contact the server? RMI or Socket?
                        1  --> RMI
                        2  --> Socket
                    Enter your choice.
                    >\040""");
            try {
                option = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(Color.RED + "Invalid input. Try again. (1 or 2)");
            }
        } while (option != 1 && option != 2);

        if(option == 1) {
            isRMI = true;
        } else {
            isSocket = true;
        }
    }



    public void askToContinue() {
        System.out.print("Press 'Enter' to continue");
        in.nextLine();
        System.out.println("-----------------------------------------------------------");
    }

    @Override
    public void askLogin() throws Exception {

    }

    @Override
    public void askMenuAction() throws Exception {

    }

    @Override
    public boolean askCreateMatch() throws Exception {
        return false;
    }

    @Override
    public int askMaxSeats() throws Exception {
        return 0;
    }

    @Override
    public boolean askJoinMatch() throws Exception {
        return false;
    }


}
