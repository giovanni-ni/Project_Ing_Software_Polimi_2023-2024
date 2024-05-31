package it.polimi.ingsw.Message.ClientToServerMsg;

public class drawCardMessage extends GenericClientMessage{
    private boolean deck;
    private int numberindex;

    public boolean getDeck() {
        return deck;
    }

    public int getNumberindex() {
        return numberindex;
    }

    public drawCardMessage(String nickname,int gameId, boolean deck, int number) {
        super(gameId, nickname);
        this.deck=deck;
        this.numberindex=number;
        super.isMessageNotForMain();
    }
}
