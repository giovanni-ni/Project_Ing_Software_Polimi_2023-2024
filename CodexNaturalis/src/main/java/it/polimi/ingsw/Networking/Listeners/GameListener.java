package it.polimi.ingsw.Networking.Listeners;

import it.polimi.ingsw.Message.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class GameListener implements Listener {

    private final ObjectOutputStream out;

    private String nickname;

    private Integer GameID ;

    public GameListener(ObjectOutputStream outputStream) {
        this.out = outputStream;
    }

    @Override
    public void update(Message msg) {
        try {
            out.writeObject(msg);
        } catch (IOException e) {

        }

    }
    public void setNickname(String nickname) {
    }

    public OutputStream getOut() {
        return out;
    }

    public String getNickname() {
        return nickname;

    }

    public Integer getGameID() {
        return GameID;
    }

    public void setGameID(Integer gameID) {
        GameID = gameID;
    }
}
