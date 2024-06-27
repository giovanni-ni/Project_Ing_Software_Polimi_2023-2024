package it.polimi.ingsw.view.TextualInterfaceUnit;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.Networking.Client;
import it.polimi.ingsw.Networking.DefaultPort;
import it.polimi.ingsw.Networking.rmi.RMIClient;
import it.polimi.ingsw.Networking.socket.SocketClient;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.Ui;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.*;

/**
 * The `Tui` class represents a Text User Interface for a game, implementing the `Ui` interface.
 * It handles user interactions, game state management, and communication with the server.
 *
 * @see Ui
 */
public class Tui  implements Ui {

    /**
     * The username of the player using this Tui instance.
     */
    private String username;


    /**
     * Static reference to the player's status.
     *
     * @see PlayerStatus
     */
    public static PlayerStatus status;

    /**
     * Static variable indicating whether there has been a change in the game state.
     */
    public static int hasChange;

    /**
     * Static boolean indicating whether the player has played in the current round.
     */
    public static boolean hasPlayed;

    /**
     * Static reference to the ViewModel of the current match.
     *
     * @see ViewModel
     */
    public static ViewModel myMatch;

    /**
     * 2D array representing the game board.
     */
    private int[][] myBoard;

    /**
     * Static reference to the player object.
     *
     * @see Player
     */
    public static Player myPlayer;

    /**
     * Boolean indicating if the connection type is RMI.
     */
    private boolean isRMI = false;

    /**
     * Boolean indicating if the connection type is Socket.
     */
    private boolean isSocket = false;

    /**
     * Scanner object for reading user input.
     */
    private final Scanner in;

    /**
     * Integer representing the first player in the game.
     */
    private int first;

    /**
     * Client object for communication with the server.
     *
     * @see Client
     */
    private Client client;

    /**
     * Static ArrayList storing chat messages.
     *
     * @see ServerChatMessage
     */
    public static ArrayList<ServerChatMessage> chat;

    /**
     * Integer for autostart configuration.
     */
    private int autostart;


    /**
     * Static boolean indicating if the player is choosing a color.
     */
    public static boolean choosingColor;

    /**
     * Constructor for the Tui class. Initializes the Tui and sets up the initial game state.
     *
     * @throws IOException if there is an error reading from the standard input.
     */
    public Tui() throws IOException {
        in = new Scanner(System.in);
        this.status = PlayerStatus.MENU;
        myMatch = new ViewModel ( new Match(0));
        this.first = 0;
        myPlayer = new Player("");
        myPlayer.setReady(false);
        chat = new ArrayList<>();
        choosingColor = true;
    }

    /**
     * Initializes the game by setting up the connection type, logging in, and starting the game loop.
     *
     * @throws Exception if there is an error during initialization.
     */
    public void init() throws Exception {
        autostart = 0;
        print(Print.Codex);
        askToContinue();
        askConnectionType();

        if(isRMI) {
            client = new RMIClient("localhost", DefaultPort.RMIPORT.getNumber(), this);
        } else {
            client = new SocketClient("localhost", DefaultPort.SOCKETPORT.getNumber(), this);
        }

        askLogin();

        while(true) {
            redirect();
            System.out.flush();
        }


    }

