package it.polimi.ingsw.view.TextualInterfaceUnit;

import it.polimi.ingsw.Message.ServerToClientMsg.GenericServerMessage;
import it.polimi.ingsw.Message.ServerToClientMsg.ServerChatMessage;
import it.polimi.ingsw.model.*;

import java.io.IOException;
import java.util.Map;



public class Print {

    public static final String Codex =
            " ______   ______    _______   _______ ___   ___    .__   __.      ___      .___________. __    __  .______           ___       __       __       _______.\n" +
            " /      | /  __  \\  |       \\ |   ____|\\  \\ /  /    |  \\ |  |     /   \\     |           ||  |  |  | |   _  \\         /   \\     |  |     |  |     /       |\n" +
            "|  ,----'|  |  |  | |  .--.  ||  |__    \\  V  /     |   \\|  |    /  ^  \\    `---|  |----`|  |  |  | |  |_)  |       /  ^  \\    |  |     |  |    |   (----`\n" +
            "|  |     |  |  |  | |  |  |  ||   __|    >   <      |  . `  |   /  /_\\  \\       |  |     |  |  |  | |      /       /  /_\\  \\   |  |     |  |     \\   \\    \n" +
            "|  `----.|  `--'  | |  '--'  ||  |____  /  .  \\     |  |\\   |  /  _____  \\      |  |     |  `--'  | |  |\\  \\----. /  _____  \\  |  `----.|  | .----)   |   \n" +
            " \\______| \\______/  |_______/ |_______|/__/ \\__\\    |__| \\__| /__/     \\__\\     |__|      \\______/  | _| `._____|/__/     \\__\\ |_______||__| |_______/    \n" +
            "                                                                                                                                                          ";


    public static final String menuOption = """
            -----------------------------------------------------------
            [Commands] Menu Option:
              create   --> Create a new match.
               join    --> Join a match.
               play    --> Join the first match available
               exit    --> Exit game.
         
            """;
    public static void print(Object message){
        System.out.println(message);
        System.out.flush();
    }


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_MUSHROOM = "\u001B[31m";
    public static final String ANSI_LEAF = "\u001B[32m";
    public static final String ANSI_GOLD = "\u001B[33m";
    public static final String ANSI_ANIMAL = "\u001B[34m";
    public static final String ANSI_INSECT = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    public static void printCardById(int cardId) throws IOException {
        Object card = findCardById(cardId);
        if (card!=null){
            printCard(card);
        }else {
            print("Card number not exist");
        }
    }

    private static Object findCardById(int cardId) throws IOException {
        Object card = null;
        CardParsing cp = new CardParsing();
        if (cardId>=TypeOfCard.RESOURCECARD.getCodeCardStart()){
            if (cardId<=TypeOfCard.RESOURCECARD.getCodeCardEnd()){
                card=cp.loadResourceCards().get(cardId-1);
            }else if(cardId<= TypeOfCard.GOLDCARD.getCodeCardEnd()){
                card=cp.loadGoldCards().get(cardId-1);
            } else if (cardId <= TypeOfCard.INITIALCARD.getCodeCardEnd()) {
                card=cp.loadInitialCards().get(cardId-1);
            } else if(cardId <= TypeOfCard.TARGETCARD.getCodeCardStart()){
                card=cp.loadTargetCards().get(cardId-1);
            }
        }
        return card;
    }


    public static void printCard(Object card){
        switch (card){
            case GoldCard GoldCard -> printGoldCard((it.polimi.ingsw.model.GoldCard) card);
            case ResourceCard ResourceCard-> printResourceCard((it.polimi.ingsw.model.ResourceCard) card);
            case InitialCard InitialCard -> printInitialCard((it.polimi.ingsw.model.InitialCard) card);
            case TargetCard targetCard -> printTargetCard((TargetCard) card);

            default -> throw new IllegalStateException("Unexpected value: " + card);
        }
        print("");

    }

