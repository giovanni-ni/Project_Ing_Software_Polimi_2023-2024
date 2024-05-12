package it.polimi.ingsw.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.Serializable;
import java.util.Objects;

public class ObliqueTargetCard extends TargetCard implements Serializable {

    private Elements elemRequired;

    private CornerPosition cornerPosition;

    public ObliqueTargetCard() {

    }
    public ObliqueTargetCard(int idCard,int basePoint,boolean ifCommon,Elements elemRequired, CornerPosition cornerPosition) {
        super(idCard,basePoint,ifCommon);
        this.elemRequired = elemRequired;
        this.cornerPosition = cornerPosition;
    }

    public Elements getElemRequired() {
        return elemRequired;
    }

    public void setElemRequired(Elements elemRequired) {
        this.elemRequired = elemRequired;
    }

    public CornerPosition getcornerPosition() {
        return cornerPosition;
    }

    public void setcornerPosition(CornerPosition cornerPosition) {
        this.cornerPosition = cornerPosition;
    }

    @Override
    public int countPoint(Board board) {
        return super.getbasePoint() * checkGoal(board);
    }
    @Override
    public int checkGoal(Board board){
        BiMap<Card,Coordinate> copy = HashBiMap.create();
        for (Card c : board.getCardCoordinate().keySet()){
            if (c.getKingdom()==elemRequired){
                copy.put(c,board.getCardCoordinate().get(c));
            }
        }
        BiMap<Card,Coordinate> copy1 = HashBiMap.create();
        copy1.putAll(copy);
        Coordinate xy1=new Coordinate(0,0);
        Coordinate xy2=new Coordinate(0,0);
        int n = 0;
        for (Card c :copy1.keySet()){
            if (copy.containsKey(c)) {
                int cardX = copy.get(c).getX();
                int cardY = copy.get(c).getY();
                if (cornerPosition == CornerPosition.UPLEFT) {
                    xy1 = new Coordinate(cardX + 1, cardY - 1);
                    xy2 = new Coordinate(cardX - 1, cardY + 1);
                } else if (cornerPosition == CornerPosition.UPRIGHT) {
                    xy1 = new Coordinate(cardX + 1, cardY + 1);
                    xy2 = new Coordinate(cardX - 1, cardY - 1);
                }
                if (copy.containsValue(xy1) && copy.containsValue(xy2)) {
                    n++;
                    copy.inverse().remove(xy1);
                    copy.inverse().remove(xy2);
                    copy.inverse().remove((new Coordinate(cardX, cardY)));
                }
            }
        }

        return n;
    }





    //error code fixed but algorithm wrong
    /*public int checkGoal2(Board board) {
        BiMap<Card,Coordinate> copy = HashBiMap.create();
        copy.putAll(board.getCardCoordinate());
        int n = 0;

        for(Card c: board.getCardCoordinate().keySet()) {
            if(!(c instanceof InitialCard) && c.getKingdom().equals(this.elemRequired))  {
                if(isCorrect(c, board)) {
                    n++;
                    copy.remove(c);
                }
            }
        }
        return n;
    }


    private boolean isCorrect(Card c, Board board) {
        if (this.cornerPosition == CornerPosition.UPRIGHT) {
            if (board.getCardInBoard(board.getCoordinate(c).getX() - 1, board.getCoordinate(c).getY() - 1)!= null &&
                    board.getCardInBoard(board.getCoordinate(c).getX() - 1, board.getCoordinate(c).getY() - 1).getKingdom() == this.elemRequired) {
                return board.getCardInBoard(board.getCoordinate(c).getX() + 1, board.getCoordinate(c).getY() + 1) != null &&
                        (board.getCardInBoard(board.getCoordinate(c).getX() + 1, board.getCoordinate(c).getY() + 1).getKingdom() == this.elemRequired);
            }
        } else if (this.cornerPosition == CornerPosition.UPLEFT) {
            if (board.getCardInBoard(board.getCoordinate(c).getX() + 1, board.getCoordinate(c).getY() - 1)!=null &&
                    board.getCardInBoard(board.getCoordinate(c).getX() + 1, board.getCoordinate(c).getY() - 1).getKingdom() == this.elemRequired) {
                return board.getCardInBoard(board.getCoordinate(c).getX() - 1, board.getCoordinate(c).getY() + 1) != null &&
                        (board.getCardInBoard(board.getCoordinate(c).getX() - 1, board.getCoordinate(c).getY() + 1).getKingdom() == this.elemRequired);
            }
        }
        return false;
    }*/

}
