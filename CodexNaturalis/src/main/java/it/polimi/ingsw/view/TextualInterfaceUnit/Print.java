package it.polimi.ingsw.view.TextualInterfaceUnit;

import it.polimi.ingsw.model.*;

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

            card = (CountTargetCard) card;
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
            if (((ObliqueTargetCard) card).getcornerPosition()==CornerPosition.UPLEFT){
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
            case EMPTY -> colorSecond = ANSI_WHITE;
            default -> colorSecond = ANSI_GOLD;
        }
        return colorSecond;
    }


    private static void printInitialCard(InitialCard card) {
        print("The initial card has");
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
    }

    private static void printGoldCard(GoldCard goldCard) {

    }
    private static void printCorners(Map<CornerPosition, Elements> map){
        for (CornerPosition cp:map.keySet()){
            print("["+cp.toString()+":"+getColorSecond(map.get(cp))+map.get(cp).toString()+ANSI_RESET+"]");
        }
    }

}
