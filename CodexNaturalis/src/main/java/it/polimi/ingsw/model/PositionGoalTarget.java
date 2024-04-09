package it.polimi.ingsw.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class PositionGoalTarget extends TargetCard{

    Elements firstElement;

    Elements secondElement;

    CornerPosition cp;

    public PositionGoalTarget() {

    }

    public PositionGoalTarget(int idCard,int basePoint,boolean ifCommon,Elements firstElement,Elements secondElement, CornerPosition cp) {
        super(idCard,basePoint,ifCommon);
        this.firstElement = firstElement;
        this.secondElement = secondElement;
        this.cp = cp;
    }

    public Elements getFirstElement() {
        return firstElement;
    }

    public void setFirstElement(Elements firstElement) {
        this.firstElement = firstElement;
    }

    public CornerPosition getCp() {
        return cp;
    }

    public void setCp(CornerPosition cp) {
        this.cp = cp;
    }

    public Elements getSecondElement() {
        return secondElement;
    }

    public void setSecondElement(Elements secondElement) {
        this.secondElement = secondElement;
    }
    @Override
    public int checkGoal(Board board) {
        BiMap<Card,Coordinate> copyFirst = HashBiMap.create();
        for (Card c : board.getCardCoordinate().keySet()){
            if (c.getKingdom()==firstElement){
                copyFirst.put(c,board.getCardCoordinate().get(c));
            }
        }


        BiMap<Card,Coordinate> copySecond= HashBiMap.create();
        for (Card c : board.getCardCoordinate().keySet()){
            if (c.getKingdom()==secondElement){
                copySecond.put(c,board.getCardCoordinate().get(c));
            }
        }
        BiMap<Card,Coordinate> copy1 = HashBiMap.create();
        copy1.putAll(copySecond);

        Coordinate xy1=new Coordinate(0,0);
        Coordinate xy2=new Coordinate(0,0);
        int n = 0;
        for (Card c :copy1.keySet()){
            if (copySecond.containsKey(c)) {
                int cardX = copySecond.get(c).getX();
                int cardY = copySecond.get(c).getY();
                switch (cp) {
                    case UPLEFT -> {
                        xy1 = new Coordinate(cardX - 1, cardY + 1);
                        xy2 = new Coordinate(cardX - 1, cardY + 3);

                    }
                    case UPRIGHT -> {
                        xy1 = new Coordinate(cardX + 1, cardY - 1);
                        xy2 = new Coordinate(cardX + 1, cardY - 3);


                    }
                    case DOWNLEFT -> {
                        xy1 = new Coordinate(cardX - 1, cardY - 1);
                        xy2 = new Coordinate(cardX - 1, cardY - 3);

                    }
                    case DOWNRIGHT -> {
                        xy1 = new Coordinate(cardX + 1, cardY + 1);
                        xy2 = new Coordinate(cardX + 1, cardY + 3);

                    }
                }
                if (copyFirst.containsValue(xy1) && copyFirst.containsValue(xy2)) {
                    n++;
                    copyFirst.inverse().remove(xy1);
                    copyFirst.inverse().remove(xy2);
                    copySecond.inverse().remove((new Coordinate(cardX, cardY)));
                }
            }


        }

        return n;
    }

    @Override
    public int countPoint(Board board) {
        return super.getbasePoint() * checkGoal(board);
    }





    /* Error for nullpointer, wrong coordinates(x-1,y-2 not exist), not matching with json
    public int checkGoal(Board board) {

        BiMap<Card,Coordinate> copy = HashBiMap.create();
        copy.putAll(board.getCardCoordinate());
        int n = 0;

        for(Card c: board.getCardCoordinate().keySet()) {
            if(c.getKingdom().equals(this.firstElement))  {
                if(isCorrect(c, board)) {
                    n++;
                    copy.remove(c);
                }
            }
        }

        return n;
    }*/
/*
    private boolean isCorrect(Card c, Board board) {
        if(this.cp == CornerPosition.UPLEFT) {
            if(board.getCardInBoard(board.getCoordinate(c).getX()-1, board.getCoordinate(c).getY()-1).getKingdom()==this.secondElement) {
                if((board.getCardInBoard(board.getCoordinate(c).getX()-1, board.getCoordinate(c).getY()-2).getKingdom()==this.secondElement)) {
                    return true;
                }
            }
        } else if(this.cp == CornerPosition.UPRIGHT) {
            if(board.getCardInBoard(board.getCoordinate(c).getX()+1, board.getCoordinate(c).getY()-1).getKingdom()==this.secondElement) {
                if((board.getCardInBoard(board.getCoordinate(c).getX()+1, board.getCoordinate(c).getY()-2).getKingdom()==this.secondElement)) {
                    return true;
                }
            }
        } else if(this.cp == CornerPosition.DOWNLEFT) {
            if(board.getCardInBoard(board.getCoordinate(c).getX()-1, board.getCoordinate(c).getY()+1).getKingdom()==this.secondElement) {
                if((board.getCardInBoard(board.getCoordinate(c).getX()-1, board.getCoordinate(c).getY()+2).getKingdom()==this.secondElement)) {
                    return true;
                }
            }
        } else if(this.cp == CornerPosition.DOWNRIGHT) {
            if(board.getCardInBoard(board.getCoordinate(c).getX()+1, board.getCoordinate(c).getY()+1).getKingdom()==this.secondElement) {
                if((board.getCardInBoard(board.getCoordinate(c).getX()+1, board.getCoordinate(c).getY()+2).getKingdom()==this.secondElement)) {
                    return true;
                }
            }
        }
        return false;
    }
    */
}
