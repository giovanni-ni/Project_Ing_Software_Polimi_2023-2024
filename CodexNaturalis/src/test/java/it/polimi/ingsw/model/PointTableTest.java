package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PointTableTest {




    @Test
    void getAndSetPlayerPoints() {
        PointTable table=new PointTable();
        Player a=new Player(5);
        Player b=new Player(12);
        Player c=new Player(7);
        Player d=new Player(22);
        Map<Player,Integer > map=new HashMap<>();
        map.put(a,a.currentScore);
        map.put(b,b.currentScore);
        map.put(c,c.currentScore);
        map.put(d,d.currentScore);
        table.setPlayerPoints(map);
        assertEquals(map,table.getPlayerPoints());

    }

    @Test
    void getAndSetTargetPoints() {

        PointTable table=new PointTable();
        TargetCard targetCard0=new CountTargetCard(1,2,false,null);
        TargetCard targetCard1=new CountTargetCard(3,4,false,null);

        Player a=new Player(targetCard0);
        Player b=new Player(targetCard1);

        Map<Player,Integer > map=new HashMap<>();

        map.put(a,a.getTarget().getbasePoint());
        map.put(b,b.getTarget().getbasePoint());
        table.setTargetPoints(map);
        assertEquals(map,table.getTargetPoints());
    }

    @Test
    void updatePoint() {

    }

    @Test
    void resetPoint() {
    }

    @Test
    void findMaxPoint() {
    }

    @Test
    void findMaxTargetPlayers() {

    }

    @Test
    void findMaxPointPlayers() {
        PointTable table=new PointTable();
        Player a=new Player(5);
        Player b=new Player(22);
        Player c=new Player(7);
        Player d=new Player(22);
        Map<Player,Integer > map = new HashMap<>();
        map.put(a,a.currentScore);
        map.put(b,b.currentScore);
        map.put(c,c.currentScore);
        map.put(d,d.currentScore);
        table.setPlayerPoints(map);

        List<Player> max = new ArrayList<>();
        max.add(d);
        max.add(b);

        boolean isEqual = table.findMaxPointPlayers().containsAll(max) && max.containsAll(table.findMaxPointPlayers());
        assertTrue(isEqual);
    }

    @Test
    void countTarget() {
    }

    @Test
    void updateCountTarget() {
    }
}