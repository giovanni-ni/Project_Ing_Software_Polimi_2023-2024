package it.polimi.ingsw.Networking.Listeners;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.ObjectOutputStream;

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
}
