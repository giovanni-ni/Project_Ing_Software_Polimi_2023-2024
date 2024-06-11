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
import org.w3c.dom.Text;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;

public class MenuController extends GenericSceneController{

    public CheckBox autoStartCheck;
    public ChoiceBox playerNumber;
    private boolean isCreateGame =false;
    private boolean isJoinGame = false;
    private int numOfError=0;
    @FXML
    TextField gameId;

    @FXML
    TextField nickName;

    @FXML
    void sendNickName(ActionEvent event) throws RemoteException {
        if (Objects.equals(nickName.getText(), "")){
            nickName.setText("nuge"+numOfError);
        }

        if(Objects.equals(nickName.getText(), "stefano")) {
            nickName.setText("别玩了呐 你已经输了");
        }

        if (isCreateGame){
            if (autoStartCheck.isSelected()){
                int num = (int) playerNumber.getValue();
                getGuiApplication().getGui().notify(new CreateGameMessage(nickName.getText(),num));
            }else {
                getGuiApplication().getGui().notify(new CreateGameMessage(nickName.getText()));
            }
        }else {
            getGuiApplication().getGui().notify(new JoinFirstMessage(nickName.getText())); //ToDO if we do join first method
        }
        ShowJoiningMessage();

    }
    @FXML
    void sendAll(ActionEvent event) throws RemoteException {
        if (gameId.getText().matches("\\d+")){
            if (Objects.equals(nickName.getText(), "")){
                nickName.setText("nuge"+numOfError);
            }
            if (isJoinGame){
                getGuiApplication().getGui().notify(new JoinGameMessage(nickName.getText(), Integer.parseInt(gameId.getText())));
            }else {
                getGuiApplication().getGui().notify(new JoinGameMessage(nickName.getText(),Integer.parseInt(gameId.getText())));
                //todo reconnect if we do that
            }
            ShowJoiningMessage();

        }else {
            ShowErrorMessage("Game Id should be a number");
        }

    }

    @FXML
    void goCreateGame(ActionEvent event) {
        getGuiApplication().showScene(ScenesName.ASKNICKNAME);
        MenuController nextC = (MenuController) getGuiApplication().getActualSceneController();
        nextC.setCreateGame(true);
    }
    @FXML
    void goJoinGame(ActionEvent event) {
        getGuiApplication().showScene(ScenesName.EASYJOIN);
        MenuController nextC = (MenuController) getGuiApplication().getActualSceneController();
        nextC.setJoinGame(true);
    }
    @FXML
    void goReconnect(ActionEvent event) {
        getGuiApplication().showScene(ScenesName.EASYJOIN);

    }
    @FXML
    void goJoinFirst(ActionEvent event) {
        getGuiApplication().showScene(ScenesName.ASKNICKNAME);

    }

    @Override
    public void ShowErrorMessage(String string){
        ErrorMessage.setText(string);
        ErrorMessage.setFill(Color.RED);
        numOfError++;
        nickName.clear();
    }
    public void ShowJoiningMessage(){
        ErrorMessage.setText("Joining");
        ErrorMessage.setFill(Color.GREEN);
    }

    public void autoStartEnable(ActionEvent actionEvent) {
        if(autoStartCheck.isSelected() && isCreateGame){
            playerNumber.getItems().add(2);
            playerNumber.getItems().add(3);
            playerNumber.getItems().add(4);

            playerNumber.setValue(4);
            playerNumber.setDisable(false);
        }else {
            playerNumber.setDisable(true);
        }

        
    }


    public void setCreateGame(boolean createGame) {
        isCreateGame = createGame;
    }

    public void setJoinGame(boolean joinGame) {
        isJoinGame = joinGame;
    }
}
