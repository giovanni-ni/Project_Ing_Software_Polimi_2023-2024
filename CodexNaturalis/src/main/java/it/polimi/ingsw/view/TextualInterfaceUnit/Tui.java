package it.polimi.ingsw.view.TextualInterfaceUnit;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Networking.Listeners.ViewListener;
import it.polimi.ingsw.Networking.socket.Client;
import it.polimi.ingsw.Networking.socket.ClientHandler;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.View;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;

public class Tui implements View{

    private String username;

    public static PlayerStatus status;

    public static Match myMatch;

    public static Player myPlayer;

    public static int matchID;

    private boolean isRMI = false;

    private boolean isSocket = false;

    private final Scanner in;

    private int first;

    public Tui() throws IOException {
        in = new Scanner(System.in);
        this.status = PlayerStatus.MENU;
        myMatch = new Match(0);
        this.first = 0;
    }

    public void init() throws Exception {
        print(Print.Codex);
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
            print("""
                    What would you like to use to contact the server? RMI or Socket?
                        1  --> RMI
                        2  --> Socket
                    Enter your choice.
                    >\040""");
            try {
                option = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                print(Color.RED + "Invalid input. Try again. (1 or 2)");
            }
        } while (option != 1 && option != 2);

        if(option == 1) {
            isRMI = true;
        } else {
            isSocket = true;
        }
    }



    public void askToContinue() {
        print("Press 'Enter' to continue");
        in.nextLine();
        print("-----------------------------------------------------------");
    }

    @Override
    public void askLogin() throws Exception {
        do{

            print("Enter the username: ");
            String username = in.nextLine();
            if (username.equals("")) {
                print(Color.RED + "You didn't choose a username. Try again");

            } else {
                this.username = username;
                return;
            }
        } while(username.equals(""));

    }


    @Override
    public void askMenuAction() throws Exception {
        print(Print.menuOption);
        print("-----------------------------------------------------------\n" +
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
                    print(Color.RED + "The [" + option + "] command cannot be found! Please try again.");
        }

    }

    @Override
    public void askJoinFirst() throws InterruptedException {
        GenericClientMessage msg = new JoinFirstMessage(this.username);
        Client.messageToServer(msg);
        Thread.sleep(1000);
    }

    @Override
    public void askSetReady() throws InterruptedException {
        String option;

            print("Ready? y/n: ");
            option = in.nextLine();
            if(option.equals("y")){
                SetReadyMessage msg = new SetReadyMessage(this.username);
                Client.messageToServer(msg);
            }
            while(status!=PlayerStatus.GamePlay){
                print("please be ready");
                Thread.sleep(1000);
            }

    }

    @Override
    public void askDrawCard() throws InterruptedException {
        print("insert number of the deck: ");
        int option = Integer.parseInt(in.nextLine());
        boolean deck;
        if(option == 1) {
            deck = true;
        } else {
            deck = false;
        }
        print("insert number of the card(1/2/3): ");
        int card = Integer.parseInt(in.nextLine());
        drawCardMessage msg = new drawCardMessage(this.username, matchID, deck, card );
        Client.messageToServer(msg);
        Thread.sleep(1000);
    }

    @Override
    public void askPlayCard() throws InterruptedException {
        print("choose the card that you want to play: ");
        int index = Integer.parseInt(in.nextLine());
        print("front or back: f/b");
        String front = in.nextLine();
        boolean f = false;
        if(front.equals("f")) {
            f = true;
        }
        print("position x: ");
        int x = Integer.parseInt(in.nextLine());
        print("position y: ");
        int y = Integer.parseInt(in.nextLine());

        playCardMessage msg = new playCardMessage(index, f, x, y);
        Client.messageToServer(msg);
        Thread.sleep(1000);
    }

    @Override
    public boolean askCreateMatch() throws Exception {
        CreateGameMessage message = new CreateGameMessage(this.username);
        Client.messageToServer(message);

        //wait(100);
        Thread.sleep(1000);
        //print(myMatch.idMatch);

        return false;
    }

    /*@Override
    public int askMaxSeats() throws Exception {
        int playerNumber = 0;
        do {

                print("Please select the number of players for this match [2 to 4]: ");
                playerNumber = Integer.parseInt(in.nextLine());
                if (playerNumber < 2 || playerNumber > 4) {
                    print(Color.RED + "Invalid number! Please try again.");
                }

        } while (playerNumber < 2 || playerNumber > 4);
        print("Selected " + playerNumber + " players.");
        return playerNumber;
    }
*/
    @Override
    public boolean askJoinMatch() throws Exception {
        String value;
        do {
            print("---------------------------------------------");
            print("Please enter the room number: ");
            value = in.nextLine();
        } while (value.equals(""));
        int matchID = Integer.parseInt(value);
        print("Selected Room [" + matchID + "].");

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
        print("[][][][][]\n" +
                           "[][][][][]\n" +
                           "[][][1][][]\n" +
                           "[][][][][]\n" +
                           "[][][][][]");
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
            //print("la partita sta per iniziare");
        }

        if(status == PlayerStatus.GamePlay) {
            inGame();
        }
    }

    /*public void waitingMethodReturn() {


                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


    }*/

    public void inGame() throws InterruptedException {
            if(first == 0) {
                List<Card> deck = new ArrayList<>();
                TargetCard[] target = {};
                print("your card: ");

                for(Player p : myMatch.getPlayers()) {
                    print("ci sono"+myMatch.getPlayers().size());
                    print(p.nickname);
                    print("hai in mano"+p.getCardOnHand().size());

                    if(p.getNickname().equals(this.username) ) {

                        deck.addAll(p.getCardOnHand());
                        print("hhhhhhh");
                        target = p.getTargetOnHand();
                    }
                }
                for(Card c: deck) {
                    print(c.getCode() + " ");
                }
                print("choose your personal target card from: ");
                print(target[0] + " " + target[1]);
                int choice = Integer.parseInt(in.nextLine());
                SetTargetCardMessage msg = new SetTargetCardMessage(myMatch.idMatch, this.username, choice);
                Client.messageToServer(msg);

                Thread.sleep(1000);

                showBoard();
            }
    }


    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }




}
