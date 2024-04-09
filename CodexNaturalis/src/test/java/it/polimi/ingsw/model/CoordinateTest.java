package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

    @Test
    void setXTest(){
        Coordinate c = new Coordinate();
        c.setX(1);
        c.setY(1);
        assertTrue(c.getX()==1 && c.getY()==1);
    }

    @Test
    void equalsTestNull(){
        Coordinate c = new Coordinate(1,1);
        assertFalse(c.equals(null));
    }

    @Test
    void equalsTestDifferentClass(){
        Coordinate c = new Coordinate(1,1);
        String s = "";
        assertFalse(c.equals(s));
    }

}