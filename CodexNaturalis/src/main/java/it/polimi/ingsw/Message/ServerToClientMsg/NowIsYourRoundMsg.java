package it.polimi.ingsw.Message.ServerToClientMsg;

public class NowIsYourRoundMsg extends GenericServerMessage{

    private String description;

    public NowIsYourRoundMsg() {
        this.description = "it's your turn";
    }

    public String getDescription() {
        return this.description;
    }
}
