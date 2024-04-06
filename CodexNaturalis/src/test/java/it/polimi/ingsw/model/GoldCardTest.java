package it.polimi.ingsw.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.HashBiMap.create;
import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {

    public final Map<CornerPosition,Elements> map = new HashMap<>();
    public final GoldCard goldCard78 = new GoldCard(78, false, map,Elements.INSECT, 1, 2, Elements.ANIMALS, TypeBonus.COUNTELEMENT_P);


    /*set a random initial Card to create a board*/
    public final InitialCard startCard = new InitialCard(82,false);
    /* if it is front
    Elements[] centralElements = {Elements.MUSHROOMS};
    InitialCard startCard = new InitialCard(82,true,centralElements);*/
    public final Board playerBoard = new Board(startCard);
    public CardParsing cp = new CardParsing();

    public ArrayList<GoldCard> goldCards = (ArrayList<GoldCard>) cp.loadGoldCards();

    GoldCardTest() throws IOException {
    }

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
        goldCardV1.setNreqElement(2);
        goldCardV1.setSecondElement(Elements.ANIMALS);
        goldCardV1.setCode(78);
        goldCardV1.setKingdom(Elements.INSECT);
        goldCardV1.setFront(false);
        goldCardV1.setBasePoint(1);

        assertEquals(goldCardV1.getType(), goldCard78.getType());
        assertEquals(goldCardV1.getNreqElement(), goldCard78.getNreqElement());
        assertEquals(goldCardV1.getKingdom(), goldCard78.getKingdom());
        assertEquals(goldCardV1.getSecondElement(), goldCard78.getSecondElement());
        assertEquals(goldCardV1.getCode(), goldCard78.getCode());
        assertEquals(goldCardV1.getBasePoint(), goldCard78.getBasePoint());

        GoldCard goldCardV2 = new GoldCard(78, false, map);
        goldCardV2.setType(TypeBonus.COUNTELEMENT_P);
        goldCardV2.setNreqElement(2);
        goldCardV2.setSecondElement(Elements.ANIMALS);
        goldCardV2.setCode(78);
        goldCardV2.setKingdom(Elements.INSECT);
        goldCardV2.setFront(false);
        goldCardV2.setBasePoint(1);

        assertEquals(goldCardV2.getType(), goldCard78.getType());
        assertEquals(goldCardV2.getNreqElement(), goldCard78.getNreqElement());
        assertEquals(goldCardV2.getKingdom(), goldCard78.getKingdom());
        assertEquals(goldCardV2.getSecondElement(), goldCard78.getSecondElement());
        assertEquals(goldCardV2.getCode(), goldCard78.getCode());
        assertEquals(goldCardV2.getBasePoint(), goldCard78.getBasePoint());
    }

    @Test
    public void checkGoals(){

        //check HideCorner Type with Card code 65
        GoldCard test65= goldCards.get(65-TypeOfCard.GOLDCARD.codeCardStart);






        //check DirectPoint Type with Card code 52
        GoldCard test52= goldCards.get(51-TypeOfCard.GOLDCARD.codeCardStart);
        //check CountElementF Type with Card code 70
        GoldCard test70= goldCards.get(69-TypeOfCard.GOLDCARD.codeCardStart);
        //check CountElementI Type with Card code 59
        GoldCard test59= goldCards.get(58-TypeOfCard.GOLDCARD.codeCardStart);
        //check CountElementP Type with Card code 48
        GoldCard test48= goldCards.get(47-TypeOfCard.GOLDCARD.codeCardStart);


        //check Hidecorner


        System.out.println(test65.getCode());

    }
    @Test
    public void testCardCoordinate(){
        BiMap<Card,Coordinate> cardCoordinateBiMap = HashBiMap.create();
        Coordinate xy = new Coordinate(1,1);
        GoldCard test52= goldCards.get(52-TypeOfCard.GOLDCARD.codeCardStart);
        cardCoordinateBiMap.put(test52,xy );
        GoldCard gold52= goldCards.get(52-TypeOfCard.GOLDCARD.codeCardStart);

       Coordinate testC = cardCoordinateBiMap.get(gold52);
        System.out.println(testC.getX());
        System.out.println(testC.getY());
        assertTrue(cardCoordinateBiMap.containsValue(new Coordinate(1,1)));

        Card cardInTest= cardCoordinateBiMap.inverse().get(xy);
        System.out.println(cardInTest.getCode());

    }

}