package it.polimi.ingsw.view.Gui.SceneControllers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import it.polimi.ingsw.Message.ClientToServerMsg.ClientChatMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.drawCardMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.playCardMessage;
import it.polimi.ingsw.Message.ServerToClientMsg.ServerChatMessage;
import it.polimi.ingsw.Networking.socket.Server;
import it.polimi.ingsw.model.*;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;

import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

import static it.polimi.ingsw.utils.pathSearch.getPathByCardID;
import static it.polimi.ingsw.view.TextualInterfaceUnit.Tui.myPlayer;

public class BoardController extends GenericSceneController implements Initializable {

    public VBox chatVBox;
    private Match match = new Match(0);

    private HashBiMap<PlayerColor, String> nickList;
    private String chatDestination;
    private ArrayList<ImageView> decksImages = new ArrayList<>();
    private ArrayList<ImageView> cardsOnHandImages = new ArrayList<>();
    private HashMap<ImageView, Integer> searchCode= new HashMap<>();
    Boolean initialized =false;
    Boolean isClickedCardOnHand=false;
    Boolean isClickedBoard= false;
    Boolean isClickedPlayACard= false;
    Boolean isClickedDeck =false;
    Boolean isClickedGetACard= false;
    Boolean toggle1= true, toggle2=true, toggle3= true, toggleR1=true , toggleG1=true , toggleR2= true, toggleG2 =true,toggleR3= true, toggleG3 = true, tooggleMain= true;
    Boolean isFront_toServer;
    Integer cardOnHandIndex_toServer=null,
    getResourceCardIndex_toServer=null,
    getGoldCardIndex_toServer=null,
    putACardX_toServer=null,
    putACardY_toServer=null,
    code_toServer=null;
    Boolean isError_playCard= false, isError_getCard=false;


    @FXML
    ImageView cardOnHandBackground, boardBrown, firstCardOnHand, secondCardOnHand, thirdCardOnHand,
            deckBackground, pointTable, firstResourceCard, secondResourceCard, kingdomResourceDeck,
            firstGoldCard, secondGoldCard, kingdomGoldDeck, firstTargetCard, secondTargetCard, backTargetCard,boardTmpImage, boardTmpImageBack;
    @FXML
    Label gameStatus;
    @FXML
    Button button, playACard, getACard;
    @FXML
    GridPane gridPane;
    @FXML
    CheckBox setBack;
    @FXML
    ChoiceBox<String> chatChoice;
    @FXML
    TextField chatTextField;

    @FXML
   public void playCardButton_toServer(ActionEvent e) throws RemoteException {
        isClickedPlayACard=true;
        String nickname=getGuiApplication().getGui().getUsername();
        code_toServer=searchCode.get(cardsOnHandImages.get(cardOnHandIndex_toServer));
        System.out.println("Print cardOnHand:"+ cardOnHandIndex_toServer+"is Front:"+ isFront_toServer);
        Player myPlayer =getGuiApplication().getGui().getMyMatch().getPlayerByNickname(getGuiApplication().getGui().getUsername());
        for(Card c: myPlayer.getCardOnHand()){
            System.out.println("code:"+ c.getCode()+"isFront"+c.getIsFront());
        }
        getGuiApplication().getGui().notify(new playCardMessage(nickname, cardOnHandIndex_toServer,isFront_toServer, putACardX_toServer-40,-(putACardY_toServer-40)));
        playACard.setDisable(true);
    }
    public void getACardButton_toServer(ActionEvent e) throws RemoteException {
        isClickedGetACard= true;
        String nickname =getGuiApplication().getGui().getUsername();
        Integer  gameId= getGuiApplication().getGui().getMatchID();
        if(getGoldCardIndex_toServer!=null){
            getGuiApplication().getGui().notify(new drawCardMessage(nickname,gameId, true, getGoldCardIndex_toServer ));
        }else{
            getGuiApplication().getGui().notify(new drawCardMessage(nickname,gameId, false, getResourceCardIndex_toServer ));
        }
    }
    @FXML
    public void setBackCard(){
        if(cardOnHandIndex_toServer!=null) {
            if (setBack.isSelected()) {
                boardTmpImage.setVisible(false);
                boardTmpImageBack.setVisible(true);
                isFront_toServer = false;
            } else {
                boardTmpImageBack.setVisible(false);
                boardTmpImage.setVisible(true);
                isFront_toServer = true;
            }
        }
    }


