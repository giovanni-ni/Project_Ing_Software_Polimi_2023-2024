package it.polimi.ingsw.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void getPlayerID() {
        Player p = new Player(PlayerColor.GREEN);
        assertEquals(PlayerColor.GREEN, p.getPlayerID());
    }
    @Test
    void setPlayerID() {
        Player p = new Player(PlayerColor.GREEN);
        p.setPlayerID(PlayerColor.RED);
        assertEquals(PlayerColor.RED, p.getPlayerID());
    }

    @Test
    void getAndSetTarget(){
        Elements[] elemRequired= {Elements.MUSHROOMS,Elements.MUSHROOMS, Elements.MUSHROOMS};
        TargetCard t1 = new CountTargetCard(95,2,false,elemRequired) ;
        TargetCard t2 = new PositionGoalTarget(99,3,false,Elements.MUSHROOMS,Elements.VEGETAL,CornerPosition.UPLEFT);
        TargetCard[] targetOnhand = {t1,t2};
        Player p =new Player(targetOnhand,PlayerColor.BLUE,0);
        p.setTarget(t2);
        assertEquals(t2,p.getTarget());
    }
    @Test
    void getAndSetCardOnHand(){
        List<Card> card= new ArrayList<>();
        Map<CornerPosition,Elements> map1=new HashMap<>();
        map1.put(CornerPosition.UPLEFT,Elements.EMPTY);
        map1.put(CornerPosition.UPRIGHT,Elements.EMPTY);
        map1.put(CornerPosition.DOWNLEFT,Elements.HIDE);
        map1.put(CornerPosition.DOWNRIGHT,Elements.INSECT);
        Card c1=new ResourceCard(5,false,map1,Elements.INSECT,1);
        Map<CornerPosition,Elements> map2=new HashMap<>();
        map2.put(CornerPosition.UPLEFT,Elements.MUSHROOMS);
        map2.put(CornerPosition.UPRIGHT,Elements.EMPTY);
        map2.put(CornerPosition.DOWNLEFT,Elements.MUSHROOMS);
        map2.put(CornerPosition.DOWNRIGHT,Elements.HIDE);
        Card c2=new ResourceCard(1,false,map2,Elements.MUSHROOMS,0);
        card.add(c1);
        card.add(c2);
        Elements[] elemRequired= {Elements.MUSHROOMS,Elements.MUSHROOMS, Elements.MUSHROOMS};
        TargetCard t1 = new CountTargetCard(95,2,false,elemRequired) ;
        TargetCard t2 = new PositionGoalTarget(99,3,false,Elements.MUSHROOMS,Elements.VEGETAL,CornerPosition.UPLEFT);
        TargetCard[] targetOnhand = {t1,t2};
        Player p =new Player(targetOnhand,PlayerColor.BLUE,0);
        p.setCardOnHand(card);
        assertEquals(card,p.getCardOnHand());
    }

    @Test
    void getAndSetBoardTest(){
        Player p = new Player(PlayerColor.RED);
        Board b = new Board();
        p.setBoard(b);
        assertEquals(b,p.getBoard());
    }

    @Test
    void getAndSetTargetOnHand(){
        Player p = new Player(PlayerColor.RED);
        TargetCard c1 = new CountTargetCard(1,1,false, new Elements[]{Elements.MUSHROOMS, Elements.INSECT});
        TargetCard c2 = new CountTargetCard(3,3,false, new Elements[]{Elements.VEGETAL, Elements.INSECT});
        TargetCard[] t = {c1,c2};
        p.setTargetOnHand(t);
        assertEquals(t, p.getTargetOnHand());
    }

    @Test
    void addTestCheckFalse() {
        Player p = new Player(PlayerColor.RED);

        Map<CornerPosition, Elements> corners = new HashMap<>();
        corners.put(CornerPosition.UPLEFT, Elements.EMPTY);
        corners.put(CornerPosition.UPRIGHT, Elements.VEGETAL);
        corners.put(CornerPosition.DOWNRIGHT, Elements.EMPTY);
        corners.put(CornerPosition.DOWNLEFT, Elements.EMPTY);
        ArrayList<Elements> array = new ArrayList<>();
        array.add(Elements.INSECT);
        InitialCard c = new InitialCard(81,true, corners,array);
        Board b = new Board(c);
        p.setBoard(b);
        ResourceCard rc = new ResourceCard();
        BiMap<Card, Coordinate> biMap = HashBiMap.create();
        assertFalse(p.add(rc, 2,2));

    }



}