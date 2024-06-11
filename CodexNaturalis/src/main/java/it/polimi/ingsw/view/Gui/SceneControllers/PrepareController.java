package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.Message.ClientToServerMsg.FrontOrBackMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.SetTargetCardMessage;
import it.polimi.ingsw.model.InitialCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetCard;
import it.polimi.ingsw.utils.pathSearch;
import it.polimi.ingsw.view.Gui.ScenesName;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrepareController extends GenericSceneController {

    Image target1, target2,common1,common2,frontInitial, backInitial, textCommon, textTarget, textInitial;
    private  int step = 0;
    private boolean initialized = false;
    private String variableText;


    @FXML
    CheckBox targetOneCheck, targetTwoCheck;

    @FXML
    ImageView targetOneImg, targetTwoImg, maintext;

    @FXML
    Button confirm;

    @FXML
    void confirmSelectTarget(ActionEvent event) throws IOException {

            if (step ==0){
                step=1;
                updateModel(UPDATE.GENERAL);
            }else{
                if (targetOneCheck.isSelected() && targetTwoCheck.isSelected()){
                    super.ShowErrorMessage("Please select only one "+variableText);
                } else if (!targetTwoCheck.isSelected() && !targetOneCheck.isSelected()) {
                    super.ShowErrorMessage("Please select a "+variableText);
                }else if (targetOneCheck.isSelected()){
                    if (step==1){
                        getGuiApplication().getGui().notify(new SetTargetCardMessage(0));
                        step=2;
                    }
                    else {
                        getGuiApplication().getGui().notify(new FrontOrBackMessage(true));
                        step =3;
                    }
                }else {
                    if (step==1){
                        getGuiApplication().getGui().notify(new SetTargetCardMessage(1));
                        step=2;
                    }
                    else {
                        getGuiApplication().getGui().notify(new FrontOrBackMessage(false));
                        step=3;
                    }
                }



        }

    }


    @Override
    public void updateModel(UPDATE update) throws IOException {

        if (!initialized){
            Player myPlayer =getGuiApplication().getGui().getMyMatch().getPlayerByNickname(getGuiApplication().getGui().getUsername());
            List<TargetCard> targetCards = List.of(myPlayer.getTargetOnHand());
            ArrayList<TargetCard> commonTargets = getGuiApplication().getGui().getMyMatch().getCommonTarget();
            InitialCard initialCard = myPlayer.getInitialCard();
            target1 =new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathSearch.getPathByCardID(targetCards.getFirst().getIdCard(), true))));
            target2 =new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathSearch.getPathByCardID(targetCards.getLast().getIdCard(), true))));
            frontInitial =new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathSearch.getPathByCardID(initialCard.getCode(), true))));
            backInitial =new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathSearch.getPathByCardID(initialCard.getCode(), false))));
            common1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathSearch.getPathByCardID(commonTargets.getFirst().getIdCard(), true))));
            common2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathSearch.getPathByCardID(commonTargets.getLast().getIdCard(), true))));
            textCommon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/view/commonTarget.png")));
            textInitial =new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/view/setInitial.png")));
            textTarget =new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/view/targetSelect.png")));
            initialized =true;
            targetOneImg.setOnMouseClicked(mouseEvent -> {
                    targetOneCheck.setSelected(true);
                    onlyOne(new ActionEvent());
            });

            targetOneImg.setCursor(Cursor.HAND);
            targetTwoImg.setOnMouseClicked(mouseEvent -> {
                    targetTwoCheck.setSelected(true);
                    onlyTwo(new ActionEvent());
            });
            targetTwoImg.setCursor(Cursor.HAND);

            confirm.setDisable(false);


        }
        switch (step){
            case 0 ->{
                maintext.setImage(textCommon);
                targetOneImg.setImage(common1);
                targetTwoImg.setImage(common2);
                targetOneCheck.setVisible(false);
                targetTwoCheck.setVisible(false);
            }
            case 1->{
                maintext.setImage(textTarget);
                targetOneImg.setImage(target1);
                targetTwoImg.setImage(target2);
                targetOneCheck.setVisible(true);
                targetTwoCheck.setVisible(true);
                variableText ="target";

            }
            case 2 -> {
                maintext.setImage(textInitial);
                targetOneImg.setImage(frontInitial);
                targetTwoImg.setImage(backInitial);
                targetOneCheck.setVisible(true);
                targetTwoCheck.setVisible(true);
                variableText ="side";
            }
            default -> {
               Platform.runLater(()->getGuiApplication().showScene(ScenesName.BOARD));
            }
        }
    }
    @FXML
    void onlyOne(ActionEvent event){
            targetTwoCheck.setSelected(false);
    }
    @FXML
    void onlyTwo(ActionEvent event){
            targetOneCheck.setSelected(false);
    }

}
