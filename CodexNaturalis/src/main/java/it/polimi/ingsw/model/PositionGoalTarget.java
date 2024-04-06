package it.polimi.ingsw.model;

import com.google.common.collect.BiMap;

public class PositionGoalTarget extends TargetCard{

    Elements firstElement;

    Elements secondElement;

    CornerPosition cp;

    public PositionGoalTarget() {

    }

    public PositionGoalTarget(Elements firstElement,Elements secondElement, CornerPosition cp) {
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

        BiMap<Card,Coordinate> copy = board.getCardCoordinate(); //it pass the address or is a copy??
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
    }
    @Override
    public int countPoint(Board board) {
        return super.getbasePoint() * checkGoal(board);
    }

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

}
