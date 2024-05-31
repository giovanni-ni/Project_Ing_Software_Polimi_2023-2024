package it.polimi.ingsw.Message.ClientToServerMsg;


import it.polimi.ingsw.Message.Message;

public class ClientChatMessage extends GenericClientMessage {
    private String fromPlayer;
    private String toPlayer;
    boolean isForAll;
    private String chatMsg;

    public ClientChatMessage(int idGame,String nickName,boolean isForAll,String toPlayer,String chatMsg) {
        super(idGame,nickName);
        fromPlayer=nickName;
        this.toPlayer=toPlayer;
        this.isForAll=isForAll;
        this.chatMsg=chatMsg;
        isMessageNotForMain();
    }

    public String getFromPlayer() {
        return super.getNickname();
    }

    public String getToPlayer() {
        return toPlayer;
    }

    public boolean isForAll() {
        return isForAll;
    }

    public String getChatMsg() {
        return chatMsg;
    }

}
