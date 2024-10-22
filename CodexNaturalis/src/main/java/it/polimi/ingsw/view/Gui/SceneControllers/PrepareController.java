package it.polimi.ingsw.view.Gui.SceneControllers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.Message.ClientToServerMsg.ChooseColorMsg;
import it.polimi.ingsw.Message.ClientToServerMsg.FrontOrBackMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.SetTargetCardMessage;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.PathSearch;
import it.polimi.ingsw.view.Gui.ScenesName;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static it.polimi.ingsw.utils.PathSearch.getPathByCard;
public class PrepareController extends GenericSceneController implements Initializable {

    // FXML Injected fields
    @FXML
    public ImageView green, blu, yellow, red, resourceBack, resource1, resource2, goldBack, gold1, gold2, waiting, Loading;
    @FXML
    public CheckBox bluCheck, greenCheck, yellowCheck, redCheck;
    @FXML
    public StackPane colorPane, drawCardPane;
    @FXML
    public VBox vred, vyellow, vblue, vgreen;

    // Other fields
    private Image target1, target2, common1, common2, frontInitial, backInitial, textCommon, textTarget, textInitial, textColor, textDeck;
    private int step = 0;
    private boolean initialized = false;
    private ArrayList<ImageView> decksImages;
    private BiMap<PlayerColor, VBox> colorVboxs;
    private boolean colorChoose = false;
    @FXML
    private CheckBox targetOneCheck, targetTwoCheck;
    @FXML
    private ImageView targetOneImg, targetTwoImg, maintext;
    @FXML
    private Button confirm;

    /**
     * Handles the action when the confirm button for selecting target cards is pressed.
     * Progresses through different steps based on the current state.
     *
     * @param event ActionEvent generated by the user's interaction with the confirm button.
     * @throws IOException If there is an error in notifying the server.
     */
    @FXML
    void confirmSelectTarget(ActionEvent event) throws IOException {
        int choice = 0;
        if (step == 0 || step == 3) {
            step++;
            updateModel(UPDATE.GENERAL);
        } else {
            if (step == 1) {
                if (!targetOneCheck.isSelected() && !targetTwoCheck.isSelected())
                    super.ShowErrorMessage("Please choose at least one");
                else{
                    getGuiApplication().getGui().notify(new FrontOrBackMessage(targetOneCheck.isSelected()));
                    step=2;
                }

            } else if (step == 2) {
                BiMap<PlayerColor, CheckBox> allCheckBox = HashBiMap.create();
                allCheckBox.put(PlayerColor.RED, redCheck);
                allCheckBox.put(PlayerColor.BLUE, bluCheck);
                allCheckBox.put(PlayerColor.GREEN, greenCheck);
                allCheckBox.put(PlayerColor.YELLOW, yellowCheck);

                PlayerColor chosenColor = null;
                for (CheckBox checkBox : allCheckBox.values()) {
                    if (checkBox.isSelected()) {
                        chosenColor = allCheckBox.inverse().get(checkBox);
                        getGuiApplication().getGui().notify(new ChooseColorMsg(chosenColor));
                        colorChoose = true;
                    }
                }

            } else if (step == 4) {
                if (!targetOneCheck.isSelected() && !targetTwoCheck.isSelected())
                    super.ShowErrorMessage("Please choose at least one");
                else {
                    if (!targetOneCheck.isSelected())
                        choice = 1;
                    getGuiApplication().getGui().notify(new SetTargetCardMessage(choice));
                    step++;
                }

            }
        }

    }

