package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.Message.ClientToServerMsg.SetReadyMessage;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller class for the waiting scene where players can ready up.
 */
public class WaitingController extends GenericSceneController implements Initializable {

    private HashMap<Integer, ImageView> ballList;
    private HashMap<Integer, ImageView> tickList;
    private HashMap<Integer, Text> nickList;
    private boolean isReady = false;

    @FXML
    ImageView blueBall, redBall, yellowBall, greenBall, tickBlue, tickRed, tickYellow, tickGreen;

    @FXML
    Text roomID, nickBlue, nickRed, nickYellow, nickGreen;

    /**
     * Event handler method for setting the player's ready status.
     * Notifies the server with a SetReadyMessage when the player is not ready.
     *
     * @throws RemoteException If there is an issue with remote method invocation.
     */
    @FXML
    void setPlayerReady() throws RemoteException {
        ArrayList<Player> players = getGuiApplication().getGui().getMyMatch().getPlayers();
        for (Player p : players){
            if (Objects.equals(p.nickname, getGuiApplication().getGui().getUsername()));
            isReady = p.getReady();
        }
        if (!isReady) {
            getGuiApplication().getGui().notify(new SetReadyMessage());
        }
    }

    /**
     * Updates the model based on the current state of players in the match.
     *
     * @param update The type of update triggering the model update.
     */
    @Override
    public void updateModel(UPDATE update) {
        ArrayList<Player> players = getGuiApplication().getGui().getMyMatch().getPlayers();
        int i = 0;
        for (Player p : players) {
            ballList.get(i).setVisible(true);
            nickList.get(i).setText(p.nickname);
            tickList.get(i).setVisible(p.getReady());
            i++;
        }
        roomID.setText("Room ID: " + getGuiApplication().getGui().getMatchID());
    }

    /**
     * Retrieves the HashMap of ImageView objects representing player balls.
     *
     * @return The HashMap containing player ball ImageViews.
     */
    private HashMap<Integer, ImageView> getBallList() {
        return getPlayerColorImageViewHashMap(blueBall, redBall, yellowBall, greenBall);
    }

    /**
     * Retrieves the HashMap of ImageView objects representing player ready ticks.
     *
     * @return The HashMap containing player ready tick ImageViews.
     */
    private HashMap<Integer, ImageView> getTickList() {
        return getPlayerColorImageViewHashMap(tickBlue, tickRed, tickYellow, tickGreen);
    }

    /**
     * Creates a HashMap of ImageView objects based on player colors.
     *
     * @param tickBlue   The ImageView for the blue player's ready tick.
     * @param tickRed    The ImageView for the red player's ready tick.
     * @param tickYellow The ImageView for the yellow player's ready tick.
     * @param tickGreen  The ImageView for the green player's ready tick.
     * @return The HashMap containing player ready tick ImageViews.
     */
    private HashMap<Integer, ImageView> getPlayerColorImageViewHashMap(ImageView tickBlue, ImageView tickRed, ImageView tickYellow, ImageView tickGreen) {
        HashMap<Integer, ImageView> tickList = new HashMap<>();
        tickList.put(0, tickBlue);
        tickList.put(1, tickRed);
        tickList.put(2, tickYellow);
        tickList.put(3, tickGreen);
        return tickList;
    }

    /**
     * Retrieves the HashMap of Text objects representing player nicknames.
     *
     * @return The HashMap containing player nickname Text objects.
     */
    private HashMap<Integer, Text> getNickList() {
        HashMap<Integer, Text> nickList = new HashMap<>();
        nickList.put(0, nickBlue);
        nickList.put(1, nickRed);
        nickList.put(2, nickYellow);
        nickList.put(3, nickGreen);
        return nickList;
    }

    /**
     * Displays an error message on the screen with a specified string and sets the text color to red.
     *
     * @param string The error message to display.
     */
    @Override
    public void ShowErrorMessage(String string) {
        ErrorMessage.setText(string);
        ErrorMessage.setFill(Color.RED);
        isReady = false;
    }

    /**
     * Initializes the waiting scene controller, setting up the initial state of player balls,
     * ready ticks, and player nicknames.
     *
     * @param url            The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources specific to this controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tickList = getTickList();
        ballList = getBallList();
        nickList = getNickList();
        for (int i =0; i< ballList.size();i++) {
            ballList.get(i).setVisible(false);
            nickList.get(i).setText("");
            tickList.get(i).setVisible(false);
        }
    }
}
