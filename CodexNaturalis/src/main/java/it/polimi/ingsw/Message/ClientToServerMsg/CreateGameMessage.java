package it.polimi.ingsw.Message.ClientToServerMsg;

public class CreateGameMessage extends GenericClientMessage{

    public CreateGameMessage(String nickname) {
        super(0,nickname);
        super.isMessageForMain();
    }
}
