package it.polimi.ingsw.view.TextualInterfaceUnit;

import it.polimi.ingsw.Message.ClientToServerMsg.ClientChatMessage;
import it.polimi.ingsw.Message.ServerToClientMsg.ServerChatMessage;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PrintTest {

    public CardParsing cp = new CardParsing();
    public ArrayList<InitialCard> initialCards = (ArrayList<InitialCard>) cp.loadInitialCards();
    public ArrayList<GoldCard> goldCards = (ArrayList<GoldCard>) cp.loadGoldCards();

    public ArrayList<ResourceCard> resourceCards = (ArrayList<ResourceCard>) cp.loadResourceCards();
    public ArrayList<TargetCard> targetCards= (ArrayList<TargetCard>) cp.loadTargetCards();

    PrintTest() throws IOException {
    }

    @Test
    public void PrintTest() throws IOException {
        ClientChatMessage msg= new ClientChatMessage(23,"Mako", false,"Linda","Buon Giorno");
        ServerChatMessage serverChatMessage = new ServerChatMessage(msg);
        Print.showNewChatMessage(serverChatMessage);

        ClientChatMessage msg2= new ClientChatMessage(23,"Mako", true,"Linda","Buona Sera");
        ServerChatMessage serverChatMessage2 = new ServerChatMessage(msg2);
        Print.showNewChatMessage(serverChatMessage2);
        for (GoldCard gc :goldCards){
            Print.printCard(gc);
        }
        for (TargetCard tc :targetCards){

            Print.printCard(tc);


        }
        for (InitialCard tc :initialCards){

            Print.printCard(tc);


        }
        for (ResourceCard rc: resourceCards){
            Print.printCard(rc);
        }
    }
}