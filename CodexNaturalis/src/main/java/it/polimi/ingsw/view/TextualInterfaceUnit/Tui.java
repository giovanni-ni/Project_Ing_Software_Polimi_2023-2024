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

public class Tui  implements Ui {

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

    private int autostart;

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
        //this.start();
        while(true) {
            redirect();
            System.out.flush();
        }


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
                print("Invalid input. Try again. (1 or 2)");
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


    public void askLogin() throws Exception {
        String user;
        do{

            print("Enter the username: ");
            user = in.nextLine();
            if (user.isEmpty()) {
                print("You didn't choose a username. Try again");

            } else {
                this.username = user;

                if(username.equals("nuge")) {
                    print("nu哥yyds");
                }
                myPlayer = new Player(username);

                return;
            }
        } while(user.isEmpty());

    }

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
            //case "help", "h", "he" -> askHelp();

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
        int index;
        do {
            index = Integer.parseInt(in.nextLine());
            if(index != 0 && index != 1 && index != 2) {
                print("error");
                print("choose the card that you want to play: ");
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
        String s;
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
            myBoard[x+10][y+10] = code;
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

        //wait(100);
        Thread.sleep(1000);
        //print(myMatch.idMatch);

    }

    private int askMaxSeats() throws Exception {
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


    public void askJoinMatch() throws Exception {
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
    }


    public boolean askLeaveMatch() throws RemoteException {
        return false;
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


    public void announceCurrentPlayer() throws RemoteException {

    }


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

    public void prepareGame() throws IOException, InterruptedException {
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
        for(Card c: myPlayer.getCardOnHand()) {
            printCardById(c.getCode());
        }
        print("choose your personal target card from: ");
        print(myPlayer.getTargetOnHand()[0].getIdCard() + " " + myPlayer.getTargetOnHand()[1].getIdCard());
        int choice;
        do {
            choice = Integer.parseInt(in.nextLine());
            if(choice != 0 && choice != 1) {
                System.out.println("error");
                print("choose your personal target card from: ");
            }
        } while(choice != 0 && choice != 1);

        SetTargetCardMessage msg = new SetTargetCardMessage(myMatch.idMatch, this.username, choice);
        client.messageToServer(msg);

        Thread.sleep(1000);

        print("this is your initial card:" + myPlayer.getInitialCard().getCode() + " front(0) or back(1) ");
        do {
            choice = Integer.parseInt(in.nextLine());
            if(choice != 0 && choice != 1) {
                print("error");
                print(" front(0) or back(1) ");
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
                print(" resource card: first(0) second(1) third(2) ");
                print(" gold card: first(3) second(4) third(5) ");
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
            print(p.getNickname() + ": " + p.currentScore + " points");
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
        } else if(msg instanceof NowIsYourRoundMsg) {
            Tui.printMessage(((NowIsYourRoundMsg) msg).getDescription());
        } else if(msg instanceof LastRoundMessage) {
            Tui.printMessage("ATTENTION !! it's the last round");
        } else if(msg instanceof endGameMessage) {
            Tui.status = PlayerStatus.END;
            Tui.myMatch = ((endGameMessage) msg).getModel();
            Tui.myPlayer = ((endGameMessage) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
        }

        /*if(msg instanceof ActionSuccessMsg /*|| msg instanceof drawCardSuccess || msg instanceof endGameMessage || msg instanceof gameStartMsg || msg instanceof joinSuccessMsg || msg instanceof playCardSuccess) {
            Tui.myMatch = ((ActionSuccessMsg) msg).getModel();

        }*/

    }
}
