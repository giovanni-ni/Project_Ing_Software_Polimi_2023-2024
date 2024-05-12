package it.polimi.ingsw.view.TextualInterfaceUnit;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Networking.Listeners.ViewListener;
import it.polimi.ingsw.Networking.socket.Client;
import it.polimi.ingsw.Networking.socket.ClientHandler;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.PlayerStatus;
import it.polimi.ingsw.view.View;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.List;
import java.util.Scanner;

public class Tui implements View{

    private String username;

    public static PlayerStatus status;

    public static Match myMatch;

    private boolean isRMI = false;

    private boolean isSocket = false;

    private final Scanner in;

    public Tui() throws IOException {
        in = new Scanner(System.in);
        this.status = PlayerStatus.MENU;
        myMatch = new Match(0);
    }

    public void init() throws Exception {
        System.out.println(Print.Codex);
        askToContinue();
        askConnectionType();

        if(isRMI) {

        } else {

            Client socket = new Client("localhost", 1234);


        }

        askLogin();
        while(true) redirect();

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
        do{

            System.out.print("Enter the username: ");
            String username = in.nextLine();
            if (username.equals("")) {
                System.out.println(Color.RED + "You didn't choose a username. Try again");

            } else {
                this.username = username;
                return;
            }
        } while(username.equals(""));

    }


    @Override
    public void askMenuAction() throws Exception {
        System.out.println(Print.menuOption);
        System.out.print("-----------------------------------------------------------\n" +
                "Enter the Command you wish to use: ");
        String option = in.nextLine();
        switch (option) {
            case "" -> {
            }
            case "create", "c", "cr" -> askCreateMatch();
            case "join", "j", "jo" -> askJoinMatch();
            case "play", "p", "pl" -> askJoinFirst();
            case "exit", "ex" -> {
                if (askExitGame()) return;
            }
            //case "help", "h", "he" -> askHelp();

            default ->
                    System.out.println(Color.RED + "The [" + option + "] command cannot be found! Please try again.");
        }

    }

    @Override
    public void askJoinFirst() {
        GenericClientMessage msg = new JoinFirstMessage(this.username);

        Client.messageToServer(msg);
    }

    @Override
    public void askSetReady() throws InterruptedException {
        String option;
        do {
            System.out.println("Ready? y/n: ");
            option = in.nextLine();
            if(option.equals("y")){
                SetReadyMessage msg = new SetReadyMessage(this.username);
                Client.messageToServer(msg);
            }
            Thread.sleep(1000);
        } while(!option.equals("y"));


    }

    @Override
    public boolean askCreateMatch() throws Exception {
        CreateGameMessage message = new CreateGameMessage(this.username);
        Client.messageToServer(message);

        //wait(100);
        Thread.sleep(1000);
        //System.out.println(myMatch.idMatch);

        return false;
    }

    /*@Override
    public int askMaxSeats() throws Exception {
        int playerNumber = 0;
        do {

                System.out.print("Please select the number of players for this match [2 to 4]: ");
                playerNumber = Integer.parseInt(in.nextLine());
                if (playerNumber < 2 || playerNumber > 4) {
                    System.out.println(Color.RED + "Invalid number! Please try again.");
                }

        } while (playerNumber < 2 || playerNumber > 4);
        System.out.println("Selected " + playerNumber + " players.");
        return playerNumber;
    }
*/
    @Override
    public boolean askJoinMatch() throws Exception {
        String value;
        do {
            System.out.println();
            System.out.println("---------------------------------------------");
            System.out.print("Please enter the room number: ");
            value = in.nextLine();
        } while (value.equals(""));
        int matchID = Integer.parseInt(value);
        System.out.println("Selected Room [" + matchID + "].");

        GenericClientMessage msg = new JoinGameMessage(this.username, matchID);
        Client.messageToServer(msg);

        return false;
    }

    @Override
    public boolean askLeaveMatch() throws RemoteException {
        return false;
    }

    @Override
    public boolean askExitGame() throws RemoteException {
        System.exit(0);
        return false;
    }

    @Override
    public void showCommonGoals() {

    }

    @Override
    public void showPersonalGoal() throws RemoteException {

    }

    @Override
    public void announceCurrentPlayer() throws RemoteException {

    }

    @Override
    public void showWhoIsPlaying() {

    }

    @Override
    public void showBoard() {

    }

    @Override
    public void askPlayerMove(){

    }
    public void redirect() throws Exception {
        if(status == PlayerStatus.MENU) {
            askMenuAction();
        }

        if(status == PlayerStatus.MatchStart) {

            askSetReady();


            System.out.println("la partita sta per iniziare");
        }
    }

    /*public void waitingMethodReturn() {


                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


    }*/

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }



}
