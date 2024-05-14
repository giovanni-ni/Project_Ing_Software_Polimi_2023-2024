package it.polimi.ingsw.view.TextualInterfaceUnit;

import java.awt.*;

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

}
