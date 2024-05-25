package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.Message.ClientToServerMsg.SetTargetCardMessage;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetCard;
import it.polimi.ingsw.utils.pathSearch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class PrepareController extends GenericSceneController {
    @FXML
    CheckBox targetOneCheck, targetTwoCheck;

    @FXML
    ImageView targetOneImg, targetTwoImg;

    @FXML
    void confirmSelectTarget(ActionEvent event) throws RemoteException {
        if (targetOneCheck.isSelected() && targetTwoCheck.isSelected()){
            super.ShowErrorMessage("Please select only one target");
        } else if (!targetTwoCheck.isSelected() && !targetOneCheck.isSelected()) {
            super.ShowErrorMessage("Please select a target");
        }else if (targetOneCheck.isSelected()){
            getGuiApplication().getGui().notify(new SetTargetCardMessage(0));
        }else {
            getGuiApplication().getGui().notify(new SetTargetCardMessage(1));
        }
    }

    @Override
    public void updateModel() throws IOException {
        Player myPlayer =getGuiApplication().getGui().getMyMatch().getPlayerByNickname(getGuiApplication().getGui().getName());
        List<TargetCard> targetCards = List.of(myPlayer.getTargetOnHand());
        targetOneImg.setImage(new Image(pathSearch.getPathByCardID(targetCards.getFirst().getIdCard(), true)));
        targetTwoImg.setImage(new Image(pathSearch.getPathByCardID(targetCards.getFirst().getIdCard(), true)));
    }

}
