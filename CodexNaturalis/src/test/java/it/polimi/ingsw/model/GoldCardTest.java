package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {
    public final GoldCard goldCard78 = new GoldCard(78, false, Elements.INSECT, 1, 2, Elements.ANIMALS, TypeBonus.COUNTELEMENT_P);


    /*set a random initial Card to create a board*/
    public final InitialCard startCard = new InitialCard(82,false);
    /* if it is front
    Elements[] centralElements = {Elements.MUSHROOMS};
    InitialCard startCard = new InitialCard(82,true,centralElements);*/
    public final Board playerBoard = new Board(startCard);
    @Test
    public void GoldCardRequirementFalse(){
        assertFalse(goldCard78.checkRequirements(playerBoard));

    }
    @Test
    public void GoldCardRequirementFalse2(){
        Map<Elements, Integer> falseCounter = new HashMap<>();
        falseCounter.put(Elements.INSECT,10);
        falseCounter.put(Elements.ANIMALS,0);
        playerBoard.setCounterOfElements(falseCounter);
        assertFalse(goldCard78.checkRequirements(playerBoard));

    }
    @Test
    public void GoldCardRequirementTrue(){
        Map<Elements, Integer> trueCounter = new HashMap<>();
        trueCounter.put(Elements.INSECT,2);
        trueCounter.put(Elements.ANIMALS,1);
        playerBoard.setCounterOfElements(trueCounter);
        assertTrue(goldCard78.checkRequirements(playerBoard));

    }
    @Test
    public void testOfDifferentConstructor() {
        GoldCard goldCardV1 = new GoldCard();
        goldCardV1.setType(TypeBonus.COUNTELEMENT_P);
        goldCardV1.setN_reqElement(2);
        goldCardV1.setSecondElement(Elements.ANIMALS);
        goldCardV1.setCode(78);
        goldCardV1.setKingdom(Elements.INSECT);
        goldCardV1.setFront(false);
        goldCardV1.setBasePoint(1);

        assertEquals(goldCardV1.getType(), goldCard78.getType());
        assertEquals(goldCardV1.getN_reqElement(), goldCard78.getN_reqElement());
        assertEquals(goldCardV1.getKingdom(), goldCard78.getKingdom());
        assertEquals(goldCardV1.getSecondElement(), goldCard78.getSecondElement());
        assertEquals(goldCardV1.getCode(), goldCard78.getCode());
        assertEquals(goldCardV1.getBasePoint(), goldCard78.getBasePoint());

        GoldCard goldCardV2 = new GoldCard(78, false);
        goldCardV2.setType(TypeBonus.COUNTELEMENT_P);
        goldCardV2.setN_reqElement(2);
        goldCardV2.setSecondElement(Elements.ANIMALS);
        goldCardV2.setCode(78);
        goldCardV2.setKingdom(Elements.INSECT);
        goldCardV2.setFront(false);
        goldCardV2.setBasePoint(1);

        assertEquals(goldCardV2.getType(), goldCard78.getType());
        assertEquals(goldCardV2.getN_reqElement(), goldCard78.getN_reqElement());
        assertEquals(goldCardV2.getKingdom(), goldCard78.getKingdom());
        assertEquals(goldCardV2.getSecondElement(), goldCard78.getSecondElement());
        assertEquals(goldCardV2.getCode(), goldCard78.getCode());
        assertEquals(goldCardV2.getBasePoint(), goldCard78.getBasePoint());
    }

}