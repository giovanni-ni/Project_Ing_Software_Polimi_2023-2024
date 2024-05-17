package it.polimi.ingsw.Message.ServerToClientMsg;

public class ActionNotRecognize extends GenericServerMessage{
    private  String description;

    public ActionNotRecognize(String description) {
        this.description=description;
    }

    public ActionNotRecognize() {
    }

    public String getDescription() {
        return this.description;
    }
}
