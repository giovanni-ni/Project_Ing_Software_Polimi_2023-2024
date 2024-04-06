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

    /*set a random initial Card to create a board*/
    public Elements[] centralElments;



    public CardParsing cp = new CardParsing();

    public ArrayList<InitialCard> initialCards = (ArrayList<InitialCard>) cp.loadInitialCards();
    public ArrayList<GoldCard> goldCards = (ArrayList<GoldCard>) cp.loadGoldCards();
    public final InitialCard startCard = initialCards.get(81-TypeOfCard.INITIALCARD.codeCardStart);
    public final Board playerBoard = new Board(startCard);
    public GoldCard goldCard78 = goldCards.get(78-TypeOfCard.GOLDCARD.codeCardStart);
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
    public void checkEveryKindOfGoals(){

        //check HideCorner Type with Card code 65
        GoldCard test65= goldCards.get(65-TypeOfCard.GOLDCARD.codeCardStart);
        //check DirectPoint Type with Card code 52
        GoldCard test52= goldCards.get(52-TypeOfCard.GOLDCARD.codeCardStart);
        //check CountElementF Type with Card code 70
        GoldCard test70= goldCards.get(70-TypeOfCard.GOLDCARD.codeCardStart);
        //check CountElementI Type with Card code 59
        GoldCard test59= goldCards.get(59-TypeOfCard.GOLDCARD.codeCardStart);
        //check CountElementP Type with Card code 48
        GoldCard test48= goldCards.get(48-TypeOfCard.GOLDCARD.codeCardStart);

        /* test if gold card has the right corners
        for (CornerPosition cp :CornerPosition.values()){
            Elements e= test52.getCorners().get(cp);
            System.out.println(e);
        }*/
        InitialCard card = new InitialCard();
        card=startCard;
        ArrayList<Elements> elements = new ArrayList<>();
        elements.add(Elements.INSECT);
        startCard.setCentralElements(elements);
        for (CornerPosition cp : CornerPosition.values()){

            map.put(cp,Elements.EMPTY);
        }
        startCard.setCorners(map);
        Board playerBoard = new Board(startCard);
        //check Hidecorner

        playerBoard.addCard(test65,1,1);
        playerBoard.addCard(test52,2,2);
        playerBoard.addCard(test59,0,2);
        playerBoard.addCard(test70,2,0);
        int time = test65.goalCount(playerBoard);
        int point = test65.getGoalPoint(playerBoard);
        assertEquals(time,4);
        assertEquals(point,8);
        //check DirectPoint
        time = test52.goalCount(playerBoard);
        point = test52.getGoalPoint(playerBoard);
        assertEquals(time,1);
        assertEquals(point,test52.getBasePoint());
        //check CountElements
        //feather
        time = test70.goalCount(playerBoard);
        point = test70.goalCount(playerBoard);
        assertEquals(time,0);
        assertEquals(point,0);
        playerBoard.addElement(Elements.FEATHER);
        time = test70.goalCount(playerBoard);
        point = test70.goalCount(playerBoard);
        assertEquals(time, 1);
        assertEquals(point,1);
        //Ink
        time = test59.goalCount(playerBoard);
        point = test59.goalCount(playerBoard);
        assertEquals(time,0);
        assertEquals(point,0);
        playerBoard.addElement(Elements.INK,3);
        time = test59.goalCount(playerBoard);
        point = test59.goalCount(playerBoard);
        assertEquals(time, 3);
        assertEquals(point,3);
        //Parchment
        time = test48.goalCount(playerBoard);
        point = test48.goalCount(playerBoard);
        assertEquals(time,0);
        assertEquals(point,0);
        playerBoard.addElement(Elements.PARCHMENT,18);
        time = test48.goalCount(playerBoard);
        point = test48.goalCount(playerBoard);
        assertEquals(time, 18);
        assertEquals(point,18);


    }
    @Test
    public void testCardCoordinate(){
        BiMap<Card,Coordinate> cardCoordinateBiMap = HashBiMap.create();
        Coordinate xy = new Coordinate(1,1);
        GoldCard test52= goldCards.get(52-TypeOfCard.GOLDCARD.codeCardStart);
        cardCoordinateBiMap.put(test52,xy );
        GoldCard gold52= goldCards.get(52-TypeOfCard.GOLDCARD.codeCardStart);

       Coordinate testC = cardCoordinateBiMap.get(gold52);
        assertEquals(testC,xy);
        assertTrue(cardCoordinateBiMap.containsValue(new Coordinate(1,1)));

        Card cardInTest= cardCoordinateBiMap.inverse().get(xy);
        assertEquals(test52,cardInTest);

    }

}