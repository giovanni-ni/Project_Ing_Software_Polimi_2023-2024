package it.polimi.ingsw.Message.ClientToServerMsg;

public class JoinFirstMessage extends GenericClientMessage{
    public JoinFirstMessage(String nickname) {
        super.isMessageForMain();
    }
}
