package it.polimi.ingsw.Networking.Listeners;

import it.polimi.ingsw.Message.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
// todo javadoc
public class ViewListener implements Listener{

    private final ObjectOutputStream out;

    public ViewListener(ObjectOutputStream out) {
        this.out = out;
    }

    @Override
    public void update(Message msg) {
            try {
                out.writeObject(msg);
            } catch (IOException e) {

            }
    }

    @Override
    public void setNickname(String nickname) {

    }

    @Override
    public String getNickname() {
        return null;
    }

    @Override
    public Integer getGameID() {
        return null;
    }

    @Override
    public void setGameID(Integer gameID) {

    }
}
