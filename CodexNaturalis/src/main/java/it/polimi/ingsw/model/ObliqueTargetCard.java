package it.polimi.ingsw.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ObliqueTargetCard extends TargetCard{

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
    public int checkGoal(Board board) {
        BiMap<Card,Coordinate> copy = HashBiMap.create();
        copy.putAll(board.getCardCoordinate());
        int n = 0;

        for(Card c: board.getCardCoordinate().keySet()) {
            if(c.getKingdom().equals(this.elemRequired))  {
                if(isCorrect(c, board)) {
                    n++;
                    copy.remove(c);
                }
            }
        }
        return n;
    }


    private boolean isCorrect(Card c, Board board) {
        if (this.cornerPosition == CornerPosition.UPLEFT) {
            if (board.getCardInBoard(board.getCoordinate(c).getX() - 1, board.getCoordinate(c).getY() - 1).getKingdom() == this.elemRequired) {
                if ((board.getCardInBoard(board.getCoordinate(c).getX() + 1, board.getCoordinate(c).getY() + 1).getKingdom() == this.elemRequired)) {
                    return true;
                }
            }
        } else if (this.cornerPosition == CornerPosition.UPRIGHT) {
            if (board.getCardInBoard(board.getCoordinate(c).getX() + 1, board.getCoordinate(c).getY() - 1).getKingdom() == this.elemRequired) {
                if ((board.getCardInBoard(board.getCoordinate(c).getX() - 1, board.getCoordinate(c).getY() + 1).getKingdom() == this.elemRequired)) {
                    return true;
                }
            }
        }
        return false;
    }

}
