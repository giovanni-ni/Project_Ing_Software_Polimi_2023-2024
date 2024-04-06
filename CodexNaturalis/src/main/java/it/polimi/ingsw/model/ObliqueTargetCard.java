package it.polimi.ingsw.model;

import com.google.common.collect.BiMap;

public class ObliqueTargetCard extends TargetCard{

    Elements elemRequired;

    CornerPosition cp;

    public ObliqueTargetCard() {

    }
    public ObliqueTargetCard(Elements elemRequired, CornerPosition cp) {
        this.elemRequired = elemRequired;
        this.cp = cp;
    }

    public Elements getElemRequired() {
        return elemRequired;
    }

    public void setElemRequired(Elements elemRequired) {
        this.elemRequired = elemRequired;
    }

    public CornerPosition getCp() {
        return cp;
    }

    public void setCp(CornerPosition cp) {
        this.cp = cp;
    }

    @Override
    public int countPoint(Board board) {
        return super.getbasePoint() * checkGoal(board);
    }
    @Override
    public int checkGoal(Board board) {
        BiMap<Card,Coordinate> copy = board.getCardCoordinate();
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
        if (this.cp == CornerPosition.UPLEFT) {
            if (board.getCardInBoard(board.getCoordinate(c).getX() - 1, board.getCoordinate(c).getY() - 1).getKingdom() == this.elemRequired) {
                if ((board.getCardInBoard(board.getCoordinate(c).getX() + 1, board.getCoordinate(c).getY() + 1).getKingdom() == this.elemRequired)) {
                    return true;
                }
            }
        } else if (this.cp == CornerPosition.UPRIGHT) {
            if (board.getCardInBoard(board.getCoordinate(c).getX() + 1, board.getCoordinate(c).getY() - 1).getKingdom() == this.elemRequired) {
                if ((board.getCardInBoard(board.getCoordinate(c).getX() - 1, board.getCoordinate(c).getY() + 1).getKingdom() == this.elemRequired)) {
                    return true;
                }
            }
        }
        return false;
    }

}
