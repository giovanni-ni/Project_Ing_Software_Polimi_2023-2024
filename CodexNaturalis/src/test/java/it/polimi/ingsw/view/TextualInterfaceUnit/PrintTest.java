package it.polimi.ingsw.view.TextualInterfaceUnit;

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
        for (TargetCard tc :targetCards){

                Print.printCard(tc);


        }
        for (InitialCard tc :initialCards){

            Print.printCard(tc);


        }
    }
}