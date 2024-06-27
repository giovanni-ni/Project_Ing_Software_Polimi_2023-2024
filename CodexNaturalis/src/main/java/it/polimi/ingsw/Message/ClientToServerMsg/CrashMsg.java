package it.polimi.ingsw.Message.ClientToServerMsg;

public class CrashMsg extends GenericClientMessage {

    public CrashMsg(String nickNameDisconnect) {
        super(nickNameDisconnect);
        isMessageForMain();
    }

    public String getNickNameDisconnect() {
        return super.getNickname();
    }
}
