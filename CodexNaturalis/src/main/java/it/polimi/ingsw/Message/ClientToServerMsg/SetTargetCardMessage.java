package it.polimi.ingsw.Message.ClientToServerMsg;

public class SetTargetCardMessage extends GenericClientMessage {

    private int choice;

    public SetTargetCardMessage(int gameID, String nickname, int choice) {
        super(gameID, nickname);
        this.choice = choice;
    }
    public SetTargetCardMessage(int choice) {
        this.choice =choice;
    }

    public int getChoice() {
        return choice;
    }
}
