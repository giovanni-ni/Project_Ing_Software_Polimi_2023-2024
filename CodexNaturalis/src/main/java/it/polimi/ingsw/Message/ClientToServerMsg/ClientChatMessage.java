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

    public void setFromPlayer(String fromPlayer) {
        this.fromPlayer = fromPlayer;
    }

    public String getToPlayer() {
        return toPlayer;
    }

    public void setToPlayer(String toPlayer) {
        this.toPlayer = toPlayer;
    }

    public boolean isForAll() {
        return isForAll;
    }

    public void setForAll(boolean forAll) {
        isForAll = forAll;
    }

    public String getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(String chatMsg) {
        this.chatMsg = chatMsg;
    }
}