    private static void printTargetCard(TargetCard card) {
        if (card instanceof CountTargetCard) {

            String color;

            print("Target: Collect \n" +
                    "Base Point: " + card.getbasePoint());

            for (Elements elements : ((CountTargetCard) card).getElemRequired()) {
                color = getColorSecond(elements);
                print(color + "|" + elements.toString() + ANSI_RESET);
            }
        } else if (card instanceof ObliqueTargetCard) {
            Elements elements=((ObliqueTargetCard) card).getElemRequired();
            String color=getColorSecond(elements);
            print("Target : Card Position");
            print("Base Point: "+card.getbasePoint());
            if (((ObliqueTargetCard) card).getcornerPosition()== CornerPosition.UPLEFT){
                print(color+
                        "|#| | |\n" +
                        "| |#| |\n" +
                        "| | |#|"+ANSI_RESET);
            }else {
                print(color+
                        "| | |#|\n" +
                        "| |#| |\n" +
                        "|#| | |"+ANSI_RESET);
            }
        }else if (card instanceof PositionGoalTarget) {
            Elements elements = ((PositionGoalTarget) card).getFirstElement();

            String colorFirst = getColorSecond(elements);

            elements = ((PositionGoalTarget) card).getSecondElement();
            String colorSecond = getColorSecond(elements);
            print("Target : Card Position");
            print("Base Point: "+card.getbasePoint());
            if (((PositionGoalTarget) card).getCp() == CornerPosition.UPLEFT) {
                print(colorFirst +
                                        "| |#| |\n" +
                                        "| |#| |\n" +
                        colorSecond +   "| | |#|" + ANSI_RESET);
            } else if (((PositionGoalTarget) card).getCp() == CornerPosition.DOWNRIGHT) {
                print(colorSecond +
                                     "|#| | |\n" +
                        colorFirst + "| |#| |\n" +
                                     "| |#| |" + ANSI_RESET);
            } else if (((PositionGoalTarget) card).getCp() == CornerPosition.DOWNLEFT) {
                print(colorSecond +
                                     "| | |#|\n" +
                        colorFirst + "| |#| |\n" +
                                     "| |#| |" + ANSI_RESET);
            }else {
                print(colorFirst +
                                        "| |#| |\n" +
                                        "| |#| |\n" +
                        colorSecond +   "|#| | |" + ANSI_RESET);
            }

        }


    }

    private static String getColorSecond(Elements elements) {
        String colorSecond;
        switch (elements) {
            case MUSHROOMS -> colorSecond = ANSI_MUSHROOM;
            case VEGETAL -> colorSecond = ANSI_LEAF;
            case INSECT -> colorSecond = ANSI_INSECT;
            case ANIMALS -> colorSecond = ANSI_ANIMAL;
            case EMPTY -> colorSecond = ANSI_BLACK;
            case HIDE -> colorSecond = ANSI_WHITE;
            default -> colorSecond = ANSI_GOLD;
        }
        return colorSecond;
    }


    private static void printInitialCard(InitialCard card) {
        print("The initial card "+card.getCode()+" has");
        print("--------------------");
            print("[Front]");
            printCorners(card.getCornersFront());
            print("Central Element/s: ");
            for (Elements e :card.getCentralElements()){
                print("|"+getColorSecond(e)+e.toString()+ANSI_RESET);
            }
        print("--------------------");
            print("[Back]");
            printCorners(card.getCornersBack());


    }

    private static void printResourceCard(ResourceCard card) {
        print(getColorSecond(card.getKingdom())+"The Resource card "+card.getCode()+" has"+ANSI_RESET);
        print("--------------------");
        print("[Front]");
        print("Base Point: "+card.getBasePoint());
        printCorners(card.getCorners());
        print("--------------------");
        print("[Back]");
        printCornersEmpty();
        print("Central Element: "+getColorSecond(card.getKingdom())+card.getKingdom().toString()+ANSI_RESET);
        print("--------------------");
        String isFront;
        if (card.getIsFront()){
            isFront="[Front]";
        }else {
            isFront="[Back]";
        }
        print("The current face is set "+isFront);


    }

    private static void printGoldCard(GoldCard card) {
        print(getColorSecond(card.getKingdom())+ANSI_GOLD+"The Gold card "+getColorSecond(card.getKingdom())+card.getCode()+ANSI_RESET+" has");
        print("--------------------");
        print("[Front]");
        printGoldCardPointType(card);
        printGoldCardRequirement(card);
        printCorners(card.getCorners());
        print("--------------------");
        print("[Back]");
        printCornersEmpty();
        print("Central Element: "+getColorSecond(card.getKingdom())+card.getKingdom().toString()+ANSI_RESET);
        print("--------------------");
        String isFront;
        if (card.getIsFront()){
            isFront="[Front]";
        }else {
            isFront="[Back]";
        }
        print("The current face is set "+isFront);

    }