    private void showCardOnHand(ArrayList<Card> cardOnHand){
        if(cardOnHand.size()==2){
            thirdCardOnHand.setVisible(false);
        }
        Image myImage5;
        Integer code;
        int numCard =1;
        for(Card c : cardOnHand) {
            code=c.getCode();
            if(c.isGoldCard()) {
                myImage5 = new Image(getClass().getResourceAsStream("/images/cards/GoldCardFront(" + code + ").jpg"));
            }
            else {
                myImage5 = new Image(getClass().getResourceAsStream("/images/cards/ResourceCardFront(" + code + ").jpg"));
            }
            if(numCard==1){
                firstCardOnHand.setImage(myImage5);
                searchCode.put(firstCardOnHand,code);
                firstCardOnHand.setCursor(Cursor.HAND);
                firstCardOnHand.setEffect(null);

                    firstCardOnHand.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                if(toggle1) {
                                    isClickedCardOnHand = true;
                                    cardOnHandIndex_toServer = 0;
                                    secondCardOnHand.setDisable(true);
                                    thirdCardOnHand.setDisable(true);
                                    light(firstCardOnHand);
                                    gameStatus.setText("CHOOSE WHERE TO PUT THE CARD ON THE BOARD");
                                    gameStatus.setTextFill(Color.WHITE);
                                }
                                else{
                                    isClickedCardOnHand = false;
                                    cardOnHandIndex_toServer = null;
                                    secondCardOnHand.setDisable(false);
                                    thirdCardOnHand.setDisable(false);
                                    firstCardOnHand.setEffect(null);
                                    gameStatus.setText("CHOOSE ONE OF YOUR CARDS ON HAND");
                                    gameStatus.setTextFill(Color.WHITE);

                                }
                                toggle1 =!toggle1 ;
                            }
                        }

                    });
            }else if(numCard==2){
                secondCardOnHand.setImage(myImage5);
                searchCode.put(secondCardOnHand, code);
                secondCardOnHand.setCursor(Cursor.HAND);
                secondCardOnHand.setEffect(null);
                secondCardOnHand.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if(toggle2) {
                                isClickedCardOnHand = true;
                                cardOnHandIndex_toServer = 1;
                                thirdCardOnHand.setDisable(true);
                                firstCardOnHand.setDisable(true);
                                light(secondCardOnHand);
                                gameStatus.setText("CHOOSE WHERE TO PUT THE CARD ON THE BOARD");
                                gameStatus.setTextFill(Color.WHITE);
                            }else{
                                isClickedCardOnHand = false;
                                cardOnHandIndex_toServer = null;
                                thirdCardOnHand.setDisable(false);
                                firstCardOnHand.setDisable(false);
                                secondCardOnHand.setEffect(null);
                                gameStatus.setText("CHOOSE ONE OF YOUR CARDS ON HAND");
                                gameStatus.setTextFill(Color.WHITE);

                                //disillumino
                            }
                            toggle2=!toggle2;
                        }
                    }

                });
            } else if (numCard==3){
                thirdCardOnHand.setImage(myImage5);
                searchCode.put(thirdCardOnHand, code);
                thirdCardOnHand.setCursor(Cursor.HAND);
                thirdCardOnHand.setEffect(null);
                thirdCardOnHand.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if(toggle3) {
                                isClickedCardOnHand = true;
                                firstCardOnHand.setDisable(true);
                                secondCardOnHand.setDisable(true);
                                cardOnHandIndex_toServer = 2;
                                light(thirdCardOnHand);
                                gameStatus.setText("CHOOSE WHERE TO PUT THE CARD ON THE BOARD");
                                gameStatus.setTextFill(Color.WHITE);
                            }else{
                                isClickedCardOnHand = false;
                                firstCardOnHand.setDisable(false);
                                secondCardOnHand.setDisable(false);
                                cardOnHandIndex_toServer = null;
                                thirdCardOnHand.setEffect(null);
                                gameStatus.setText("CHOOSE ONE OF YOUR CARDS ON HAND");
                                gameStatus.setTextFill(Color.WHITE);

                            }
                            toggle3=!toggle3;
                        }
                    }

                });
            }
            numCard++;
        }
    }

    private void setUpGridPane() {

    }

    private void resetDeck(){
        firstResourceCard.setImage(null);
        secondResourceCard.setImage(null);
        kingdomResourceDeck.setImage(null);
        firstGoldCard.setImage(null);
        secondGoldCard.setImage(null);
        kingdomGoldDeck.setImage(null);
    }
    private void showDecks(ArrayList<GoldCard> goldDeck, ArrayList<ResourceCard> resourceDeck){

        for(int i=0; i<3; i++){
            Image image9 = null;
            Image image10=null;
            if(i==0 ||i ==1) {
                image9 = new Image(getClass().getResourceAsStream("/images/cards/GoldCardFront(" + goldDeck.get(i).getCode() + ").jpg"));
                image10 = new Image(getClass().getResourceAsStream("/images/cards/ResourceCardFront(" + resourceDeck.get(i).getCode() + ").jpg"));
            }else{
                if(goldDeck.get(i).getKingdom().equals(Elements.INSECT))
                    image9= new Image(getClass().getResourceAsStream("/images/cards/InsectBackGold.jpg"));
                else if (goldDeck.get(i).getKingdom().equals(Elements.ANIMALS))
                    image9= new Image(getClass().getResourceAsStream("/images/cards/AnimalBackGold.jpg"));
                else if (goldDeck.get(i).getKingdom().equals(Elements.MUSHROOMS)) {
                    System.out.println("entrato");
                    image9= new Image(getClass().getResourceAsStream("/images/cards/MushroomBackGold.jpg"));
                }
                else if(goldDeck.get(i).getKingdom().equals(Elements.VEGETAL))
                    image9 =new Image(getClass().getResourceAsStream("/images/cards/VegetalBackGold.jpg"));

                if(resourceDeck.get(i).getKingdom().equals(Elements.INSECT))
                    image10= new Image(getClass().getResourceAsStream("/images/cards/InsectBack.jpg"));
                else if (resourceDeck.get(i).getKingdom().equals(Elements.ANIMALS))
                    image10= new Image(getClass().getResourceAsStream("/images/cards/AnimalBack.jpg"));
                else if (resourceDeck.get(i).getKingdom().equals(Elements.MUSHROOMS)) {
                    image10 = new Image(getClass().getResourceAsStream("/images/cards/MushroomBack.jpg"));
                }
                else if(resourceDeck.get(i).getKingdom().equals(Elements.VEGETAL))
                    image10 =new Image(getClass().getResourceAsStream("/images/cards/VegetalBack.jpg"));
            }
            if(i==0){
                firstGoldCard.setImage(image9);
                firstGoldCard.setCursor(Cursor.HAND);
                firstGoldCard.setEffect(null);

                firstGoldCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if(toggleG1) {
                                getGoldCardIndex_toServer = 0;
                                isClickedDeck = true;
                                disableDecksEXCEPTone(firstGoldCard);
                                light(firstGoldCard);
                                gameStatus.setText("CLICK THE BUTTON -GET A CARD");
                                gameStatus.setTextFill(Color.WHITE);
                            }else{
                                getGoldCardIndex_toServer = null;
                                isClickedDeck = false ;
                                ableDecks();
                                firstGoldCard.setEffect(null);
                                gameStatus.setText("CHOOSE A CARD FROM DECKS");
                                gameStatus.setTextFill(Color.WHITE);
                            }
                            toggleG1= !toggleG1;
                        }
                    }

                });
                firstResourceCard.setImage(image10);
                firstResourceCard.setCursor(Cursor.HAND);
                firstResourceCard.setEffect(null);
                firstResourceCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if(toggleR1) {
                                isClickedDeck = true;
                                getResourceCardIndex_toServer = 0;
                                disableDecksEXCEPTone(firstResourceCard);
                                light(firstResourceCard);
                                gameStatus.setText("CLICK THE BUTTON -GET A CARD");
                                gameStatus.setTextFill(Color.WHITE);
                            }else {
                                getResourceCardIndex_toServer = null;
                                isClickedDeck = false;
                                ableDecks();
                                firstResourceCard.setEffect(null);
                                gameStatus.setText("CHOOSE A CARD FROM DECKS");
                                gameStatus.setTextFill(Color.WHITE);
                            }
                            toggleR1 =! toggleR1 ;
                        }
                    }

                });

            }else if(i==1){
                secondGoldCard.setImage(image9);
                secondGoldCard.setCursor(Cursor.HAND);
                secondGoldCard.setEffect(null);

                secondGoldCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if(toggleG2) {
                                isClickedDeck = true;
                                getGoldCardIndex_toServer = 1;
                                disableDecksEXCEPTone(secondGoldCard);
                                light(secondGoldCard);
                                gameStatus.setText("CLICK THE BUTTON -GET A CARD");
                                gameStatus.setTextFill(Color.WHITE);
                            }else{
                                isClickedDeck=false;
                                getGoldCardIndex_toServer= null;
                                ableDecks();
                                secondGoldCard.setEffect(null);
                                gameStatus.setText("CHOOSE A CARD FROM DECKS");
                                gameStatus.setTextFill(Color.WHITE);
                            }
                            toggleG2=!toggleG2;
                        }
                    }

                });
                secondResourceCard.setImage(image10);
                secondResourceCard.setCursor(Cursor.HAND);
                secondResourceCard.setEffect(null);

                secondResourceCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        if (event.getButton() == MouseButton.PRIMARY) {
                            if(toggleR2) {
                                isClickedDeck = true;
                                getResourceCardIndex_toServer = 1;
                                disableDecksEXCEPTone(secondResourceCard);
                                light(secondResourceCard);
                                gameStatus.setText("CLICK THE BUTTON -GET A CARD");
                                gameStatus.setTextFill(Color.WHITE);
                            }else {
                                isClickedDeck = false;
                                getResourceCardIndex_toServer = null;
                                ableDecks();
                                secondResourceCard.setEffect(null);
                                gameStatus.setText("CHOOSE A CARD FROM DECKS");
                                gameStatus.setTextFill(Color.WHITE);
                            }
                            toggleR2=!toggleR2;
                        }
                    }

                });
            }else if(i==2){
                kingdomGoldDeck.setImage(image9);
                kingdomGoldDeck.setCursor(Cursor.HAND);
                kingdomGoldDeck.setEffect(null);
                kingdomGoldDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if(toggleG3) {
                                isClickedDeck = true;
                                getGoldCardIndex_toServer = 2;
                                disableDecksEXCEPTone(kingdomGoldDeck);
                                light(kingdomGoldDeck);
                                gameStatus.setText("CLICK THE BUTTON -GET A CARD");
                                gameStatus.setTextFill(Color.WHITE);
                            }else{
                                isClickedDeck=false;
                                getGoldCardIndex_toServer=null;
                                ableDecks();
                                kingdomGoldDeck.setEffect(null);
                                gameStatus.setText("CHOOSE A CARD FROM DECKS");
                                gameStatus.setTextFill(Color.WHITE);
                            }
                            toggleG3=! toggleG3;
                        }
                    }

                });
                kingdomResourceDeck.setImage(image10);
                kingdomResourceDeck.setCursor(Cursor.HAND);
                kingdomResourceDeck.setEffect(null);

                kingdomResourceDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if(toggleR2) {
                                isClickedDeck = true;
                                getResourceCardIndex_toServer = 2;
                                disableDecksEXCEPTone(kingdomResourceDeck);
                                light(kingdomResourceDeck);
                            }else{
                                isClickedDeck=false;
                                getResourceCardIndex_toServer =null;
                                ableDecks();
                                kingdomResourceDeck.setEffect(null);
                                gameStatus.setText("CHOOSE A CARD FROM DECKS");
                                gameStatus.setTextFill(Color.WHITE);
                            }
                            toggleR2=!toggleR2;
                        }
                    }

                });
            }
        }

    }

    private void showBoard(Board b) throws IOException {
        BiMap<Card, Coordinate> map= b.getCardCoordinate();
        for(BiMap.Entry<Card, Coordinate> entry: map.entrySet()){
            Card card = entry.getKey();
            Coordinate coo= entry.getValue();
            System.out.println("codice di carta messa sul board "+card.getCode()+
            "le sue coordinate sono x:"+(coo.getX()+40)+"y:"+(-coo.getY()+40));

            Platform.runLater(()-> {
                try {
                    gridPane.add(createImageView(card), coo.getX()+40, (-coo.getY()+40));

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private ImageView createImageView(Card card) throws IOException {
        Image image = null;
        ImageView imageView= new ImageView();
        if(card.getIsFront())
            image =new Image(getClass().getResourceAsStream(getPathByCardID(card.getCode(), true)));
        else
            image= new Image(getClass().getResourceAsStream(getPathByCardID(card.getCode(), false)));

        imageView.setImage(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(146);
        imageView.setDisable(true);
        return imageView;
    }

    @Override
    public void updateModel(UPDATE update) throws IOException {
        switch (update){
            case CHATMESSAGE ->{
                showNewMessage();
            }
        }
        ViewModel model = getGuiApplication().getGui().getMyMatch();
        Player myPlayer =getGuiApplication().getGui().getMyMatch().getPlayerByNickname(getGuiApplication().getGui().getUsername());
        ArrayList<Card> cardOnHand = (ArrayList<Card>) myPlayer.getCardOnHand();
        ArrayList<GoldCard> goldDeck = model.getGoldDeck();
        ArrayList<ResourceCard> resourceDeck =  model.getResourceDeck();
        showDecks(goldDeck,resourceDeck);
        showCardOnHand(cardOnHand);
        showBoard(myPlayer.getBoard());

        if(!model.getCurrentPlayer().getNickname().equals(getGuiApplication().getGui().getUsername())){
            if(!isError_getCard&&!tooggleMain&&initialized&&isClickedGetACard){
                thirdCardOnHand.setVisible(true);
                tooggleMain=true;
                isClickedGetACard=false;
            }
            // disabilito gioco carta
            gridPane.setDisable(true);
            playACard.setDisable(true);
            setBack.setDisable(true);
            disableCardOnHand();

            // disabilito pesca carta
            disableDecks();
            getACard.setDisable(true);
        }else{
            //se sono nel mio turno
            //abilito gioco carta
            gridPane.setDisable(false);
            ableCardOnHand();
            if(!isError_playCard&&initialized&& tooggleMain && isClickedPlayACard){
                gameStatus.setText("CHOOSE A CARD ON DECKS");

                cardsOnHandImages.get(cardOnHandIndex_toServer).setVisible(true);
                cardsOnHandImages.get(cardOnHandIndex_toServer).setEffect(null);

                switch (cardOnHandIndex_toServer){
                    case 0:
                        toggle1=!toggle1;
                        break;
                    case 1:
                        toggle2=! toggle2;
                        break;
                    case 2:
                        toggle3=! toggle3;
                        break;
                }
                cardOnHandIndex_toServer=null ;

                if(setBack.isSelected())
                    setBack.setSelected(false);

                isFront_toServer= null;
                putACardX_toServer=null;
                putACardY_toServer=null;
                playACard.setDisable(true);
                ableDecks();
                disableCardOnHand();
                getACard.setDisable(false);
                gridPane.setDisable(true);
                firstCardOnHand.setVisible(true);
                tooggleMain= false;
                boardTmpImage = new ImageView();
                boardTmpImageBack = new ImageView();
                setBack.setDisable(true);
                gameStatus.setText("CHOOSE A CARD FROM DECKS");
                gameStatus.setTextFill(Color.WHITE);

                isClickedPlayACard=false;
            }

            // abilito  pesca carta
        }
        boardTmpImageBack = new ImageView();
        boardTmpImage= new ImageView();
        setBack.setDisable(true);

        if(!initialized){
            disableDecks();
            ArrayList<TargetCard> commonTarget = model.getCommonTarget();
            TargetCard targetCard= myPlayer.getTarget();
            setUpBoard(commonTarget,targetCard );
            gameStatus.setText("CHOOSE ONE OF YOUR CARDS ON HAND");
            gameStatus.setTextFill(Color.WHITE);

            for(int i= 0; i<82; i++)
                for(int j=0; j<82; j++) {
                    if (((i+j)%2 == 0) &&!(i==40 &&j==40)){
                        //add heart
                        ImageView imageViewHeart = new ImageView();
                        imageViewHeart.setImage(new Image(getClass().getResourceAsStream("/images/view/heart.png")));
                        imageViewHeart.setCursor(Cursor.HAND);
                        imageViewHeart.setFitWidth(15);
                        imageViewHeart.setFitHeight(15);
                        StackPane stackPane= new StackPane(imageViewHeart);
                        stackPane.setAlignment(Pos.CENTER);
                        gridPane.add(stackPane, i, j);

                        //what happens if you click the heart
                        imageViewHeart.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (event.getButton() == MouseButton.PRIMARY && isClickedCardOnHand){
                                    //abilito la casella
                                    setBack.setDisable(false);
                                    isClickedBoard=true;
                                    gameStatus.setText("CLICK THE BUTTON -PLAY CARD- or MOVE THE CARD ON THE BOARD");
                                    gameStatus.setTextFill(Color.WHITE);

                                    // rendo invisibile le carte che ho in mano
                                    cardsOnHandImages.get(cardOnHandIndex_toServer).setDisable(true);
                                    cardsOnHandImages.get(cardOnHandIndex_toServer).setVisible(false);

                                    // ora puoi cliccare il bottone di giocare una carta
                                    playACard.setDisable(false);

                                    // search which heart is clicked
                                    ImageView imageClicked = (ImageView) event.getSource();
                                    StackPane stackPane= (StackPane) imageClicked.getParent();
                                    Integer columnIndex = GridPane.getColumnIndex(stackPane);
                                    Integer rowIndex = GridPane.getRowIndex(stackPane);

                                    // after you first put of card you can move it on the board
                                    if(putACardY_toServer!=rowIndex || putACardX_toServer !=columnIndex ){
                                        gridPane.getChildren().remove(boardTmpImage);
                                        gridPane.getChildren().remove(boardTmpImageBack);
                                    }
                                    putACardX_toServer= columnIndex == null ? 0 : columnIndex;
                                    putACardY_toServer = rowIndex == null ? 0 : rowIndex;


                                    //add frontCard
                                    boardTmpImage.setImage(cardsOnHandImages.get(cardOnHandIndex_toServer).getImage());
                                    boardTmpImage.setFitWidth(146);
                                    boardTmpImage.setFitHeight(100);
                                    boardTmpImage.setCursor(Cursor.HAND);
                                    gridPane.add(boardTmpImage, putACardX_toServer, putACardY_toServer);
                                    Player myPlayer =getGuiApplication().getGui().getMyMatch().getPlayerByNickname(getGuiApplication().getGui().getUsername());
                                    List <Card> cardOnHand =  myPlayer.getCardOnHand();

                                    // add back card
                                    Image image = null;
                                    switch (cardOnHand.get(cardOnHandIndex_toServer).getKingdom()) {
                                        case Elements.ANIMALS:
                                            if(cardOnHand.get(cardOnHandIndex_toServer)instanceof GoldCard)
                                                image = new Image(getClass().getResourceAsStream("/images/cards/AnimalBackGold.jpg"));
                                            else
                                                image=  new Image(getClass().getResourceAsStream("/images/cards/AnimalBack.jpg"));
                                            break;
                                        case Elements.MUSHROOMS:
                                            if(cardOnHand.get(cardOnHandIndex_toServer)instanceof  GoldCard)
                                                image = new Image(getClass().getResourceAsStream("/images/cards/MushroomBackGold.jpg"));
                                            else
                                                image = new Image(getClass().getResourceAsStream("/images/cards/MushroomBack.jpg"));
                                            break;
                                        case Elements.INSECT:
                                            if(cardOnHand.get(cardOnHandIndex_toServer)instanceof  GoldCard)
                                                image = new Image(getClass().getResourceAsStream("/images/cards/InsectBackGold.jpg"));
                                            else
                                                image = new Image(getClass().getResourceAsStream("/images/cards/InsectBack.jpg"));
                                            break;
                                        case Elements.VEGETAL:
                                            if(cardOnHand.get(cardOnHandIndex_toServer)instanceof  GoldCard)
                                                image = new Image(getClass().getResourceAsStream("/images/cards/VegetalBackGold.jpg"));
                                            else
                                                image=  new Image(getClass().getResourceAsStream("/images/cards/VegetalBack.jpg"));
                                            break;
                                    }
                                    boardTmpImageBack.setImage(image);
                                    boardTmpImageBack.setFitHeight(100);
                                    boardTmpImageBack.setFitWidth(146);
                                    gridPane.add(boardTmpImageBack, putACardX_toServer, putACardY_toServer);

                                    // in base alla casella decido quale carta mostrare
                                    if(!setBack.isSelected()) {
                                        boardTmpImageBack.setVisible(false);
                                        boardTmpImage.setVisible(true);
                                        isFront_toServer=true;
                                    }
                                    else {
                                        boardTmpImageBack.setVisible(true);
                                        boardTmpImage.setVisible(false);
                                        isFront_toServer=false;
                                    }

                                    // se clico un' altra volta una carta front del board
                                    boardTmpImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            gameStatus.setText("CHOOSE ONE OF YOUR CARDS ON HAND");
                                            gameStatus.setTextFill(Color.WHITE);

                                            if (event.getButton() == MouseButton.PRIMARY && isClickedCardOnHand) {
                                                boardTmpImage.setVisible(false);
                                                isFront_toServer=null;
                                                isClickedCardOnHand=false;

                                                setBack.setDisable(true);
                                                playACard.setDisable(true);
                                                isClickedBoard=false;

                                                // rendo visibile la carta in mano
                                                for (ImageView ima : cardsOnHandImages){
                                                    ima.setVisible(true);
                                                    ima.setDisable(false);
                                                }
                                                cardsOnHandImages.get(cardOnHandIndex_toServer).setEffect(null);
                                                switch (cardOnHandIndex_toServer){
                                                    case 0:
                                                        toggle1=!toggle1;
                                                        break;
                                                    case 1:
                                                        toggle2=! toggle2;
                                                        break;
                                                    case 2:
                                                        toggle3=! toggle3;
                                                        break;
                                                }

                                                cardOnHandIndex_toServer=null;

                                                // rimnuovo le carte sul board
                                                gridPane.getChildren().remove(boardTmpImage);
                                                gridPane.getChildren().remove(boardTmpImageBack);


                                                // inizializzo boardTmpImage
                                                boardTmpImage = new ImageView();
                                                boardTmpImageBack = new ImageView();
                                            }

                                        }
                                    });
                                }
                            }
                        });


                    }
                }
            initializeChatChoice(model.getPlayers());
            if (!getGuiApplication().getGui().getChat().isEmpty()){
                showAllChat();
            }
            initialized =true;
        }}
        }
        //todo update all the scene with the information of the model
    }

    private void showAllChat() {
        for(ServerChatMessage msg : getGuiApplication().getGui().getChat()){
            showChatMessage(msg);
        }
    }

    private void showNewMessage() {
        showChatMessage(getGuiApplication().getGui().getChat().getLast());
    }
    private void showChatMessage(ServerChatMessage msg){
        Color color = Color.WHITE;
        String channel;
        if (msg.isForAll()){
            channel ="[Public]";
        }else {
            channel ="[Private]";
            color = Color.LIGHTGREEN;
        }
        String fromPlayer = msg.getFromPlayer();
        if (Objects.equals(fromPlayer, getGuiApplication().getGui().getUsername())){
            fromPlayer = "You";
        }
        Text chatMessage = new Text(channel+"["+fromPlayer+"]:"+msg.getChatMsg());
        chatMessage.setFill(color);
        chatMessage.getStyleClass().add("chatText");
       Platform.runLater(()->chatVBox.getChildren().add(chatMessage));
    }

    @Override
    public void ShowErrorMessage(String string){
        gameStatus.setText(string);
        gameStatus.setTextFill(Color.RED);
        if(isClickedPlayACard) {
            cardsOnHandImages.get(cardOnHandIndex_toServer).setVisible(true);
            gridPane.getChildren().remove(boardTmpImage);
            gridPane.getChildren().remove(boardTmpImageBack);
            isError_playCard = false;
            ableCardOnHand();
            isClickedDeck=false;
            getResourceCardIndex_toServer=null;
            getGoldCardIndex_toServer =null;
            ableDecks();
            tooggleMain=true;
            setBack.setDisable(true);
        }
         else if(isClickedGetACard){
        isError_getCard = true;
        }
    }

    private void light(ImageView imageView) {
        Light.Distant light = new Light.Distant();
        light.setAzimuth(45); // Angolo della luce
        light.setElevation(30); // Elevazione della luce

        // Applica l'effetto di illuminazione
        Lighting lighting = new Lighting();
        lighting.setLight(light);

        // Applica l'effetto all'immagine
        imageView.setEffect(lighting);
    }

    private void setUpBoard(ArrayList<TargetCard> commonTarget,TargetCard personalTarget)
    {
        Image myImage1 = new Image(getClass().getResourceAsStream("/images/view/playGround.jpg"));
        Image myImage2 =new Image(getClass().getResourceAsStream("/images/view/transparentTabCoh.png"));
        Image myImage3 =new Image(getClass().getResourceAsStream("/images/view/transparentTabDeck.png"));
        Image myImage4 =new Image(getClass().getResourceAsStream("/images/view/pointTableBottom.png"));
        Image myImage6= new Image(getClass().getResourceAsStream("/images/cards/TargetCardFront("+commonTarget.get(0).getIdCard()+").jpg"));
        Image myImage7= new Image(getClass().getResourceAsStream("/images/cards/TargetCardFront("+commonTarget.get(1).getIdCard()+").jpg"));
        Image myImage8= new Image(getClass().getResourceAsStream("/images/cards/TargetCardFront("+personalTarget.getIdCard()+").jpg"));
        boardBrown.setImage(myImage1);
        cardOnHandBackground.setImage(myImage2);
        deckBackground.setImage(myImage3);
        pointTable.setImage(myImage4);
        pointTable.setCursor(Cursor.HAND);
        firstTargetCard.setImage(myImage6);
        secondTargetCard.setImage(myImage7);
        backTargetCard.setImage(myImage8);
        decksImages.add(firstGoldCard);
        decksImages.add(firstResourceCard);
        decksImages.add(secondGoldCard);
        decksImages.add(secondResourceCard);
        decksImages.add(kingdomGoldDeck);
        decksImages.add(kingdomResourceDeck);
        cardsOnHandImages.add(firstCardOnHand);
        cardsOnHandImages.add(secondCardOnHand);
        cardsOnHandImages.add(thirdCardOnHand);

    }

    private void disableCardOnHand(){
        for(ImageView imageView :cardsOnHandImages){
            imageView.setDisable(true);
        }
    }

    private void ableCardOnHand(){
        for(ImageView imageView :cardsOnHandImages){
            imageView.setDisable(false);
        }
    }


    private void ableDecks(){
        firstResourceCard.setDisable(false);
        secondResourceCard.setDisable(false);
        kingdomResourceDeck.setDisable(false);
        firstGoldCard.setDisable(false);
        secondGoldCard.setDisable(false);
        kingdomGoldDeck.setDisable(false);
    }
    private void disableDecks(){
        firstResourceCard.setDisable(true);
        secondResourceCard.setDisable(true);
        kingdomResourceDeck.setDisable(true);
        firstGoldCard.setDisable(true);
        secondGoldCard.setDisable(true);
        kingdomGoldDeck.setDisable(true);

    }

    private void disableDecksEXCEPTone(ImageView imageView){
        for(ImageView c: decksImages){
            if(!c.equals(imageView)){
                c.setDisable(true);
            }
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatDestination=null;
        chatTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER){
                try {
                    sendChatMessage();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    //chat methods
    private void initializeChatChoice(List<Player> players){
        String client = getGuiApplication().getGui().getUsername();
        for (Player p :players){
            if (!Objects.equals(p.nickname, client)){
                chatChoice.getItems().addAll(p.nickname);
            }
        }
        chatChoice.getItems().add("Public");
        chatChoice.setValue("Public");
    }


    private void sendChatMessage() throws RemoteException {
        boolean isForAll = false;

        chatDestination =chatChoice.getValue();

        if (chatDestination!=null && !chatTextField.getText().isEmpty()){
            if(chatDestination.equals("Public"))
                isForAll=true;
            else{
                showChatMessage(new ServerChatMessage(new ClientChatMessage(0,"You",isForAll,chatDestination,chatTextField.getText())));
            }
            getGuiApplication().getGui().notify(new ClientChatMessage(0,"",isForAll,chatDestination,chatTextField.getText()));
            chatTextField.clear();
        }
    }
}
