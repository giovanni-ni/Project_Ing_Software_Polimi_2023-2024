package it.polimi.ingsw.Message.ClientToServerMsg;

public class ReconnectMessage extends GenericClientMessage{
    public ReconnectMessage(String nickname, int idGame) {
        super(idGame, nickname);
        super.setMainControllerMessage(true);
    }
}
