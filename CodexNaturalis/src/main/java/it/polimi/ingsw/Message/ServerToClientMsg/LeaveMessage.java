package it.polimi.ingsw.Message.ServerToClientMsg;



public class LeaveMessage extends GenericServerMessage {
    private String leftPlayer;

    public LeaveMessage(String leftPlayer) {
        this.leftPlayer = leftPlayer;
    }
}
