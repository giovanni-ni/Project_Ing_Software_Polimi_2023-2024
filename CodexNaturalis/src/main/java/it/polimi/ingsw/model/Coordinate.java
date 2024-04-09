package it.polimi.ingsw.model;

import java.util.Objects;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate() {

    }
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int hashCode(){
        return Objects.hash(x,y);
    }

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
