package it.polimi.ingsw.view.TextualInterfaceUnit;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Networking.Client;
import it.polimi.ingsw.Networking.remoteInterface.VirtualServer;
import it.polimi.ingsw.Networking.rmi.RMIClient;
import it.polimi.ingsw.Networking.socket.SocketClient;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.View;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;

public class Tui implements View{

    private String username;

    public static PlayerStatus status;

    public static ViewModel myMatch;

    private int[][] myBoard;

    public static Player myPlayer;

    public static int matchID;

    private boolean isRMI = false;

    private boolean isSocket = false;

    private final Scanner in;

    private int first;

    private Client client;

    public Tui() throws IOException {
        in = new Scanner(System.in);
        this.status = PlayerStatus.MENU;
        myMatch = new ViewModel ( new Match(0));
        this.first = 0;
        myPlayer = new Player("");
        myPlayer.setReady(false);
    }

    public void init() throws Exception {
        System.out.println(Print.Codex);
        askToContinue();
        askConnectionType();

        if(isRMI) {
            final String serverName = "AdderServer";
            Registry registry = LocateRegistry.getRegistry("localhost", 2234);
            VirtualServer server = (VirtualServer) registry.lookup(serverName);
            client = new RMIClient(server);
        } else {
            client = new SocketClient("localhost", 1234);
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
    public void askJoinFirst() throws InterruptedException, RemoteException {
        GenericClientMessage msg = new JoinFirstMessage(this.username);
        client.messageToServer(msg);
        Thread.sleep(1000);
    }

    @Override
    public void askSetReady() throws InterruptedException, RemoteException {
        String option;
        if (!myPlayer.getReady()) {
            do {

                print("Ready? y/n: ");
                option = in.nextLine();
                if(option.equals("y")){
                    SetReadyMessage msg = new SetReadyMessage(this.username);
                    myPlayer.setReady(true);
                    client.messageToServer(msg);
                }
                Thread.sleep(1000);
            } while(!option.equals("y"));

        }
        print("Waiting other Players");

    }

    @Override
    public void askDrawCard() throws InterruptedException, RemoteException {
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
        client.messageToServer(msg);
        Thread.sleep(1000);
    }

    @Override
    public void askPlayCard() throws InterruptedException, RemoteException {
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
        client.messageToServer(msg);
        Thread.sleep(1000);
    }

    @Override
    public boolean askCreateMatch() throws Exception {
        CreateGameMessage message = new CreateGameMessage(this.username);
        client.messageToServer(message);

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
        client.messageToServer(msg);

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
        int p = 1;
        System.out.println("1 2 3 4 5 6 7 8 9 10 11");
        System.out.println("");
        System.out.println("");
        for (int i = 0; i < myBoard.length; i++) {
            for (int j = 0; j < myBoard[0].length; j++) {
                System.out.print(myBoard[i][j]+" ");
            }
            System.out.println("        "+p);
            p++;
        }
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
            //print("game status change recognized, method calling...");
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

    public void inGame() throws InterruptedException, RemoteException {
        print("in game method called");
            if(first == 0) {
                List<Card> deck = new ArrayList<>();
                TargetCard[] target = {};
                print("your card: ");

                //for(Player p : myMatch.getPlayers()) {
                    //print("ci sono giocatori n"+myMatch.getPlayers().size());
                    //print(p.nickname);
                    //print("hai in mano "+p.getCardOnHand().size());

                //    if(p.getNickname().equals(this.username) ) {

                //        deck.addAll(p.getCardOnHand());
                        //print("hhhhhhh");
                //        target = p.getTargetOnHand();
                //    }
                //}
                for(Card c: myPlayer.getCardOnHand()) {
                    print(c.getCode() + " ");
                }
                print("choose your personal target card from: ");
                print(myPlayer.getTargetOnHand()[0].getIdCard() + " " + myPlayer.getTargetOnHand()[1].getIdCard());
                int choice = Integer.parseInt(in.nextLine());
                SetTargetCardMessage msg = new SetTargetCardMessage(myMatch.idMatch, this.username, choice);
                client.messageToServer(msg);

                Thread.sleep(1000);

                print("this is your initial card:"+ myPlayer.getInitialCard()+" front(0) or back(1) ");
                choice = Integer.parseInt(in.nextLine());
                boolean c;
                if(choice == 0) {
                    c = true;
                } else {
                    c = false;
                }

                FrontOrBackMessage msg1 = new FrontOrBackMessage(myMatch.idMatch, this.username, c);
                Client.messageToServer(msg1);

                Thread.sleep(1000);

                resetBoard();
                myBoard[5][5] = myPlayer.getInitialCard().getCode();
                showBoard();
                this.first++;
                System.out.println("Game is Start!");
            }
            if(myMatch.getCurrentPlayer().nickname.equals(this.username)) {
                System.out.println("it's your round!!!");
                showBoard();
            } else {
                System.out.println(myMatch.getCurrentPlayer().nickname + " " + this.username + " " + myPlayer.nickname);
                System.out.println("it's " + myMatch.getCurrentPlayer().nickname + "'s turn");
                showBoard();
            }

            while(true) {

            }

    }


    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    private void resetBoard() {
        myBoard = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
    }

}
