package it.polimi.ingsw.view.TextualInterfaceUnit;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.ServerToClientMsg.ServerChatMessage;
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

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.*;

public class Tui /*extends Thread*/ implements View{

    private String username;

    public static PlayerStatus status;

    public static int hasChange;

    public static boolean hasPlayed;

    public static ViewModel myMatch;

    private int[][] myBoard;

    public static Player myPlayer;

    public static int matchID;

    private boolean isRMI = false;

    private boolean isSocket = false;

    private final Scanner in;

    private int first;

    private Client client;

    public static ArrayList<ServerChatMessage> chat;

    public Tui() throws IOException {
        in = new Scanner(System.in);
        this.status = PlayerStatus.MENU;
        myMatch = new ViewModel ( new Match(0));
        this.first = 0;
        myPlayer = new Player("");
        myPlayer.setReady(false);
        chat = new ArrayList<>();
    }

    /*@Override
    public void run() {
        while(true) {
            if(hasChange == 1) {
                try {
                    hasChange = 0;
                    redirect();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }*/

    public void init() throws Exception {
        print(Print.Codex);
        askToContinue();
        askConnectionType();

        if(isRMI) {
            final String serverName = "AdderServer";
            Registry registry = LocateRegistry.getRegistry("localhost", 2334);
            VirtualServer server = (VirtualServer) registry.lookup(serverName);
            client = new RMIClient(server);
        } else {
            client = new SocketClient("localhost", 4234);
        }

        askLogin();
        //this.start();
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

                if(username.equals("nuge")) {
                    print("nu哥yyds");
                }
                return;
            }
        } while(username.equals(""));

    }
    @Override
    public void askMenuAction() throws Exception {
        print(Print.menuOption);
        print("-----------------------------------------------------------\n" +
                "Enter the Command you wish to use: ");
        String option;
        do {
             option = in.nextLine();
             if(!(option.equals("create") || option.equals("join") || option.equals("play") || option.equals("exit") || option.equals("c") || option.equals("j") || option.equals("p") || option.equals("ex") || option.equals("cr") || option.equals("jo") || option.equals("pl"))){
                 System.out.println("error");
             }
        }while(!(option.equals("create") || option.equals("join") || option.equals("play") || option.equals("exit") || option.equals("c") || option.equals("j") || option.equals("p") || option.equals("ex") || option.equals("cr") || option.equals("jo") || option.equals("pl")));

        switch (option) {
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
                    print("Waiting");
                }
                Thread.sleep(1000);
            } while(!option.equals("y"));

        }
        System.out.flush();
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

        playCardMessage msg = new playCardMessage(this.username, index, f, x, y);
        client.messageToServer(msg);
        Thread.sleep(1000);

        if(status == PlayerStatus.Draw) {
            myBoard[x+10][y+10] = myPlayer.getCardOnHand().get(index).getCode();
        }
    }

    @Override
    public void askChat() throws RemoteException, InterruptedException {

        print("who do you want to send the message to: (BROADCAST for all players)");
        for(Player p: myMatch.getPlayers()){
            if(!p.getNickname().equals(myPlayer.nickname)) {
                print(p.getNickname());
            }
        }
        String toPlayer = in.nextLine();
        print("your message: ");
        String chatMsg = in.nextLine();

        if(toPlayer.equalsIgnoreCase("BROADCAST")) {
            ClientChatMessage msg = new ClientChatMessage(myMatch.getIdMatch(), this.username, true, "", chatMsg);
            client.messageToServer(msg);
        } else {
            ClientChatMessage msg = new ClientChatMessage(myMatch.getIdMatch(), this.username, false, toPlayer, chatMsg);
            client.messageToServer(msg);
        }

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
        Thread.sleep(1000);
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
        print("common targets");
        for(TargetCard c: myMatch.getCommonTarget()) {
            print(c.getIdCard());
        }
    }

    @Override
    public void showPersonalGoal() throws RemoteException {
        print("your target card");
        print(myPlayer.getTarget().getIdCard());
    }

    @Override
    public void announceCurrentPlayer() throws RemoteException {

    }

    @Override
    public void showBoard() {
        int p = 1;
        print("-9 -8 -7 -6 -5 -4 -3 -2 -1 0 1 2 3 4 5 6 7 8 9");
        print("");
        print("");
        for (int i = 0; i < myBoard.length; i++) {
            for (int j = 0; j < myBoard[0].length; j++) {
                System.out.print( myBoard[i][j]+" ");
            }
            print("        "+p);
            p++;
        }
    }

    public void redirect() throws Exception {
        if(status == PlayerStatus.MENU) {
            askMenuAction();
        }

        if(status == PlayerStatus.MatchStart) {

            askSetReady();
            //print("la partita sta per iniziare");
        }

        if(status == PlayerStatus.Preparing) {
            prepareGame();
        }

        if(status == PlayerStatus.GamePlay) {
            //print("game status change recognized, method calling...");
            inGame();

        }

        if(status == PlayerStatus.Draw) {
            if(hasPlayed) {
                drawCard();
            }
        }

        if(status == PlayerStatus.END) {
            endGame();
        }
    }

    public void prepareGame() throws RemoteException, InterruptedException {
        List<Card> deck = new ArrayList<>();
        TargetCard[] target = {};

        showMyCard();

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
        print("choose your personal target card from: ");
        print(myPlayer.getTargetOnHand()[0].getIdCard() + " " + myPlayer.getTargetOnHand()[1].getIdCard());
        int choice;
        do {
            choice = Integer.parseInt(in.nextLine());
            if(choice != 0 && choice != 1) {
                System.out.println("error");
            }
        } while(choice != 0 && choice != 1);

        SetTargetCardMessage msg = new SetTargetCardMessage(myMatch.idMatch, this.username, choice);
        client.messageToServer(msg);

        Thread.sleep(1000);

        print("this is your initial card:" + myPlayer.getInitialCard().getCode() + " front(0) or back(1) ");
        do {
            choice = Integer.parseInt(in.nextLine());
            if(choice != 0 && choice != 1) {
                System.out.println("error");
            }
        } while(choice != 0 && choice != 1);
        boolean c;
        if(choice == 0) {
            c = true;
        } else {
            c = false;
        }

        FrontOrBackMessage msg1 = new FrontOrBackMessage(myMatch.idMatch, this.username, c);
        client.messageToServer(msg1);

        Thread.sleep(1000);

        resetBoard();
        myBoard[10][10] = myPlayer.getInitialCard().getCode();
        showBoard();
        this.first++;
        print("Game is Start!");
        status = PlayerStatus.GamePlay;
    }
    @Override
    public void endGame() {
        print("GAME END");
        print("Point table: ");
        for(Player p: myMatch.getPlayers()) {
            print(p.getNickname() + ": " + p.currentScore + " points");
        }
        print("WINNER IS/ARE .... ");
        for(Player p: myMatch.getWinners()) {
            print(p.getNickname() + ": " + p.currentScore + " points");
        }
        print("!!!!!!!");
    }

    @Override
    public void drawCard() throws IOException, InterruptedException {
        print("draw a card:\n" +
                "resource cards: " + myMatch.getResourceDeck().get(0).getCode() + myMatch.getResourceDeck().get(1).getCode() +
                " kingdom of the third card: " + myMatch.getResourceDeck().get(2).getKingdom() +
                "\n gold cards: " + myMatch.getGoldDeck().get(0).getCode() + myMatch.getGoldDeck().get(1).getCode() +
                " kingdom of the third card: " + myMatch.getGoldDeck().get(2).getKingdom());
        String option;
        do{
            print(drawCard);
            option = in.nextLine();
            if(option.equals("chat")) {
                askChat();
            } else if(option.equals("s")) {
                askShowCard();
            } else if(option.equals("c")) {
                print("resource card: first(0) second(1) third(2)");
                print("gold card: first(3) second(4) third(5)");
                int choice = Integer.parseInt(in.nextLine());
                boolean isGoldDeck = false;
                if(choice > 2) {
                    isGoldDeck = true;
                    choice -= 3;
                }

                drawCardMessage msg = new drawCardMessage(this.username, myMatch.getIdMatch(), isGoldDeck, choice);
                client.messageToServer(msg);

            } else {
                print("errore");
            }
        } while(!option.equals("c"));

        Thread.sleep(1000);
    }

    /*public void waitingMethodReturn() {


                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


    }*/

    public void inGame() throws InterruptedException, IOException {
        //print("in game method called");

            if(myMatch.getCurrentPlayer().nickname.equals(this.username)) {
                print("it's your round!!!");
                hasPlayed = false;
            } else {
                //System.out.println(myMatch.getCurrentPlayer().nickname + " " + this.username + " " + myPlayer.nickname);
                System.out.println("it's " + myMatch.getCurrentPlayer().nickname + "'s turn");
            }

            showBoard();
            showMyCard();
            showElements();
            print(Print.menuOperations);
            print("-----------------------------------------------------------\n" +
                    "Enter the Command you wish to use: ");
            String option;
            do {
                option = in.nextLine();
                if(!(option.equals("p") || option.equals("c") || option.equals("sc") || option.equals("sp") || option.equals("s"))) {
                    System.out.println("error");
                }
            } while(!(option.equals("p") || option.equals("c") || option.equals("sc") || option.equals("sp") || option.equals("s")));


            switch (option) {
                case "p" -> askPlayCard();
                case "c" -> askChat();
                case "sc" -> showCommonGoals();
                case "sp" -> showPersonalGoal();
                case "s"  -> askShowCard();

                default -> {
                    print(Color.RED + "The [" + option + "] command cannot be found! Please try again.");
                    print(myMatch.getCurrentPlayer().nickname + " " + this.username + " " + myPlayer.nickname);
                    print("it's " + myMatch.getCurrentPlayer().nickname + "'s turn");
                    showBoard();
                }
            }
        Thread.sleep(1000);
    }

    private void askShowCard() throws IOException {
        print("which card do you want:");
        printCardById(Integer.parseInt(in.nextLine()));
    }
    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    private void resetBoard() {
        myBoard = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
    }

    private void showElements() {
        System.out.print("now you have: ");
        System.out.println("MUSHROOMS: " + myPlayer.getBoard().getCounterOfElements().get(Elements.MUSHROOMS) +
                        " VEGETAL: " + myPlayer.getBoard().getCounterOfElements().get(Elements.VEGETAL) +
                        " ANIMALS: " + myPlayer.getBoard().getCounterOfElements().get(Elements.ANIMALS) +
                        " INSECT: " + myPlayer.getBoard().getCounterOfElements().get(Elements.INSECT) +
                        " FEATHER: " + myPlayer.getBoard().getCounterOfElements().get(Elements.FEATHER) +
                        " INK: " + myPlayer.getBoard().getCounterOfElements().get(Elements.INK) +
                        " PARCHMENT: " + myPlayer.getBoard().getCounterOfElements().get(Elements.PARCHMENT)
                         );
    }

    private void showMyCard() {
        System.out.print("your cards: ");
        for(Card c: myPlayer.getCardOnHand()) {
            System.out.print(c.getCode() + " ");
        }
        System.out.println("");
    }

    //someWhere to create a ChatMessage
    private void showAllChatMessage() {
        for (ServerChatMessage msg :Tui.chat){
            showNewChatMessage(msg);
        }
    }

    public static void printMessage(String message) {
        print(message);
    }
}
