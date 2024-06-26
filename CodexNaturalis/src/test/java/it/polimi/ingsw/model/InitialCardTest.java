package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static it.polimi.ingsw.model.CornerPosition.*;
import static it.polimi.ingsw.model.Elements.*;
import static org.junit.jupiter.api.Assertions.*;

class InitialCardTest {
    private CardParsing cp = new CardParsing();
    private ArrayList<InitialCard> initialCards = (ArrayList<InitialCard>) cp.loadInitialCards();
    private final InitialCard testCard0= initialCards.get(0);

    InitialCardTest() throws IOException {
    }
    @Test
    void getCornersFront() {
        Map<CornerPosition, Elements> testCornersFront0= testCard0.getCornersFront();
        assertEquals(testCornersFront0.get(UPLEFT),EMPTY);
        assertEquals(testCornersFront0.get(UPRIGHT),VEGETAL);
        assertEquals(testCornersFront0.get(DOWNRIGHT),EMPTY);
        assertEquals(testCornersFront0.get(DOWNLEFT),INSECT);
    }

    @Test
    void getCornersBack() {
        Map<CornerPosition, Elements> testCornerBack0= testCard0.getCornersBack();
        assertEquals(testCornerBack0.get(UPLEFT),MUSHROOMS);
        assertEquals(testCornerBack0.get(UPRIGHT),VEGETAL);
        assertEquals(testCornerBack0.get(DOWNRIGHT),ANIMALS);
        assertEquals(testCornerBack0.get(DOWNLEFT),INSECT);

    }


    @Test
    void setSide() {
    }

    @Test
    void getCentralElements() {
        ArrayList <Elements> testCentralELements=testCard0.getCentralElements();
        assertTrue(testCentralELements.contains(INSECT));
    }

    @Test
    void InitialCard(){
        InitialCard testCard1 = new InitialCard(testCard0.getCode(), testCard0.getIsFront(),testCard0.getCorners(),testCard0.getCentralElements(),testCard0.getCornersFront(),testCard0.getCornersBack());
        assertEquals(testCard1.getCode(), testCard0.getCode());
        assertEquals(testCard1.getIsFront(), testCard0.getIsFront());
        assertEquals(testCard1.getCorners(), testCard0.getCorners());

        InitialCard testCard2 = new InitialCard(testCard0.getCode(), testCard0.getIsFront());

        InitialCard testCar3= new InitialCard(testCard0.getCode(), testCard0.getIsFront(),testCard0.getCorners(),testCard0.getCentralElements());
    }

    @Test
    void SetSide(){
        testCard0.setFront(true);
        testCard0.setSide();
    }


}