    private static void printGoldCardPointType(GoldCard card) {
        TypeBonus type =card.getType();
        switch (type) {
            case HIDECORNER -> {
                print("For each corner that this card hides you'll get "+ANSI_GOLD+card.getBasePoint()+" Points"+ANSI_RESET);
            }
            case DIRECTPOINT -> {
                print("If you place this card you'll get "+ANSI_GOLD+card.getBasePoint()+" Points"+ANSI_RESET);
            }
            default -> {
                String item;
                switch (type){
                    case COUNTELEMENT_P -> item = Elements.PARCHMENT.toString();
                    case COUNTELEMENT_F -> item = Elements.FEATHER.toString();
                    case COUNTELEMENT_I -> item = Elements.INK.toString();
                }

                print("For each "+ANSI_GOLD+"item"+ANSI_RESET +" you'll get "+ANSI_GOLD+card.getBasePoint()+" Points"+ANSI_RESET);
            }
        }

    }

    private static void printGoldCardRequirement(GoldCard card) {
        String mainElement;
        String secondElement ="";
        if (card.getSecondElement()!=Elements.EMPTY){
            secondElement = " + "+getColorSecond(card.getSecondElement())+card.getSecondElement().toString()+"x1"+ANSI_RESET;
        }
        print("Requirement :"+getColorSecond(card.getKingdom())+card.getKingdom().toString()+"x"+card.getNreqElement()+ANSI_RESET+secondElement);

    }

    private static void printCorners(Map<CornerPosition, Elements> map){

        print(  "["+CornerPosition.UPLEFT.toString()+":"+getColorSecond(map.get(CornerPosition.UPLEFT))+map.get(CornerPosition.UPLEFT).toString()+ANSI_RESET+"]"+
                "["+CornerPosition.UPRIGHT.toString()+":"+getColorSecond(map.get(CornerPosition.UPRIGHT))+map.get(CornerPosition.UPRIGHT).toString()+ANSI_RESET+"]");
        print(  "["+CornerPosition.DOWNLEFT.toString()+":"+getColorSecond(map.get(CornerPosition.DOWNLEFT))+map.get(CornerPosition.DOWNLEFT).toString()+ANSI_RESET+"]"+
                "["+CornerPosition.DOWNRIGHT.toString()+":"+getColorSecond(map.get(CornerPosition.DOWNRIGHT))+map.get(CornerPosition.DOWNRIGHT).toString()+ANSI_RESET+"]");
    }
    private static void printCornersEmpty(){

        print( "["+CornerPosition.UPLEFT.toString()+":"+getColorSecond(Elements.EMPTY)+"EMPTY"+ANSI_RESET+"]"+
                "["+CornerPosition.UPRIGHT.toString()+":"+getColorSecond(Elements.EMPTY)+"EMPTY"+ANSI_RESET+"]");
        print(  "["+CornerPosition.DOWNLEFT.toString()+":"+getColorSecond(Elements.EMPTY)+"EMPTY"+ANSI_RESET+"]"+
                "["+CornerPosition.DOWNRIGHT.toString()+":"+getColorSecond(Elements.EMPTY)+"EMPTY"+ANSI_RESET+"]");
    }
    public static void showNewChatMessage(GenericServerMessage msg) {
        ServerChatMessage chatMessage = (ServerChatMessage) msg;
        String channel;
        if (chatMessage.isForAll()){
            channel =ANSI_GOLD+"[Public]";
        }else {
            channel =ANSI_CYAN+"[Private]";
        }
        print(channel+"["+chatMessage.getFromPlayer()+"]:"+ANSI_RESET+chatMessage.getChatMsg());

    }

    public static final String menuOperations = """
            -----------------------------------------------------------
            [Commands] Menu Operations:
              p   --> play a card
              c   --> chat
              sc  --> show common target card
              sp  --> show personal target card
              s   --> show a card
              
         
            """;
}