    /**
     * Updates the GUI based on the state of the game model.
     * Manages the visibility of elements such as draw card areas, initial cards, colors,
     * and target cards based on the current step of the preparation phase.
     *
     * @param update The type of update to be applied to the GUI.
     * @throws IOException If there is an error in updating the GUI elements.
     */
    @Override
    public void updateModel(UPDATE update) throws IOException {

        if (!initialized){
            Player myPlayer =getGuiApplication().getGui().getMyMatch().getPlayerByNickname(getGuiApplication().getGui().getUsername());
            List<TargetCard> targetCards = List.of(myPlayer.getTargetOnHand());
            ArrayList<TargetCard> commonTargets = getGuiApplication().getGui().getMyMatch().getCommonTarget();
            InitialCard initialCard = myPlayer.getInitialCard();
            target1 =new Image(Objects.requireNonNull(getClass().getResourceAsStream(PathSearch.getPathByCardID(targetCards.getFirst().getIdCard(), true))));
            target2 =new Image(Objects.requireNonNull(getClass().getResourceAsStream(PathSearch.getPathByCardID(targetCards.getLast().getIdCard(), true))));
            frontInitial =new Image(Objects.requireNonNull(getClass().getResourceAsStream(PathSearch.getPathByCardID(initialCard.getCode(), true))));
            backInitial =new Image(Objects.requireNonNull(getClass().getResourceAsStream(PathSearch.getPathByCardID(initialCard.getCode(), false))));
            common1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(PathSearch.getPathByCardID(commonTargets.getFirst().getIdCard(), true))));
            common2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(PathSearch.getPathByCardID(commonTargets.getLast().getIdCard(), true))));
            updateDrawCard();
            initialized =true;
        }
        if (colorChoose && step==2&& update == UPDATE.GENERAL){
            step++;
        }
        Loading.setVisible(false);
        // step 0 show draw card area
        // step 1 initial card selection
        // step 2 color selection
        // step 3 show common target
        // step 4 private target selection
        switch (step){
            case 0 ->{
                maintext.setImage(textDeck);
                maintext.setVisible(true);

                setPane(colorPane,false);

                targetOneImg.setVisible(false);
                targetTwoImg.setVisible(false);

                setAllCheckBoxFalse();

                targetOneCheck.setVisible(false);
                targetTwoCheck.setVisible(false);

                setPane(drawCardPane,true);

                confirm.setDisable(false);
            }
            case 1 -> {
                setPane(drawCardPane,false);
                maintext.setImage(textInitial);
                targetOneImg.setImage(frontInitial);
                targetTwoImg.setImage(backInitial);
                targetOneImg.setVisible(true);
                targetTwoImg.setVisible(true);
                targetOneCheck.setVisible(true);
                targetTwoCheck.setVisible(true);
                confirm.setDisable(false);
            }
            case 2 ->{
                maintext.setImage(textColor);
                updateColorPane();
                setPane(colorPane,true);
                setPane(drawCardPane,false);
                targetOneImg.setVisible(false);
                targetTwoImg.setVisible(false);
                targetOneCheck.setVisible(false);
                targetTwoCheck.setVisible(false);
            }
            case 3 ->{

                setPane(colorPane,false);
                setPane(drawCardPane,false);
                maintext.setImage(textCommon);
                targetOneImg.setImage(common1);
                targetTwoImg.setImage(common2);
                targetOneCheck.setVisible(false);
                targetTwoCheck.setVisible(false);
                targetOneImg.setVisible(true);
                targetTwoImg.setVisible(true);
                confirm.setDisable(false);
            }
            case 4->{
                setPane(colorPane,false);
                setPane(drawCardPane,false);
                maintext.setImage(textTarget);
                targetOneImg.setImage(target1);
                targetTwoImg.setImage(target2);
                targetOneCheck.setVisible(true);
                targetTwoCheck.setVisible(true);
                targetOneImg.setVisible(true);
                targetTwoImg.setVisible(true);
                confirm.setDisable(false);
            }
            default -> {
                boolean allReady = true;
                ArrayList<Player> players = getGuiApplication().getGui().getMyMatch().getPlayers();
                for (Player p : players){
                    if (p.getPlayerID()==null){
                        allReady =false;
                    }
                }
                if (allReady){
                    Platform.runLater(()->getGuiApplication().showScene(ScenesName.BOARD));
                }else {
                    maintext.setVisible(false);
                    confirm.setDisable(false);
                    confirm.setVisible(false);
                    waiting.setVisible(true);
                    setPane(colorPane, false);
                    setPane(drawCardPane,false);
                    targetOneImg.setVisible(false);
                    targetTwoImg.setVisible(false);
                    setAllCheckBoxFalse();

                    targetOneCheck.setVisible(false);
                    targetTwoCheck.setVisible(false);
                }
            }
        }
    }

    /**
     * Updates the visibility of color selection panes based on the current player's available colors.
     * Hides panes that do not correspond to the player's color choice.
     */
    private void updateColorPane() {
        for (VBox vBox : colorVboxs.values()) {
            vBox.setVisible(false);
        }
        for (PlayerColor color : getGuiApplication().getGui().getMyMatch().getPlayerColors()){
            colorVboxs.get(color).setVisible(true);
        }
    }

    /**
     * Updates the images displayed for resource and gold draw cards based on the game model.
     *
     * @throws IOException If there is an error in retrieving or displaying the card images.
     */
    private void updateDrawCard() throws IOException {
        ArrayList<ResourceCard> resourceDeck = getGuiApplication().getGui().getMyMatch().getResourceDeck();
        ArrayList<GoldCard> goldDeck = getGuiApplication().getGui().getMyMatch().getGoldDeck();
        int i ;
        for (i =0; i<3 && i<resourceDeck.size() ; i++){
            ResourceCard card = resourceDeck.get(i);
            //set the third one back and others front
            if (i!=2)
                card.setFront(true);
            ImageView imageView = decksImages.get(i);
            imageView.setEffect(null);
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(getPathByCard(card)))));
        }
        while(i<3){
            //if the for loops ends because of deck empty disable others slots
            ImageView imageView = decksImages.get(i);
            imageView.setVisible(false);
            imageView.setEffect(null);
            i++;
        }
        //same logic for the gold cards slots
        for (i =0 ; i<3 && i<goldDeck.size() ; i++){
            GoldCard card = goldDeck.get(i);
            if (i!=2)
                card.setFront(true);
            ImageView imageView = decksImages.get(i+3);
            imageView.setEffect(null);
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(getPathByCard(card)))));
        }
        while(i<3){
            ImageView imageView = decksImages.get(i+3);
            imageView.setEffect(null);
            imageView.setVisible(false);
            i++;
        }
    }

    /**
     * Handles the action when only one target card is selected.
     *
     * @param event ActionEvent generated by the user's interaction with the target card.
     */
    @FXML
    void onlyOne(ActionEvent event) {
        targetTwoCheck.setSelected(false);
    }

    /**
     * Handles the action when only two target cards are selected.
     *
     * @param event ActionEvent generated by the user's interaction with the target cards.
     */
    @FXML
    void onlyTwo(ActionEvent event) {
        targetOneCheck.setSelected(false);
    }

    /**
     * Sets only the blue color checkbox as selected and clears all other color checkboxes.
     *
     * @param event ActionEvent generated by the user's interaction with the blue color checkbox.
     */
    public void onlyBlue(ActionEvent event) {
        setAllCheckBoxFalse();
        bluCheck.setSelected(true);
    }

    /**
     * Sets only the red color checkbox as selected and clears all other color checkboxes.
     *
     * @param event ActionEvent generated by the user's interaction with the red color checkbox.
     */
    public void onlyRed(ActionEvent event) {
        setAllCheckBoxFalse();
        redCheck.setSelected(true);
    }

    /**
     * Sets only the yellow color checkbox as selected and clears all other color checkboxes.
     *
     * @param event ActionEvent generated by the user's interaction with the yellow color checkbox.
     */
    public void onlyYellow(ActionEvent event) {
        setAllCheckBoxFalse();
        yellowCheck.setSelected(true);
    }

    /**
     * Sets only the green color checkbox as selected and clears all other color checkboxes.
     *
     * @param event ActionEvent generated by the user's interaction with the green color checkbox.
     */
    public void onlyGreen(ActionEvent event) {
        setAllCheckBoxFalse();
        greenCheck.setSelected(true);
    }

    /**
     * Handles the mouse event when only blue color selection is clicked.
     *
     * @param mouseEvent MouseEvent generated by the user's interaction with the blue color area.
     */
    public void onlyB(MouseEvent mouseEvent) {
        onlyBlue(new ActionEvent());
    }

    /**
     * Handles the mouse event when only red color selection is clicked.
     *
     * @param mouseEvent MouseEvent generated by the user's interaction with the red color area.
     */
    public void onlyR(MouseEvent mouseEvent) {
        onlyRed(new ActionEvent());
    }

    /**
     * Handles the mouse event when only yellow color selection is clicked.
     *
     * @param mouseEvent MouseEvent generated by the user's interaction with the yellow color area.
     */
    public void onlyY(MouseEvent mouseEvent) {
        onlyYellow(new ActionEvent());
    }
    /**
     * Handles the mouse event when only green color selection is clicked.
     * Calls {@link #onlyGreen(ActionEvent)} method to set only the green color checkbox as selected.
     *
     * @param mouseEvent MouseEvent generated by the user's interaction with the green color area.
     */
    public void onlyG(MouseEvent mouseEvent) {
        onlyGreen(new ActionEvent());
    }

    /**
     * Sets all checkboxes related to color and target selection to false.
     * Clears the selection from {@code targetOneCheck}, {@code targetTwoCheck},
     * {@code bluCheck}, {@code greenCheck}, {@code yellowCheck}, and {@code redCheck}.
     */
    public void setAllCheckBoxFalse() {
        ArrayList<CheckBox> allCheckBox = new ArrayList<>(List.of(targetOneCheck, targetTwoCheck, bluCheck, greenCheck, yellowCheck, redCheck));
        for (CheckBox checkBox : allCheckBox) {
            checkBox.setSelected(false);
        }
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     * Loads necessary images and sets up event handlers for GUI elements.
     *
     * @param url            The location used to resolve relative paths for the root object,
     *                       or {@code null} if the location is not known.
     * @param resourceBundle The resources used to localize the root object,
     *                       or {@code null} if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textCommon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/view/commonTarget.png")));
        textInitial = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/view/setInitial.png")));
        textTarget = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/view/targetSelect.png")));
        textColor = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/view/playerColorText.png")));
        textDeck = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/view/drawCardText.png")));

        // Set up event handlers for target card images
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

        maintext.setVisible(false);
        setPane(drawCardPane, false);

        // Make color checkboxes visible
        bluCheck.setVisible(true);
        redCheck.setVisible(true);
        yellowCheck.setVisible(true);
        greenCheck.setVisible(true);

        setPane(colorPane, false);
        setAllCheckBoxFalse();
        confirm.setDisable(false);

        // Initialize images for resource and gold decks
        decksImages = new ArrayList<>();
        decksImages.addAll(List.of(resource1, resource2, resourceBack, gold1, gold2, goldBack));

        // Initialize color VBox mappings
        colorVboxs = HashBiMap.create();
        colorVboxs.put(PlayerColor.RED, vred);
        colorVboxs.put(PlayerColor.BLUE, vblue);
        colorVboxs.put(PlayerColor.YELLOW, vyellow);
        colorVboxs.put(PlayerColor.GREEN, vgreen);
    }

    /**
     * Sets the visibility and disabled state of a {@link StackPane}.
     *
     * @param pane The {@link StackPane} whose visibility and disable state is to be set.
     * @param b    {@code true} to set the pane visible and enabled, {@code false} to hide and disable it.
     */
    private void setPane(StackPane pane, boolean b) {
        pane.setVisible(b);
        pane.setDisable(!b);
    }

    /**
     * Shows an error message in the GUI and resets the {@code colorChoose} flag to false.
     *
     * @param string The error message to display.
     */
    @Override
    public void ShowErrorMessage(String string) {
        super.ShowErrorMessage(string);
        colorChoose = false;
    }
}
