package it.polimi.ingsw.Message.ClientToServerMsg;

public class ReconnectRequestMsg extends GenericClientMessage{

    public ReconnectRequestMsg(String nickname, int id) {
        super(id, nickname);
        super.setMainControllerMessage(true);
    }

}
