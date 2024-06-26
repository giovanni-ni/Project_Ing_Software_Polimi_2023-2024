package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Objects;
/**
 * Coordinate of a card in the board Map
 */
public class Coordinate implements Serializable {
    private int x;
    private int y;
    /**
     * Constructor of the coordinate of a card
     */
    public Coordinate() {
    }

    /**
     * Constructor of the coordinate of a card with data
     * @param x : x position of the card
     * @param y : y position of the card
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Getter of the x data of the coordinate
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter of the y data of the coordinate
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Setter of the y data of the coordinate
     * @param y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Setter of the x data of the coordinate
     * @param x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }


    /**
     * Override of the hash code, as all the Coordinate object are the same if they have same x and y
     */
    @Override
    public int hashCode(){
        return Objects.hash(x,y);
    }

    /**
     * Override of the equals method, as all the Coordinate object are the same if they have same x and y
     */
    @Override
    public boolean equals(Object obj){
        if (this==obj){
            return true;
        }
        if (obj==null || getClass()!=obj.getClass()){
            return false;
        }
        Coordinate c = (Coordinate) obj;
        return (x == c.getX() && y==c.getY());
    }

}
