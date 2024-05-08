package it.polimi.ingsw.Message.ClientToServerMsg;

public class JoinGameMessage extends GenericClientMessage{
    public JoinGameMessage(String nickname, int idGame) {
        super(idGame,nickname);
        super.isMessageForMain();
    }
}
