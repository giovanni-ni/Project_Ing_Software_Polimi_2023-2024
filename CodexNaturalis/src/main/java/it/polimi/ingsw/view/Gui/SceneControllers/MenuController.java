package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.Message.ClientToServerMsg.CreateGameMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.JoinFirstMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.JoinGameMessage;
import it.polimi.ingsw.view.Gui.ScenesName;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.rmi.RemoteException;
import java.util.Objects;

/**
 * Controller class for managing the main menu scene of the game.
 * Handles creating and joining game sessions, nickname input, and UI navigation.
 */
public class MenuController extends GenericSceneController {

    public CheckBox autoStartCheck;
    public ChoiceBox playerNumber;
    @FXML
    TextField gameId;
    @FXML
    TextField nickName;

    private boolean isCreateGame = false;
    private boolean isJoinGame = false;
    private int numOfError = 0;

    /**
     * Event handler for sending nickname and initiating a game session creation or joining.
     * Handles button click events for sending nickname and starting/joining a game.
     *
     * @param event The action event triggering the method call.
     * @throws RemoteException If there's an error in remote communication.
     */
    @FXML
    void sendNickName(ActionEvent event) throws RemoteException {
        // Handle default and special nicknames
        if (Objects.equals(nickName.getText(), "")) {
            nickName.setText("nuge" + numOfError);
        }
        if (Objects.equals(nickName.getText(), "stefano")) {
            nickName.setText("别玩了呐 你已经输了");
        }

        // Determine action based on current mode (create game or join game)
        if (isCreateGame) {
            // Create game message with or without auto-start settings
            if (autoStartCheck.isSelected()) {
                int num = (int) playerNumber.getValue();
                getGuiApplication().getGui().notify(new CreateGameMessage(nickName.getText(), num));
            } else {
                getGuiApplication().getGui().notify(new CreateGameMessage(nickName.getText()));
            }
        } else {
            // Join game message
            getGuiApplication().getGui().notify(new JoinFirstMessage(nickName.getText()));
        }
        // Show joining message to user
        ShowJoiningMessage();
    }

    /**
     * Event handler for sending nickname and game ID to join an existing game session.
     * Handles button click events for sending nickname and game ID.
     *
     * @param event The action event triggering the method call.
     * @throws RemoteException If there's an error in remote communication.
     */
    @FXML
    void sendAll(ActionEvent event) throws RemoteException {
        // Validate game ID as numeric
        if (gameId.getText().matches("\\d+")) {
            // Handle default nickname if empty
            if (Objects.equals(nickName.getText(), "")) {
                nickName.setText("nuge" + numOfError);
            }
            // Determine action based on current mode (join game or reconnect)
            if (isJoinGame) {
                getGuiApplication().getGui().notify(new JoinGameMessage(nickName.getText(), Integer.parseInt(gameId.getText())));
            } else {
                getGuiApplication().getGui().notify(new JoinGameMessage(nickName.getText(), Integer.parseInt(gameId.getText())));
                // todo: handle reconnect logic if needed
            }
            // Show joining message to user
            ShowJoiningMessage();
        } else {
            // Show error message for invalid game ID format
            ShowErrorMessage("Game Id should be a number");
        }
    }

    /**
     * Switches to the create game scene.
     * Shows the scene where players can create a new game session.
     *
     * @param event The action event triggering the method call.
     */
    @FXML
    void goCreateGame(ActionEvent event) {
        getGuiApplication().showScene(ScenesName.ASKNICKNAME);
        MenuController nextC = (MenuController) getGuiApplication().getActualSceneController();
        nextC.setCreateGame(true);
    }

    /**
     * Switches to the join game scene.
     * Shows the scene where players can join an existing game session.
     *
     * @param event The action event triggering the method call.
     */
    @FXML
    void goJoinGame(ActionEvent event) {
        getGuiApplication().showScene(ScenesName.EASYJOIN);
        MenuController nextC = (MenuController) getGuiApplication().getActualSceneController();
        nextC.setJoinGame(true);
    }

    /**
     * Switches to the reconnect scene.
     * Shows the scene where players can reconnect to an ongoing game session.
     *
     * @param event The action event triggering the method call.
     */
    @FXML
    void goReconnect(ActionEvent event) {
        getGuiApplication().showScene(ScenesName.EASYJOIN);
        // todo: implement reconnect logic if needed
    }

    /**
     * Switches to the initial join scene.
     * Shows the initial scene where players enter their nickname.
     *
     * @param event The action event triggering the method call.
     */
    @FXML
    void goJoinFirst(ActionEvent event) {
        getGuiApplication().showScene(ScenesName.ASKNICKNAME);
    }

    /**
     * Displays an error message to the user.
     * Shows an error message on the UI in red color.
     *
     * @param string The error message to display.
     */
    @Override
    public void ShowErrorMessage(String string) {
        ErrorMessage.setText(string);
        ErrorMessage.setFill(Color.RED);
        numOfError++;
        nickName.clear();
    }

    /**
     * Displays a joining message to the user.
     * Shows a message on the UI indicating that the game is being joined in green color.
     */
    public void ShowJoiningMessage() {
        ErrorMessage.setText("Joining");
        ErrorMessage.setFill(Color.GREEN);
    }

    /**
     * Enables or disables player number selection based on auto start checkbox.
     * If auto start is selected in create game mode, enables player number selection.
     * Otherwise, disables player number selection.
     *
     * @param actionEvent The action event triggering the method call.
     */
    public void autoStartEnable(ActionEvent actionEvent) {
        if (autoStartCheck.isSelected() && isCreateGame) {
            playerNumber.getItems().add(2);
            playerNumber.getItems().add(3);
            playerNumber.getItems().add(4);
            playerNumber.setValue(4);
            playerNumber.setDisable(false);
        } else {
            playerNumber.setDisable(true);
        }
    }

    /**
     * Sets the flag indicating whether the user is in create game mode.
     *
     * @param createGame True if in create game mode, false otherwise.
     */
    public void setCreateGame(boolean createGame) {
        isCreateGame = createGame;
    }

    /**
     * Sets the flag indicating whether the user is in join game mode.
     *
     * @param joinGame True if in join game mode, false otherwise.
     */
    public void setJoinGame(boolean joinGame) {
        isJoinGame = joinGame;
    }
}