    /**
     * Asks the user to choose the connection type (RMI or Socket).
     */
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
                print("Invalid input. Try again. (1 or 2)");
            }
        } while (option != 1 && option != 2);

        if(option == 1) {
            isRMI = true;
        } else {
            isSocket = true;
        }
    }

    /**
     * Waits for the user to press Enter to continue.
     */
    public void askToContinue() {
        print("Press 'Enter' to continue");
        in.nextLine();
        print("-----------------------------------------------------------");
    }

    /**
     * Asks the user to enter their username.
     *
     * @throws Exception if there is an error during the login process.
     */
    public void askLogin() throws Exception {
        String user;
        do{

            print("Enter the username: ");
            user = in.nextLine();
            if (user.isEmpty()) {
                print("You didn't choose a username. Try again");

            } else {
                this.username = user;
                myPlayer = new Player(username);

                return;
            }
        } while(user.isEmpty());

    }

    /**
     * Asks the user to choose an action from the menu.
     *
     * @throws Exception if there is an error during menu action selection.
     */
    public void askMenuAction() throws Exception {
        print(Print.menuOption);
        print("-----------------------------------------------------------\n" +
                "Enter the Command you wish to use: ");
        String option;
        do {
             option = in.nextLine();
             if(!(option.equals("create") || option.equals("join") || option.equals("play") || option.equals("exit") || option.equals("c") || option.equals("j") || option.equals("p") || option.equals("ex") || option.equals("cr") || option.equals("jo") || option.equals("pl"))){
                 System.out.println("error");
                 System.out.println("Enter the Command you wish to use: ");
             }
        }while(!(option.equals("create") || option.equals("join") || option.equals("play") || option.equals("exit") || option.equals("c") || option.equals("j") || option.equals("p") || option.equals("ex") || option.equals("cr") || option.equals("jo") || option.equals("pl")));

        switch (option) {
            case "create", "c", "cr" -> askCreateMatch();
            case "join", "j", "jo" -> askJoinMatch();
            case "play", "p", "pl" -> askJoinFirst();
            case "exit", "ex" -> {
                if (askExitGame()) return;
            }

            default ->
                    print(Color.RED + "The [" + option + "] command cannot be found! Please try again.");
        }

    }


    public void askJoinFirst() throws InterruptedException, RemoteException {
        GenericClientMessage msg = new JoinFirstMessage(this.username);
        client.messageToServer(msg);
        Thread.sleep(1000);
    }


    public void askSetReady() throws InterruptedException, RemoteException {
        String option;

        if(autostart == 0) {
            if (!myPlayer.getReady()) {
                do {

                    print("Ready? y/n: ");
                    option = in.nextLine();
                    if(option.equals("y")){
                        SetReadyMessage msg = new SetReadyMessage(myMatch.getIdMatch(),this.username);
                        myPlayer.setReady(true);
                        client.messageToServer(msg);
                    }
                    Thread.sleep(1000);
                } while(!option.equals("y"));

            }
        }
        System.out.flush();
    }

    public void askPlayCard() throws InterruptedException, RemoteException {
        print("choose the card that you want to play: ");
        int index = 3;
        String s;
        do {
            s = in.nextLine();
            if(!s.equals("0") && !s.equals("1") && !s.equals("2")) {
                print("error");
                print("choose the card that you want to play: ");
            } else {
                index = Integer.parseInt(s);
            }
        }while(index != 0 && index != 1 && index != 2);

        int code = myPlayer.getCardOnHand().get(index).getCode();

        print("front or back: f/b");
        String front;
        do {
            front = in.nextLine();
            if(!(front.equals("f")) && !(front.equals("b"))) {
                print("error");
                print("front or back: f/b");
            }
        }while(!(front.equals("f")) && !(front.equals("b")));

        boolean f = false;
        if(front.equals("f")) {
            f = true;
        }
        print("position x: ");
        do {
            s = in.nextLine();
            if(s.isEmpty())
                print("position x: ");
        } while(s.isEmpty());

        int x = Integer.parseInt(s);

        print("position y: ");

        do {
            s = in.nextLine();
            if(s.isEmpty())
                print("position y: ");
        } while(s.isEmpty());

        int y = Integer.parseInt(s);

        playCardMessage msg = new playCardMessage(this.username, index, f, x, y);
        client.messageToServer(msg);
        Thread.sleep(1000);

        if(status == PlayerStatus.Draw) {
            myBoard[x+10][10-y] = code;
        }
    }


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


    public void askCreateMatch() throws Exception {

        String choice;

        do {
            print("autostart? y/n");
            choice = in.nextLine();

            if(!choice.equals("y") && !choice.equals("n")) {
                print("error");
            }

        } while(!choice.equals("y") && !choice.equals("n"));

        CreateGameMessage message;

        if(choice.equals("y")) {
            int numPlayer = askMaxSeats();
            message = new CreateGameMessage(this.username, numPlayer);
            autostart = 1;
        } else {
            message = new CreateGameMessage(this.username);
        }


        client.messageToServer(message);

        Thread.sleep(1000);

    }

    private int askMaxSeats() throws Exception {
        int playerNumber = 0;
        String s;
        do {

                print("Please select the number of players for this match [2 to 4]: ");

                s = in.nextLine();
                if(!s.equals("2") && !s.equals("3") && !s.equals("4")) {
                    print("error");
                    print("Please select the number of players for this match [2 to 4]: ");
                } else {
                    playerNumber = Integer.parseInt(s);
                }

        } while (!s.equals("2") && !s.equals("3") && !s.equals("4"));
        print("Selected " + playerNumber + " players.");
        return playerNumber;
    }


    public void askJoinMatch() throws Exception {
        String value;
        do {
            print("---------------------------------------------");
            print("Please enter the room number: ");
            value = in.nextLine();
        } while (value.isEmpty());
        int matchID = Integer.parseInt(value);
        print("Selected Room [" + matchID + "].");
        GenericClientMessage msg = new JoinGameMessage(this.username, matchID);
        client.messageToServer(msg);
        Thread.sleep(1000);
    }


    public boolean askExitGame() throws RemoteException {
        System.exit(0);
        return false;
    }


    public void showCommonGoals() {
        print("common targets");
        for(TargetCard c: myMatch.getCommonTarget()) {
            print(c.getIdCard());
        }
    }


    public void showPersonalGoal() throws RemoteException {
        print("your target card");
        print(myPlayer.getTarget().getIdCard());
    }


    public void showBoard() {
        int p = 10;
        print("-9 -8 -7 -6 -5 -4 -3 -2 -1 0 1 2 3 4 5 6 7 8 9");
        print("");
        print("");
        for (int i = 0; i < myBoard.length; i++) {
            for (int j = 0; j < myBoard[0].length; j++) {
                System.out.print( myBoard[i][j]+" ");
            }
            print("        "+p);
            p--;
        }
    }

    /**
     * Handles redirection to appropriate game state methods based on the current status.
     *
     * @throws Exception if there is an error during redirection.
     */
    public void redirect() throws Exception {
        if(status == PlayerStatus.MENU) {
            askMenuAction();
        }

        if(status == PlayerStatus.MatchStart) {
            if(!myMatch.getAutostart())
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

    /**
     * Prepares the game by setting up the board, choosing colors, and initializing game elements.
     *
     * @throws IOException if there is an error reading from the standard input.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */

    public void prepareGame() throws IOException, InterruptedException {
        List<Card> deck = new ArrayList<>();
        TargetCard[] target = {};

        while(choosingColor) {
            print("choose a color from:");
            for( int i = 0; i < myMatch.getPlayerColors().size(); i++ ) {
                print( myMatch.getPlayerColors().get(i) );
            }
            String color;
            do {
                color = in.nextLine();
                ChooseColorMsg msg;
                switch(color) {
                    case "red":
                        msg = new ChooseColorMsg(myPlayer.getNickname(), myMatch.idMatch, PlayerColor.RED);
                        client.messageToServer(msg);
                        break;
                    case "blue":
                        msg = new ChooseColorMsg(myPlayer.getNickname(), myMatch.idMatch, PlayerColor.BLUE);
                        client.messageToServer(msg);
                        break;
                    case "yellow":
                        msg = new ChooseColorMsg(myPlayer.getNickname(), myMatch.idMatch, PlayerColor.YELLOW);
                        client.messageToServer(msg);
                        break;
                    case "green":
                        msg = new ChooseColorMsg(myPlayer.getNickname(), myMatch.idMatch, PlayerColor.GREEN);
                        client.messageToServer(msg);
                        break;
                    default:
                        print("error, choose a color");
                        break;
                }
            } while(!color.equals("blue") && !color.equals("red") && !color.equals("yellow") && !color.equals("green"));


            choosingColor = false;

            Thread.sleep(1000);

            if(choosingColor) {
                print("error");
            }
        }

        showDeck();

        int choice;
        String s;
        printCard(myPlayer.getInitialCard());

        print("this is your initial card: choose front(0) or back(1) ");

            s = in.nextLine();

            while(!s.equals("0") && !s.equals("1")) {
                print("error");
                print(" front(0) or back(1) ");
                s = in.nextLine();
            }
            choice = Integer.parseInt(s);
        boolean b;
        if(choice == 0) {
            b = true;
        } else {
            b = false;
        }

        FrontOrBackMessage msg1 = new FrontOrBackMessage(myMatch.idMatch, this.username, b);
        client.messageToServer(msg1);

        Thread.sleep(1000);

        print("choose your personal target card from: ");
        print("0: ");
        printCard(myPlayer.getTargetOnHand()[0]);
        print("1: ");
        printCard(myPlayer.getTargetOnHand()[1]);

            s = in.nextLine();
            while(!s.equals("0") && !s.equals("1")) {

                print("error");
                print("choose your personal target card: 0 or 1 ");
                s = in.nextLine();
            }
            choice = Integer.parseInt(s);

        SetTargetCardMessage msg = new SetTargetCardMessage(myMatch.idMatch, this.username, choice);
        client.messageToServer(msg);

        Thread.sleep(1000);

        showMyCard();

        resetBoard();
        myBoard[10][10] = myPlayer.getInitialCard().getCode();
        showBoard();
        this.first++;

        boolean waiting = true;
        while(myMatch.getPlayerColors().size() != 4 - myMatch.getPlayers().size()) {
            if(waiting) {
                print("...waiting other players...");
                waiting = false;

            }
            System.out.flush();
        }

        print("Game is Start!");
        status = PlayerStatus.GamePlay;
    }


    private void showDeck() {
        print("resource cards: ");
        printCard( myMatch.getResourceDeck().get(0));
        printCard( myMatch.getResourceDeck().get(1));
        print("kingdom of the third card: ");
        print(myMatch.getResourceDeck().get(2).getKingdom());

        print("gold cards: ");
        printCard(myMatch.getGoldDeck().get(0));
        printCard(myMatch.getGoldDeck().get(1));
        print("kingdom of the third card: ");
        print(myMatch.getGoldDeck().get(2).getKingdom());

    }

    /**
     * Ends the game and displays the final scores and winners.
     */
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


    /**
     * Draws a card from the deck.
     *
     * @throws IOException if there is an error reading from the standard input.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public void drawCard() throws IOException, InterruptedException {
        print("draw a card:\n" +
                "resource cards: " + myMatch.getResourceDeck().get(0).getCode() + " " + myMatch.getResourceDeck().get(1).getCode() +
                "kingdom of the third card: " + myMatch.getResourceDeck().get(2).getKingdom() +
                "\ngold cards: " + myMatch.getGoldDeck().get(0).getCode() + " " + myMatch.getGoldDeck().get(1).getCode() +
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
                String s;
                print(" resource card: first(0) second(1) third(2) ");
                print(" gold card: first(3) second(4) third(5) ");
                print("choose the card that you want to draw : ");
                s = in.nextLine();

                while(!s.equals("0") && !s.equals("1") && !s.equals("2") && !s.equals("3") && !s.equals("4") && !s.equals("5")) {
                    print("error");
                    print("choose the card that you want to draw : ");
                }

                int choice = Integer.parseInt(s);
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

    /**
     * Handles in-game actions such as playing cards, chatting, and viewing game elements.
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     * @throws IOException if there is an error reading from the standard input.
     */
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
            showPoints();
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

    private void showPoints() {
        for(Player p : myMatch.getPlayers()) {
            print(p.getNickname() + ": " + "[" + p.getPlayerID() + "] "+ p.currentScore + " points");
        }
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
        System.out.println();
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


    /**
     * Handles messages received from the server.
     *
     * @param msg the message received from the server
     */
    @Override
    public void handleMessage(GenericServerMessage msg)  {
        if(msg instanceof joinSuccessMsg) {
            if(!status.equals(PlayerStatus.Preparing)) {
                status = PlayerStatus.MatchStart;
                myMatch = ((joinSuccessMsg) msg).getModel();
                myPlayer = ((joinSuccessMsg) msg).getModel().getPlayers().getLast();
                print(myMatch.idMatch);
            }
        } else if(msg instanceof joinFailMsg) {
            print("join fail because" + ((joinFailMsg) msg).getDescription());
        }else if(msg instanceof ServerChatMessage) {
            print("New chat Message:");
            showNewChatMessage(msg);
            //store the chat for historical chat view
            chat.add((ServerChatMessage)msg);
        } else if(msg instanceof newPlayerInMsg) {
            print("new player is in " + ((newPlayerInMsg) msg).getNicknameNewPlayer());
        } else if(msg instanceof gameStartMsg) {
            //print("the game is starting.. 3.. 2.. 1..");
            //print("numero di giocatori del ultimo model"+((gameStartMsg) msg).getModel().getPlayers().size());
            myMatch = ((gameStartMsg) msg).getModel();
            myPlayer = ((gameStartMsg) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
            status = PlayerStatus.Preparing;
            //print("game status change" );

        } else if(msg instanceof playCardSuccess) {
            Tui.myMatch = ((playCardSuccess) msg).getModel();
            Tui.myPlayer = ((playCardSuccess) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
            Tui.hasChange = 1;
            Tui.hasPlayed = true;
            if(((playCardSuccess) msg).getModel().getCurrentPlayer().getNickname().equals(Tui.myPlayer.nickname)) {
                Tui.status = PlayerStatus.Draw;
            }

        } else if(msg instanceof drawCardSuccess) {
            Tui.status = PlayerStatus.GamePlay;
            Tui.myMatch = ((drawCardSuccess) msg).getModel();
            Tui.myPlayer = ((drawCardSuccess) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
        }
        else if(msg instanceof ActionSuccessMsg) {
            Tui.myMatch = ((ActionSuccessMsg) msg).getModel();
            Tui.myPlayer = ((ActionSuccessMsg) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
        } else if(msg instanceof ActionNotRecognize) {
            Tui.printMessage(((ActionNotRecognize) msg).getDescription());
            if(((ActionNotRecognize) msg).getDescription().equals("Color already choose")) {
                Tui.choosingColor = true;
            }
        } else if(msg instanceof NowIsYourRoundMsg) {
            Tui.printMessage(((NowIsYourRoundMsg) msg).getDescription());
        } else if(msg instanceof LastRoundMessage) {
            Tui.printMessage("ATTENTION !! it's the last round");
        } else if(msg instanceof endGameMessage) {
            Tui.status = PlayerStatus.END;
            Tui.myMatch = ((endGameMessage) msg).getModel();
            Tui.myPlayer = ((endGameMessage) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
        } /*else if(msg instanceof ReconnectSuccess) {

            Tui.myMatch = ((ReconnectSuccess) msg).getModel();
            Tui.myPlayer = ((ReconnectSuccess) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
            if(Tui.myMatch.getStatus().equals(MatchStatus.Playing)) {
                Tui.status = PlayerStatus.GamePlay;
            }

            print("welcome back " + Tui.myPlayer.nickname);
        }*/

        /*if(msg instanceof ActionSuccessMsg /*|| msg instanceof drawCardSuccess || msg instanceof endGameMessage || msg instanceof gameStartMsg || msg instanceof joinSuccessMsg || msg instanceof playCardSuccess) {
            Tui.myMatch = ((ActionSuccessMsg) msg).getModel();

        }*/

    }

    /**
     * Asks the user to reconnect to the game.
     *
     * @throws RemoteException if there is an error in remote communication.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public void askReconnect() throws RemoteException, InterruptedException {
        print("enter your room: ");
        int num = Integer.parseInt(in.nextLine());
        print("enter your name: ");
        String name = in.nextLine();
        ReconnectRequestMsg msg = new ReconnectRequestMsg(name, num);
        client.messageToServer(msg);

        Thread.sleep(3000);
    }
}
