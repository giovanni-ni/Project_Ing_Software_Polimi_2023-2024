package it.polimi.ingsw.Message.ClientToServerMsg;

public class SetReadyMessage extends GenericClientMessage {
    public SetReadyMessage(int id, String nickname) {
        super(id,nickname);
        //super.isMessageForMain();
    }
    public SetReadyMessage() {
    }
}
