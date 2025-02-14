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


    private boolean connectionSuccess;

    private boolean finish;

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
        finish = false;
    }

    /**
     * Initializes the game by setting up the connection type, logging in, and starting the game loop.
     *
     * @throws Exception if there is an error during initialization.
     */
    public void init() throws Exception {
        autostart = 0;
        connectionSuccess = false;
        print(Print.Codex);
        askToContinue();
        do {
            isRMI = false;
            isSocket = false;
            String s = askConnectionType();
            if(s.isEmpty()) {
                s = "localhost";
            }
            if(isRMI) {
                client = new RMIClient(s, DefaultPort.RMIPORT.getNumber(), this);
            } else {
                client = new SocketClient(s, DefaultPort.SOCKETPORT.getNumber(), this);
            }
            Thread.sleep(1000);
            if(!connectionSuccess) {
                print("connection failed retry...");
            }
        } while(!connectionSuccess);

        askLogin();

        while(true) {
            redirect();
            System.out.flush();
        }


    }

    /**
     * Asks the user to choose the connection type (RMI or Socket).
     */
    public String askConnectionType() {
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
                print(ANSI_MUSHROOM + "Invalid input. Try again. (1 or 2)" +ANSI_RESET);
            }
        } while (option != 1 && option != 2);

        if(option == 1) {
            isRMI = true;
        } else {
            isSocket = true;
        }

        print("insert server IP([ENTER] for localhost):  ");
        return in.nextLine();
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


    /**
     * Sends a request to join the game session first.
     *
     * @throws InterruptedException if the thread is interrupted while sleeping.
     * @throws RemoteException      if a remote exception occurs during communication.
     */
    public void askJoinFirst() throws InterruptedException, RemoteException {
        GenericClientMessage msg = new JoinFirstMessage(this.username);
        client.messageToServer(msg);
        Thread.sleep(1000);
    }


    /**
     * Asks the player if they are ready to start and sends their readiness status
     * to the server.
     *
     * @throws InterruptedException if the thread is interrupted while sleeping.
     * @throws RemoteException      if a remote exception occurs during communication.
     */
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

    /**
     * Asks the player to choose a card, position, and orientation to play,
     * then sends the chosen card's information to the server.
     *
     * @throws InterruptedException if the thread is interrupted while sleeping.
     * @throws RemoteException      if a remote exception occurs during communication.
     */
    public void askPlayCard() throws InterruptedException, RemoteException {
        print("choose the card that you want to play: ");
        for(int i=0; i< myPlayer.getCardOnHand().size(); i++ ) {
            print(i + " for card " + myPlayer.getCardOnHand().get(i).getCode());
        }

        int index = 3;

        do {
            try {
                print("choose:");
                index = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                print(ANSI_MUSHROOM + "Invalid input. Try again. (0 or 1 or 2 )"+ANSI_RESET);
            }
        }while(index < 0 || index > myPlayer.getCardOnHand().size()-1);

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

        Integer x;
        do {
            try {
                x = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                print(ANSI_MUSHROOM + "Try again. Must be a number"+ANSI_RESET);
                x = null;
            }
        } while(x == null);

        print("position y: ");
        Integer y;
        do {
            try {
                y = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                print(ANSI_MUSHROOM + "Try again. Must be a number"+ANSI_RESET);
                y = null;
            }
        } while(y == null);

        playCardMessage msg = new playCardMessage(this.username, index, f, x, y);
        client.messageToServer(msg);
        Thread.sleep(3000);

        if(status == PlayerStatus.Draw) {
            myBoard[10-x][y+10] = code;
        }
    }


    /**
     * Asks the player to whom they want to send a message and the content of the message,
     * then sends the chat message to the appropriate recipient(s) on the server.
     *
     * @throws RemoteException      if a remote exception occurs during communication.
     * @throws InterruptedException if the thread is interrupted while sleeping.
     */
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


    /**
     * Asks the player if they want to create a match with auto-start option,
     * and sends the match creation request to the server.
     *
     * @throws Exception if an error occurs during match creation.
     */
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

    /**
     * Asks the player to select the number of seats (players) for a match.
     *
     * @return the number of players selected for the match.
     * @throws Exception if an error occurs during input or if an invalid number of seats is selected.
     */
    private int askMaxSeats() throws Exception {
        int playerNumber = 0;
        do {

                print("Please select the number of players for this match [2 to 4]: ");
                try {
                    playerNumber = Integer.parseInt(in.nextLine());
                } catch(NumberFormatException e) {
                    print(ANSI_MUSHROOM + "Try again! 2 to 4"+ANSI_RESET);
                }
        } while (playerNumber != 2 && playerNumber != 3 && playerNumber != 4);

        print("Selected " + playerNumber + " players.");
        return playerNumber;
    }


    /**
     * Asks the player to enter the ID of the match they want to join,
     * then sends the join request to the server.
     *
     * @throws Exception if an error occurs during match joining.
     */
    public void askJoinMatch() throws Exception {
        int matchID = -1;
        do {
            print("---------------------------------------------");
            print("Please enter the room number: ");
            try {
                matchID = Integer.parseInt(in.nextLine());
            } catch(NumberFormatException e) {
                print(ANSI_MUSHROOM + "Try again! must be a number > 0"+ANSI_RESET);
                matchID = -1;
            }
        } while (matchID == -1);
        print("Selected Room [" + matchID + "].");
        GenericClientMessage msg = new JoinGameMessage(this.username, matchID);
        client.messageToServer(msg);
        Thread.sleep(1000);
    }


    /**
     * Exits the game.
     *
     * @return always returns false (unreachable code due to System.exit(0)).
     * @throws RemoteException if a remote exception occurs during communication.
     */
    public boolean askExitGame() throws RemoteException {
        System.exit(0);
        return false;
    }

    /**
     * Displays the common target cards for the current match.
     *
     * @throws IOException if an I/O error occurs while printing the cards.
     */
    public void showCommonGoals() throws IOException {
        print("common targets");
        for(TargetCard c: myMatch.getCommonTarget()) {
            printCardById(c.getIdCard());
        }
    }


    /**
     * Displays the personal target card of the player.
     *
     * @throws IOException if an I/O error occurs while printing the card.
     */
    public void showPersonalGoal() throws IOException {
        print("your target card");
        printCardById(myPlayer.getTarget().getIdCard());
    }


    /**
     * Displays the game board with current positions and values.
     * Uses a grid layout with coordinates.
     */
    public void showBoard() {
        int p = 10;
        print("-10\t-9\t-8\t-7\t-6\t-5\t-4\t-3\t-2\t-1\t0\t1\t2\t3\t4\t5\t6\t7\t8");
        print("");
        for (int i = 0; i < myBoard.length; i++) {
            for (int j = 0; j < myBoard[0].length; j++) {
                System.out.print( myBoard[i][j]+"\t");
            }
            print("\t"+p);
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
            if(hasPlayed && (!myMatch.getGoldDeck().isEmpty() || !myMatch.getResourceDeck().isEmpty())) {
                drawCard();
            } else {
                status = PlayerStatus.GamePlay;
            }
        }

        if(status == PlayerStatus.END) {
            endGame();
        }

        if(status == PlayerStatus.Finish && !finish ) {
            //print(Print.GameEnd);
            finish = true;
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

        int choice = -1;

        printCard(myPlayer.getInitialCard());

        print("this is your initial card:  ");

        do {

            try {
                print("choose front(0) or back(1): ");
                choice = Integer.parseInt(in.nextLine());
            } catch(NumberFormatException e) {
                print(ANSI_MUSHROOM + "Try again! 0 or 1"+ANSI_RESET);
            }

        }while(choice != 0 && choice != 1);

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

        do {
            try {
                print("0 or 1: ");
                choice = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                print(ANSI_MUSHROOM + "Try Again! 0 or 1"+ANSI_RESET);
                choice = -1;
            }
        } while(choice != 0 && choice != 1);

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


    /**
     * show the deck on table which can be drawn by the players
     */
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
        Tui.status = PlayerStatus.Finish;
        print(Print.GameEnd);

    }


    /**
     * Draws a card from the deck.
     *
     * @throws IOException if there is an error reading from the standard input.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public void drawCard() throws IOException, InterruptedException {
        print("draw a card:\n");
        print("resource cards: ");
        for(int i = 0; i < 3 && i < myMatch.getResourceDeck().size(); i++) {
           if(i == 0 || i == 1) {
               print( myMatch.getResourceDeck().get(i).getCode()+"(" + i + ")" + " ");
           } else {
               print( " kingdom of the third card(2): " + myMatch.getResourceDeck().get(i).getKingdom());
           }
        }
        print("gold cards: ");
        for(int i = 0; i < 3 && i < myMatch.getGoldDeck().size(); i++) {
            if(i == 0 || i == 1) {
                int y = i+3;
                print(myMatch.getGoldDeck().get(i).getCode() + "(" + y + ")"  + " ");
            } else {
                print(" kingdom of the third card(5): " + myMatch.getGoldDeck().get(i).getKingdom());
            }
        }
        String option;
        do{
            print(drawCard);
            print("action you want to do: ");
            option = in.nextLine();
            if(option.equals("chat")) {
                askChat();
            } else if(option.equals("s")) {
                askShowCard();
            } else if(option.equals("c")) {
                String s;

                int choice = -1;
                do {
                    try {
                        print("choose the card that you want to draw : ");
                        choice = Integer.parseInt(in.nextLine());
                    } catch(NumberFormatException e) {
                        print(ANSI_MUSHROOM + "Try Again!"+ANSI_RESET);
                    }
                } while(choice != 0 && choice != 1 && choice != 2 && choice != 3 && choice != 4 && choice != 5);

                boolean isGoldDeck = false;
                if(choice > 2) {
                    isGoldDeck = true;
                    choice -= 3;
                }

                DrawCardMessage msg = new DrawCardMessage(this.username, myMatch.getIdMatch(), isGoldDeck, choice);
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

    /**
     * show every player's points
     */
    private void showPoints() {
        for(Player p : myMatch.getPlayers()) {
            print(p.getNickname() + ": " + "[" + p.getPlayerID() + "] "+ p.currentScore + " points");
        }
    }

    /**
     * use to print a specific card by its code
     */
    private void askShowCard() throws IOException {

        boolean success;
        do {
            try {
                print("insert the code of the card:");
                success = true;
                printCardById(Integer.parseInt(in.nextLine()));
            } catch (NumberFormatException e) {
                print("is not a number");
                success = false;
            }
        } while(!success);

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
        if(msg instanceof JoinSuccessMsg) {
            if(!status.equals(PlayerStatus.Preparing)) {
                status = PlayerStatus.MatchStart;
                myMatch = ((JoinSuccessMsg) msg).getModel();
                myPlayer = ((JoinSuccessMsg) msg).getModel().getPlayers().getLast();
                print("roomID: ");
                print(myMatch.idMatch);
            }
        } else if(msg instanceof JoinFailMsg) {
            print("join fail because" + ((JoinFailMsg) msg).getDescription());
        }else if(msg instanceof ServerChatMessage) {
            print("New chat Message:");
            showNewChatMessage(msg);
            //store the chat for historical chat view
            chat.add((ServerChatMessage)msg);
        } else if(msg instanceof NewPlayerInMsg) {
            print("new player is in " + ((NewPlayerInMsg) msg).getNicknameNewPlayer());
        } else if(msg instanceof GameStartMsg) {
            //print("the game is starting.. 3.. 2.. 1..");
            //print("numero di giocatori del ultimo model"+((GameStartMsg) msg).getModel().getPlayers().size());
            myMatch = ((GameStartMsg) msg).getModel();
            myPlayer = ((GameStartMsg) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
            status = PlayerStatus.Preparing;
            //print("game status change" );

        } else if(msg instanceof PlayCardSuccess) {
            Tui.myMatch = ((PlayCardSuccess) msg).getModel();
            Tui.myPlayer = ((PlayCardSuccess) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
            Tui.hasChange = 1;
            Tui.hasPlayed = true;
            if(((PlayCardSuccess) msg).getModel().getCurrentPlayer().getNickname().equals(Tui.myPlayer.nickname)) {
                Tui.status = PlayerStatus.Draw;
            }

        } else if(msg instanceof DrawCardSuccess) {
            Tui.status = PlayerStatus.GamePlay;
            Tui.myMatch = ((DrawCardSuccess) msg).getModel();
            Tui.myPlayer = ((DrawCardSuccess) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
        }
        else if(msg instanceof ActionSuccessMsg) {
            if(!connectionSuccess) {
                connectionSuccess = true;
            }
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
        } else if(msg instanceof EndGameMessage) {
            Tui.status = PlayerStatus.END;
            Tui.myMatch = ((EndGameMessage) msg).getModel();
            Tui.myPlayer = ((EndGameMessage) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
            endGame();
        } else if(msg instanceof LeaveMessage) {
            print(((LeaveMessage) msg).getLeftPlayer() + " lost. GAME OVER");
            print(Print.GameEnd);
            System.exit(0);
        }


            /*else if(msg instanceof ReconnectSuccess) {

            Tui.myMatch = ((ReconnectSuccess) msg).getModel();
            Tui.myPlayer = ((ReconnectSuccess) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
            if(Tui.myMatch.getStatus().equals(MatchStatus.Playing)) {
                Tui.status = PlayerStatus.GamePlay;
            }

            print("welcome back " + Tui.myPlayer.nickname);
        }*/


        /*if(msg instanceof ActionSuccessMsg /*|| msg instanceof DrawCardSuccess || msg instanceof EndGameMessage || msg instanceof GameStartMsg || msg instanceof JoinSuccessMsg || msg instanceof PlayCardSuccess) {
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
