package it.polimi.ingsw.Networking.Listeners;

import it.polimi.ingsw.Message.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
// todo javadoc
public class SocketListener implements Listener {

    private transient final ObjectOutputStream out;

    private String nickname;

    private Integer GameID ;

    public SocketListener(ObjectOutputStream outputStream) {
        this.out = outputStream;
    }

    @Override
    public void update(Message msg) {
        try {
            out.writeObject(msg);
            finishSending();
        } catch (IOException e) {

        }

    }
    @Override
    public void setNickname(String nickname) {
        this.nickname =nickname;
    }
    private void finishSending() throws IOException {
        out.flush();
        out.reset();
    }

    public OutputStream getOut() {
        return out;
    }
    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public Integer getGameID() {
        return GameID;
    }

    @Override
    public void setGameID(Integer gameID) {
        GameID = gameID;
    }
}
