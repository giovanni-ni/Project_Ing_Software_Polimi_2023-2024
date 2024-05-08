package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Coordinate;

public class playCardMessage extends GenericClientMessage{
    private boolean front;
    private Coordinate coo;
    private int indexOfCardOnHand;

    public Coordinate getCoo() {
        return coo;
    }

    public void setCoo(Coordinate coo) {
        this.coo = coo;
    }

    public int getIndexOfCardOnHand() {
        return indexOfCardOnHand;
    }

    public void setIndexOfCardOnHand(int indexOfCardOnHand) {
        this.indexOfCardOnHand = indexOfCardOnHand;
    }

    public boolean isFront() {
        return front;
    }

    public void setFront(boolean front) {
        this.front = front;
    }

    public playCardMessage(int indexOfCardOnHand, boolean front, int x, int y) {
        super.isMessageNotForMain();
        this.indexOfCardOnHand=indexOfCardOnHand;
        coo = new Coordinate(x,y);
        this.front=front;

    }
}
