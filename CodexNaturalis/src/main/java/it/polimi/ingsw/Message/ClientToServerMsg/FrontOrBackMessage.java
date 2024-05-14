package it.polimi.ingsw.Message.ClientToServerMsg;

public class FrontOrBackMessage extends GenericClientMessage {
    private boolean frontOrBack;

    public FrontOrBackMessage(int gameID, String nickname, boolean choice) {
        super(gameID, nickname);
        this.frontOrBack = choice;
    }

    public boolean getFrontOrBack() {
        return frontOrBack;
    }
}
