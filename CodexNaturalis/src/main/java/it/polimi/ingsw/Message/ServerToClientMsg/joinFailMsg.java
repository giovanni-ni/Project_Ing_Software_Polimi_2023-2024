package it.polimi.ingsw.Message.ServerToClientMsg;

public class joinFailMsg extends GenericServerMessage{
    private final String description;

    public joinFailMsg(String description) {
        this.description=description;
    }

    public String getDescription() {
        return description;
    }
}
