package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.model.Deck;

public class drawCardMessage extends GenericClientMessage{
    private boolean deck;
    private int numberindex;

    public boolean getDeck() {
        return deck;
    }

    public void setDeck(boolean deck) {
        this.deck = deck;
    }

    public int getNumberindex() {
        return numberindex;
    }

    public void setNumberindex(int numberindex) {
        this.numberindex = numberindex;
    }

    public drawCardMessage(boolean deck, int number) {
        this.deck=deck;
        this.numberindex=number;
        super.isMessageNotForMain();
    }
}
