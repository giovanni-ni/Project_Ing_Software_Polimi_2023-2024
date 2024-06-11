package it.polimi.ingsw.view.Gui.SceneControllers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import it.polimi.ingsw.Message.ClientToServerMsg.ClientChatMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.drawCardMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.playCardMessage;
import it.polimi.ingsw.Message.ServerToClientMsg.ServerChatMessage;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.Gui.SceneControllers.ptPositions.PTPOSITION;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;

import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

import static it.polimi.ingsw.utils.pathSearch.getPathByCard;
import static it.polimi.ingsw.utils.pathSearch.getPathByCardID;


public class BoardController extends GenericSceneController implements Initializable {

    @FXML
    public Text n_mushroom;
    @FXML
    public Text n_insect;
    @FXML
    public Text n_animals;
    @FXML
    public Text n_vegetal;
    @FXML
    public Text n_parchment;
    @FXML
    public Text n_ink;
    @FXML
    public Text n_feather;
    @FXML
    public VBox chatVBox, winVbox;

    private HashBiMap<PlayerColor, String> nickList;
    private String chatDestination;
    private ArrayList<ImageView> decksImages = new ArrayList<>();
    private ArrayList<ImageView> cardsOnHandImages = new ArrayList<>();
    private ArrayList<ImageView> backCardsOnHandImages_BLUE = new ArrayList<>();
    private ArrayList<ImageView> backCardsOnHandImages_RED = new ArrayList<>();
    private ArrayList<ImageView> backCardsOnHandImages_YELLOW= new ArrayList<>();
    private ArrayList<ImageView> backCardsOnHandImages_GREEN = new ArrayList<>();
    //todo delete unuseful elements
    private HashMap<ImageView, Integer> searchCode= new HashMap<>();
    Boolean initialized =false;
    Boolean isClickedCardOnHand=false;
    Boolean isClickedBoard= false;
    Boolean isClickedPlayACard= false;
    Boolean isClickedDeck =false;
    Boolean isClickedGetACard= false;
    Boolean toggle1= true, toggle2=true, toggle3= true, toggleR1=true , toggleG1=true , toggleR2= true, toggleG2 =true, toggleG3 = true, tooggleMain= true;
    Boolean isFront_toServer;
    Integer cardOnHandIndex_toServer=null,
    getResourceCardIndex_toServer=null,
    getGoldCardIndex_toServer=null,
    putACardX_toServer=null,
    putACardY_toServer=null;
    Boolean isError_playCard= false, isError_getCard=false;

    private BiMap<PlayerColor,StackPane> paneMap;
    private BiMap<PlayerColor,Board> playerBoards;
    private BiMap<PlayerColor,GridPane> playerGrids;
    private BiMap<PlayerColor,ImageView> playerColorMap;
    @FXML
    ImageView cardOnHandBackground, boardBrown, firstCardOnHand, secondCardOnHand, thirdCardOnHand,
            deckBackground, pointTable, firstResourceCard, secondResourceCard, kingdomResourceDeck,
            firstGoldCard, secondGoldCard, kingdomGoldDeck, firstTargetCard, secondTargetCard, backTargetCard,boardTmpImage, boardTmpImageBack,
            blue, red, yellow, green,firstCardOnHand_BLUE,firstCardOnHand_RED,firstCardOnHand_YELLOW,firstCardOnHand_GREEN,secondCardOnHand_BLUE,
            secondCardOnHand_RED,secondCardOnHand_YELLOW,secondCardOnHand_GREEN,thirdCardOnHand_BLUE,thirdCardOnHand_RED,thirdCardOnHand_YELLOW,thirdCardOnHand_GREEN,biggerImg;
    @FXML
    Label gameStatus;
    @FXML
    Button  playACard, getACard;
    @FXML
    GridPane gridPane, gridPane2, gridPane3, gridPane4, myGrid;
    @FXML
    CheckBox setBack;
    @FXML
    ChoiceBox<String> chatChoice;
    @FXML
    TextField chatTextField, nRed, nBlue, nGreen, nYellow;
    @FXML
    TextField rCard1, rCard2, gCard1, gCard2, rDeckText, gDeckText, cTarget1, cTarget2, pTarget;
    @FXML
    Text p1nick,p2nick,p3nick,p4nick,p1nick1,p2nick1,p3nick1,p4nick1, playerID, winText;

    @FXML
    StackPane ptPane,loadPane, bluePane,redPane, yellowPane, greenPane,bigImgPane, winPane;
    @FXML
    ScrollPane scrollPane1, scrollPane2, scrollPane3, scrollPane4;
    @FXML
    AnchorPane ptAnchor;

    @FXML
   public void playCardButton_toServer(ActionEvent e) throws RemoteException {
        isClickedPlayACard=true;
        String nickname=getGuiApplication().getGui().getUsername();
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


    private void showBackCards(ArrayList<ImageView> imagesList, Player p)throws IOException{
        ArrayList<Card> backCardOnHand= (ArrayList<Card>) p.getCardOnHand();
        for(int i =0 ;i<p.getCardOnHand().size();i++){
            ImageView imageView;
            imageView=imagesList.get(i);
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(getPathByBackCard(backCardOnHand.get(i),false)))));
            imageView.setVisible(false);
        }
        //if a player played a card, I do not show his thirdCardOnHand
        if(backCardOnHand.size()==2){
            imagesList.get(2).setImage(null);
        }
    }

    private void showCardOnHand(ArrayList<Card> cardOnHand) throws IOException {
        for (ImageView imageView : cardsOnHandImages){
            imageView.setVisible(false);
        }

        for(int i =0 ;i<cardOnHand.size();i++){
            ImageView imageView;
            cardOnHand.get(i).setFront(true);
            imageView=cardsOnHandImages.get(i);
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(getPathByCard(cardOnHand.get(i))))));
            searchCode.put(imageView,cardOnHand.get(i).getCode()); // search code is used for?
            imageView.setVisible(true);
        }
    }

    private void showDecks(ArrayList<GoldCard> goldDeck, ArrayList<ResourceCard> resourceDeck) throws IOException {
        int i;
        // show resource deck images
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

    private void showBoard(Board b, Player p) throws IOException {
        Board targetBoard = playerBoards.get(p.getPlayerID());
        GridPane targetGrid = playerGrids.get(p.getPlayerID());
        BiMap<Card, Coordinate> map= b.getCardCoordinate();

        if (map!=null && (!playerBoards.containsKey(p.getPlayerID()) || playerBoards.get(p.getPlayerID()).getCardCoordinate().size()<=map.size())){

            for(BiMap.Entry<Card, Coordinate> entry: map.entrySet()){
                Card card = entry.getKey();
                Coordinate coo= entry.getValue();

                if (!playerBoards.containsKey(p.getPlayerID()) || !playerBoards.get(p.getPlayerID()).isCardCoordinate(coo.getX(), coo.getY())){

                    System.out.println("codice di carta messa sul board di " + p.nickname+" " +card.getCode()+
                            "le sue coordinate sono x: "+(coo.getX()+40)+" y: "+(-coo.getY()+40));

                    Platform.runLater(()-> {
                        try {
                            targetGrid.add(createImageView(card), coo.getX()+40, (-coo.getY()+40));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    i--;
                }

            }
            playerBoards.put(p.getPlayerID(),b);

        }

    }
//TODO animal back gold wrong
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
        ViewModel model = getGuiApplication().getGui().getMyMatch();
        switch (update) {
            case CHATMESSAGE -> {
                showNewMessage();
            }
            case LASTROUND -> {
                showChatMessage(new ServerChatMessage(new ClientChatMessage(0,"Server",true,"","THIS IS THE LAST ROUND, COME ON, GO FOR THE VICTORY")));
            }
            case ENDMESSAGE -> {
                StringBuilder winner = new StringBuilder();
                // disabilito gioco carta
                myGrid.setDisable(true);

                playACard.setDisable(true);
                setBack.setDisable(true);
                disableCardOnHand();

                // disabilito pesca carta
                disableDecks();
                getResourceCardIndex_toServer=null;
                getGoldCardIndex_toServer=null;
                getACard.setDisable(true);
                for(Player p: model.getWinners()){
                    winner.append(p.getNickname());
                    winner.append(" ");
                    Text wint = new Text(p.nickname);
                    wint.setStyle(winText.getStyle());
                    wint.setFont(winText.getFont());
                    wint.setFill(winText.getFill());
                    winVbox.getChildren().add(wint);
                }
                winVbox.getChildren().remove(winText);
                //A richiesta di Stefano Hong
                Text wint = new Text("Loser is JO77");
                wint.setStyle(winText.getStyle());
                wint.setFont(winText.getFont());
                wint.setFill(winText.getFill());
                winVbox.getChildren().add(wint);

                gameStatus.setText("WOWWW THE VICTORY BELONGS TO "+ winner);
                gameStatus.setTextFill(Color.WHITE);
                winPane.setVisible(true);
            }

            case YOURROUND -> {
                gameStatus.setText("IT'S YOUR ROUND PLAY AND DRAW CARD WITH CAUTION");
                gameStatus.setTextFill(Color.WHITE);
                lightGold(cardOnHandBackground);
            }
            default -> {

                Player myPlayer =getGuiApplication().getGui().getMyMatch().getPlayerByNickname(getGuiApplication().getGui().getUsername());
                ArrayList<Card> cardOnHand = (ArrayList<Card>) myPlayer.getCardOnHand();
                ArrayList<GoldCard> goldDeck = model.getGoldDeck();
                ArrayList<ResourceCard> resourceDeck =  model.getResourceDeck();
                n_animals.setText((myPlayer.getBoard().getCounterOfElements().get(Elements.ANIMALS)).toString());
                n_mushroom.setText((myPlayer.getBoard().getCounterOfElements().get(Elements.MUSHROOMS)).toString());
                n_feather.setText((myPlayer.getBoard().getCounterOfElements().get(Elements.FEATHER)).toString());
                n_parchment.setText((myPlayer.getBoard().getCounterOfElements().get(Elements.PARCHMENT)).toString());
                n_vegetal.setText((myPlayer.getBoard().getCounterOfElements().get(Elements.VEGETAL)).toString());
                n_insect.setText((myPlayer.getBoard().getCounterOfElements().get(Elements.INSECT)).toString());
                n_ink.setText((myPlayer.getBoard().getCounterOfElements().get(Elements.INK)).toString());

                if(!initialized) {
                    switch(myPlayer.getPlayerID()){
                        case BLUE-> {
                           myGrid =gridPane;
                        }
                        case RED -> {
                            myGrid =gridPane2;
                        }
                        case YELLOW -> {
                            myGrid =gridPane3;
                        }
                        case GREEN -> {
                            myGrid =gridPane4;
                        }
                    }
                    disableDecks();
                    ArrayList<TargetCard> commonTarget = model.getCommonTarget();
                    TargetCard targetCard = myPlayer.getTarget();
                    setUpBoard(commonTarget, targetCard);
                    gameStatus.setText("CHOOSE ONE OF YOUR CARDS ON HAND");
                    gameStatus.setTextFill(Color.WHITE);
                    lightGold(cardOnHandBackground);
                        for (int i = 0; i < 82; i++)
                            for (int j = 0; j < 82; j++) {
                                if (((i + j) % 2 == 0) && !(i == 40 && j == 40)) {
                                //add heart
                                ImageView imageViewHeart = new ImageView();
                                imageViewHeart.setImage(new Image(getClass().getResourceAsStream("/images/view/heart.png")));
                                imageViewHeart.setCursor(Cursor.HAND);
                                imageViewHeart.setFitWidth(15);
                                imageViewHeart.setFitHeight(15);
                                StackPane stackPane = new StackPane(imageViewHeart);
                                stackPane.setAlignment(Pos.CENTER);
                                myGrid.add(stackPane, i, j);

                                //what happens if you click the heart
                                imageViewHeart.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        if (event.getButton() == MouseButton.PRIMARY && isClickedCardOnHand) {
                                            //abilito la casella
                                            setBack.setDisable(false);
                                            isClickedBoard = true;
                                            gameStatus.setText("CLICK THE BUTTON PLAY CARD or MOVE THE CARD ON THE BOARD");
                                            gameStatus.setTextFill(Color.WHITE);

                                            // rendo invisibile le carte che ho in mano
                                            cardsOnHandImages.get(cardOnHandIndex_toServer).setDisable(true);
                                            cardsOnHandImages.get(cardOnHandIndex_toServer).setVisible(false);

                                            // ora puoi cliccare il bottone di giocare una carta
                                            playACard.setDisable(false);

                                            // search which heart is clicked
                                            ImageView imageClicked = (ImageView) event.getSource();
                                            StackPane stackPane = (StackPane) imageClicked.getParent();
                                            Integer columnIndex = GridPane.getColumnIndex(stackPane);
                                            Integer rowIndex = GridPane.getRowIndex(stackPane);

                                            // after you first put of card you can move it on the board
                                            if (!Objects.equals(putACardY_toServer, rowIndex) || !Objects.equals(putACardX_toServer, columnIndex)) {

                                                myGrid.getChildren().remove(boardTmpImage);
                                                myGrid.getChildren().remove(boardTmpImageBack);

                                            }
                                            putACardX_toServer = columnIndex == null ? 0 : columnIndex;
                                            putACardY_toServer = rowIndex == null ? 0 : rowIndex;


                                            //add frontCard
                                            boardTmpImage.setImage(cardsOnHandImages.get(cardOnHandIndex_toServer).getImage());
                                            boardTmpImage.setFitWidth(146);
                                            boardTmpImage.setFitHeight(100);
                                            boardTmpImage.setCursor(Cursor.HAND);

                                            myGrid.add(boardTmpImage, putACardX_toServer, putACardY_toServer);

                                            // add back card
                                            Image image = null;
                                            Player myPlayer =getGuiApplication().getGui().getMyMatch().getPlayerByNickname(getGuiApplication().getGui().getUsername());
                                            ArrayList<Card> cardOnHand = (ArrayList<Card>) myPlayer.getCardOnHand();
                                            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(getPathByCardID(cardOnHand.get(cardOnHandIndex_toServer).getCode(),false))));

                                            boardTmpImageBack.setImage(image);
                                            boardTmpImageBack.setFitHeight(100);
                                            boardTmpImageBack.setFitWidth(146);
                                            myGrid.add(boardTmpImageBack, putACardX_toServer, putACardY_toServer);

                                            // in base alla casella decido quale carta mostrare
                                            if (!setBack.isSelected()) {
                                                boardTmpImageBack.setVisible(false);
                                                boardTmpImage.setVisible(true);
                                                isFront_toServer = true;
                                            } else {
                                                boardTmpImageBack.setVisible(true);
                                                boardTmpImage.setVisible(false);
                                                isFront_toServer = false;
                                            }

                                            // se clico un' altra volta una carta front del board
                                            boardTmpImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    gameStatus.setText("CHOOSE ONE OF YOUR CARDS ON HAND");
                                                    gameStatus.setTextFill(Color.WHITE);

                                                    if (event.getButton() == MouseButton.PRIMARY && isClickedCardOnHand) {
                                                        boardTmpImage.setVisible(false);
                                                        isFront_toServer = null;
                                                        isClickedCardOnHand = false;

                                                        setBack.setDisable(true);
                                                        playACard.setDisable(true);
                                                        isClickedBoard = false;

                                                        // rendo visibile la carta in mano
                                                        for (ImageView ima : cardsOnHandImages) {
                                                            ima.setVisible(true);
                                                            ima.setDisable(false);
                                                        }
                                                        //todo got a null pointer exception here because of index
                                                        cardsOnHandImages.get(cardOnHandIndex_toServer).setEffect(null);
                                                        switch (cardOnHandIndex_toServer) {
                                                            case 0:
                                                                toggle1 = !toggle1;
                                                                break;
                                                            case 1:
                                                                toggle2 = !toggle2;
                                                                break;
                                                            case 2:
                                                                toggle3 = !toggle3;
                                                                break;
                                                        }

                                                        cardOnHandIndex_toServer = null;

                                                        // rimnuovo le carte sul board
                                                        myGrid.getChildren().remove(boardTmpImage);
                                                        myGrid.getChildren().remove(boardTmpImageBack);

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
                    initPtPane();
                    playerID.setText(myPlayer.nickname);
                    playerID.setVisible(true);

                    if (!getGuiApplication().getGui().getChat().isEmpty()){
                        showAllChat();
                    }
                    loadPane.setVisible(false);
                    loadPane.setDisable(true);
                    initialized = true;
                    yellow.setVisible(false);
                    green.setVisible(false);
                    int i = 0;
                    for (Player p : getGuiApplication().getGui().getMyMatch().getPlayers()) {
                        if (i == 0) {
                            red.setCursor(Cursor.HAND);
                        } else if (i == 1) {
                            blue.setCursor(Cursor.HAND);
                        } else if (i == 2) {
                            yellow.setVisible(true);
                            yellow.setCursor(Cursor.HAND);
                        } else if (i == 3) {
                            green.setVisible(true);
                            green.setCursor(Cursor.HAND);
                        }
                        i++;
                    }
                    blue.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {

                            scrollPane1.setVisible(true);
                            scrollPane2.setVisible(false);
                            scrollPane3.setVisible(false);
                            scrollPane4.setVisible(false);
                            // my ID is blue, show myFrontCardOnHand
                            if(getGuiApplication().getGui().getMyMatch().getPlayerByNickname(getGuiApplication().getGui().getUsername()).getPlayerID().equals(PlayerColor.BLUE)){
                                visibleCardsOnHand(cardsOnHandImages);
                            }else { //my id is not  blue show back cards
                                visibleCardsOnHand(backCardsOnHandImages_BLUE);
                                disableVisibilityCardOnHand(cardsOnHandImages);
                            }

                            disableVisibilityCardOnHand(backCardsOnHandImages_RED);
                            disableVisibilityCardOnHand(backCardsOnHandImages_YELLOW);
                            disableVisibilityCardOnHand(backCardsOnHandImages_GREEN);
                        }

                    });
                    red.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            scrollPane1.setVisible(false);
                            scrollPane2.setVisible(true);
                            scrollPane3.setVisible(false);
                            scrollPane4.setVisible(false);
                            // my ID is red, show myFrontCardOnHand
                            if(getGuiApplication().getGui().getMyMatch().getPlayerByNickname(getGuiApplication().getGui().getUsername()).getPlayerID().equals(PlayerColor.RED)){
                                visibleCardsOnHand(cardsOnHandImages);
                            }else { //my id is not  red show back cards
                                visibleCardsOnHand(backCardsOnHandImages_RED);
                                disableVisibilityCardOnHand(cardsOnHandImages);
                            }
                            disableVisibilityCardOnHand(backCardsOnHandImages_BLUE);
                            disableVisibilityCardOnHand(backCardsOnHandImages_YELLOW);
                            disableVisibilityCardOnHand(backCardsOnHandImages_GREEN);
                        }
                    });
                    for(Player p : getGuiApplication().getGui().getMyMatch().getPlayers()){

                    }
                    yellow.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            scrollPane1.setVisible(false);
                            scrollPane2.setVisible(false);
                            scrollPane3.setVisible(true);
                            scrollPane4.setVisible(false);
                            // my ID is yellow, show myFrontCardOnHand
                            if(getGuiApplication().getGui().getMyMatch().getPlayerByNickname(getGuiApplication().getGui().getUsername()).getPlayerID().equals(PlayerColor.YELLOW)){
                                visibleCardsOnHand(cardsOnHandImages);
                            }else { //my id is not  red ,show back cards
                                visibleCardsOnHand(backCardsOnHandImages_YELLOW);
                                disableVisibilityCardOnHand(cardsOnHandImages);

                            }
                            disableVisibilityCardOnHand(backCardsOnHandImages_BLUE);
                            disableVisibilityCardOnHand(backCardsOnHandImages_RED);
                            disableVisibilityCardOnHand(backCardsOnHandImages_GREEN);
                        }
                    });

                    green.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            scrollPane1.setVisible(false);
                            scrollPane2.setVisible(false);
                            scrollPane3.setVisible(false);
                            scrollPane4.setVisible(true);
                            if(getGuiApplication().getGui().getMyMatch().getPlayerByNickname(getGuiApplication().getGui().getUsername()).getPlayerID().equals(PlayerColor.GREEN)){
                                visibleCardsOnHand(cardsOnHandImages);
                            }else { //my id is not  red show back cards
                                visibleCardsOnHand(backCardsOnHandImages_GREEN);
                                disableVisibilityCardOnHand(cardsOnHandImages);

                            }

                            disableVisibilityCardOnHand(backCardsOnHandImages_BLUE);
                            disableVisibilityCardOnHand(backCardsOnHandImages_RED);
                            disableVisibilityCardOnHand(backCardsOnHandImages_YELLOW);
                        }
                    });

                }

                for(Player p: getGuiApplication().getGui().getMyMatch().getPlayers()){
                       showBoard(p.getBoard(), p);
                }
                switch(getGuiApplication().getGui().getMyMatch().getPlayerByNickname(getGuiApplication().getGui().getUsername()).getPlayerID()) {
                    case BLUE:
                        scrollPane1.setVisible(true);
                        scrollPane2.setVisible(false);
                        scrollPane3.setVisible(false);
                        scrollPane4.setVisible(false);
                        break;
                    case RED:
                        scrollPane1.setVisible(false);
                        scrollPane2.setVisible(true);
                        scrollPane3.setVisible(false);
                        scrollPane4.setVisible(false);
                        break;
                    case YELLOW:
                        scrollPane1.setVisible(false);
                        scrollPane2.setVisible(false);
                        scrollPane3.setVisible(true);
                        scrollPane4.setVisible(false);
                        break;
                    case GREEN:
                        scrollPane1.setVisible(false);
                        scrollPane2.setVisible(false);
                        scrollPane3.setVisible(false);
                        scrollPane4.setVisible(true);
                        break;
                }
                showDecks(goldDeck,resourceDeck);
                showCardOnHand(cardOnHand);
                for(Player p: getGuiApplication().getGui().getMyMatch().getPlayers()){
                    if(!p.getNickname().equals(getGuiApplication().getGui().getUsername()))
                        switch (p.getPlayerID()){
                            case BLUE -> {
                                showBackCards(backCardsOnHandImages_BLUE, p);
                            }
                            case RED -> {
                                showBackCards(backCardsOnHandImages_RED, p);
                            }
                            case YELLOW -> {
                                showBackCards(backCardsOnHandImages_YELLOW, p);
                            }
                            case GREEN -> {
                                showBackCards(backCardsOnHandImages_GREEN,p);
                            }
                        }
                }
                if(!model.getCurrentPlayer().getNickname().equals(getGuiApplication().getGui().getUsername())){
                    gameStatus.setText("WAITING OTHER PLAYERS");
                    disableAllGoldLights();
                    if(!isError_getCard&&!tooggleMain&&initialized&&isClickedGetACard){
                        thirdCardOnHand.setVisible(true);
                        tooggleMain=true;
                        isClickedGetACard=false;
                        getResourceCardIndex_toServer= null;
                        getGoldCardIndex_toServer= null;
                        gameStatus.setTextFill(Color.WHITE);
                    }

                    // disabilito gioco carta
                    myGrid.setDisable(true);

                    playACard.setDisable(true);
                    setBack.setDisable(true);
                    disableCardOnHand();

                    // disabilito pesca carta
                    disableDecks();
                    getResourceCardIndex_toServer=null;
                    getGoldCardIndex_toServer=null;
                    getACard.setDisable(true);
                }else{
                    //se sono nel mio turno
                    //abilito gioco carta

                    myGrid.setDisable(false);

                    ableCardOnHand();
                    if(!isError_playCard&&initialized&& tooggleMain && isClickedPlayACard){
                        gameStatus.setText("CHOOSE A CARD ON DECKS");
                        disableAllGoldLights();
                        lightGold(deckBackground);


                        cardsOnHandImages.get(cardOnHandIndex_toServer).setVisible(true);
                        cardsOnHandImages.get(cardOnHandIndex_toServer).setEffect(null);
                        thirdCardOnHand.setImage(null);

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

                        myGrid.setDisable(true);

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
                updatePt();
                lightCurrentPlayer(model);
            }
        }
    }

    private void disableAllGoldLights() {
        deckBackground.setEffect(null);
        cardOnHandBackground.setEffect(null);
    }

    private void lightGold(ImageView imageView) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(255, 215, 0, 0.75));
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.5);
        imageView.setEffect(dropShadow);
    }

    private void lightCurrentPlayer(ViewModel model) {
        for (ImageView image : playerColorMap.values()){
            image.setEffect(null);
        }
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(255, 215, 0, 0.75));
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.5);
        playerColorMap.get(model.getCurrentPlayer().getPlayerID()).setEffect(dropShadow);
    }

    private void initPtPane() {
        paneMap = HashBiMap.create();
        paneMap.put(PlayerColor.RED,redPane);
        paneMap.put(PlayerColor.BLUE,bluePane);
        paneMap.put(PlayerColor.GREEN,greenPane);
        paneMap.put(PlayerColor.YELLOW,yellowPane);
        playerColorMap =HashBiMap.create();
        ArrayList<Player> players = getGuiApplication().getGui().getMyMatch().getPlayers();
        for (Player p : players){
            PlayerColor playerColor = p.getPlayerID();
            String nick = p.nickname;
            switch (playerColor){
                case RED -> {
                    p2nick.setText(nick);
                    nRed.setText(nick);
                    initColorEvent(red,nRed);
                    playerColorMap.put(PlayerColor.RED,red);
                    p2nick.setVisible(true);
                }
                case GREEN -> {
                    p4nick.setText(nick);
                    initColorEvent(green,nGreen);
                    nGreen.setText(nick);
                    p4nick.setVisible(true);
                    playerColorMap.put(PlayerColor.GREEN,green);
                }
                case BLUE -> {
                    initColorEvent(blue,nBlue);
                    p1nick.setText(nick);
                    nBlue.setText(nick);
                    p1nick.setVisible(true);
                    playerColorMap.put(PlayerColor.BLUE,blue);
                }
                default -> {
                    initColorEvent(yellow,nYellow);
                    nYellow.setText(nick);
                    p3nick.setText(nick);
                    p3nick.setVisible(true);
                    playerColorMap.put(PlayerColor.YELLOW,yellow);
                }
            }
        }

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

            myGrid.getChildren().remove(boardTmpImage);
            myGrid.getChildren().remove(boardTmpImageBack);


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
        decksImages.add(firstResourceCard);
        decksImages.add(secondResourceCard);
        decksImages.add(kingdomResourceDeck);
        decksImages.add(firstGoldCard);
        decksImages.add(secondGoldCard);
        decksImages.add(kingdomGoldDeck);
        cardsOnHandImages.add(firstCardOnHand);
        cardsOnHandImages.add(secondCardOnHand);
        cardsOnHandImages.add(thirdCardOnHand);
        backCardsOnHandImages_BLUE.add(firstCardOnHand_BLUE);
        backCardsOnHandImages_BLUE.add(secondCardOnHand_BLUE);
        backCardsOnHandImages_BLUE.add(thirdCardOnHand_BLUE);
        backCardsOnHandImages_RED.add(firstCardOnHand_RED);
        backCardsOnHandImages_RED.add(secondCardOnHand_RED);
        backCardsOnHandImages_RED.add(thirdCardOnHand_RED);
        backCardsOnHandImages_YELLOW.add(firstCardOnHand_YELLOW);
        backCardsOnHandImages_YELLOW.add(secondCardOnHand_YELLOW);
        backCardsOnHandImages_YELLOW.add(thirdCardOnHand_YELLOW);
        backCardsOnHandImages_GREEN.add(firstCardOnHand_GREEN);
        backCardsOnHandImages_GREEN.add(secondCardOnHand_GREEN);
        backCardsOnHandImages_GREEN.add(thirdCardOnHand_GREEN);
        disableVisibilityCardOnHand(backCardsOnHandImages_BLUE);
        disableVisibilityCardOnHand(backCardsOnHandImages_RED);
        disableVisibilityCardOnHand(backCardsOnHandImages_YELLOW);
        disableVisibilityCardOnHand(backCardsOnHandImages_GREEN);

    }

    private void visibleCardsOnHand(ArrayList <ImageView> arr){
        for(ImageView im: arr){
            im.setVisible(true);
        }
    }

    private void disableVisibilityCardOnHand(ArrayList <ImageView> arr){
        for(ImageView im: arr){
            im.setVisible(false);
        }
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
    private void setStackPt(StackPane pane,int point){
        pane.setDisable(false);
        pane.setVisible(true);
        ArrayList<PTPOSITION> ptPositions =  new ArrayList<>(List.of(PTPOSITION.values()));
        if (point>29)
            point=29;
        PTPOSITION pt = ptPositions.get(point) ;
        AnchorPane.setTopAnchor(pane,pt.y);
        AnchorPane.setLeftAnchor(pane,pt.x);
    }
    private void updatePt(){
        for (Player p  : getGuiApplication().getGui().getMyMatch().getPlayers()){
            setStackPt(paneMap.get(p.getPlayerID()), p.currentScore);

            PlayerColor playerColor = p.getPlayerID();
            String point = String.valueOf(p.currentScore) + "Pt";
            switch (playerColor){
                case RED -> {
                    p2nick1.setText(point);
                    p2nick1.setVisible(true);
                }
                case GREEN -> {
                    p4nick1.setText(point);
                    p4nick1.setVisible(true);
                }
                case BLUE -> {
                    p1nick1.setText(point);
                    p1nick1.setVisible(true);
                }
                default -> {
                    p3nick1.setText(point);
                    p3nick1.setVisible(true);
                }
            }

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

    public void closePt(ActionEvent event) {
        ptPane.setVisible(false);
        ptPane.setDisable(true);
    }

    public void openPt(ActionEvent event) {
        if(ptPane.isDisable()){
            ptPane.setVisible(true);
            ptPane.setDisable(false);
        }else{
            closePt(event);
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
        playerBoards = HashBiMap.create();

        playerGrids = HashBiMap.create();
        playerGrids.put(PlayerColor.BLUE, gridPane);
        playerGrids.put(PlayerColor.RED, gridPane2);
        playerGrids.put(PlayerColor.YELLOW, gridPane3);
        playerGrids.put(PlayerColor.GREEN, gridPane4);

        initDrawAreaEvent(firstResourceCard, rCard2);
        initDrawAreaEvent(secondResourceCard, rCard1);
        initDrawAreaEvent(kingdomResourceDeck, rDeckText);

        initDrawAreaEvent(firstGoldCard, gCard2);
        initDrawAreaEvent(secondGoldCard, gCard1);
        initDrawAreaEvent(kingdomGoldDeck, gDeckText);

        initDrawAreaEvent(firstTargetCard, cTarget2);
        initDrawAreaEvent(secondTargetCard, cTarget1);
        initDrawAreaEvent( backTargetCard, pTarget);


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

        firstCardOnHand.setCursor(Cursor.HAND);
        firstCardOnHand.setEffect(null);
        secondCardOnHand.setCursor(Cursor.HAND);
        secondCardOnHand.setEffect(null);
        thirdCardOnHand.setCursor(Cursor.HAND);
        thirdCardOnHand.setEffect(null);

        kingdomResourceDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if(toggleR2) {
                        isClickedDeck = true;
                        getResourceCardIndex_toServer = 2;
                        disableDecksEXCEPTone(kingdomResourceDeck);
                        light(kingdomResourceDeck);
                        getACard.setDisable(false);
                    }else{
                        isClickedDeck=false;
                        getResourceCardIndex_toServer =null;
                        ableDecks();
                        getACard.setDisable(true);
                        kingdomResourceDeck.setEffect(null);
                        gameStatus.setText("CHOOSE A CARD FROM DECKS");
                        gameStatus.setTextFill(Color.WHITE);
                    }
                    toggleR2=!toggleR2;
                }
            }

        });
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
                        getACard.setDisable(false);
                    }else{
                        getACard.setDisable(true);
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
                        getACard.setDisable(false);
                    }else {
                        getACard.setDisable(true);
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
        secondGoldCard.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if(toggleG2) {
                    getACard.setDisable(false);
                    isClickedDeck = true;
                    getGoldCardIndex_toServer = 1;
                    disableDecksEXCEPTone(secondGoldCard);
                    light(secondGoldCard);
                    gameStatus.setText("CLICK THE BUTTON -GET A CARD");
                    gameStatus.setTextFill(Color.WHITE);
                }else{
                    getACard.setDisable(true);
                    isClickedDeck=false;
                    getGoldCardIndex_toServer= null;
                    ableDecks();
                    secondGoldCard.setEffect(null);
                    gameStatus.setText("CHOOSE A CARD FROM DECKS");
                    gameStatus.setTextFill(Color.WHITE);
                }
                toggleG2=!toggleG2;
            }
        });
        secondResourceCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getButton() == MouseButton.PRIMARY) {
                    if(toggleR2) {
                        getACard.setDisable(false);
                        isClickedDeck = true;
                        getResourceCardIndex_toServer = 1;
                        disableDecksEXCEPTone(secondResourceCard);
                        light(secondResourceCard);
                        gameStatus.setText("CLICK THE BUTTON -GET A CARD");
                        gameStatus.setTextFill(Color.WHITE);
                    }else {
                        getACard.setDisable(true);
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
                        getACard.setDisable(false);
                    }else{
                        getACard.setDisable(true);
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
        firstGoldCard.setCursor(Cursor.HAND);
        firstGoldCard.setEffect(null);
        firstResourceCard.setCursor(Cursor.HAND);
        firstResourceCard.setEffect(null);
        secondGoldCard.setCursor(Cursor.HAND);
        secondGoldCard.setEffect(null);
        secondResourceCard.setCursor(Cursor.HAND);
        secondResourceCard.setEffect(null);
        kingdomGoldDeck.setCursor(Cursor.HAND);
        kingdomGoldDeck.setEffect(null);
        kingdomResourceDeck.setCursor(Cursor.HAND);
        kingdomResourceDeck.setEffect(null);
    }

    private void initDrawAreaEvent(ImageView firstTargetCard, TextField cTarget2) {
        firstTargetCard.setOnMouseEntered(event -> {
            cTarget2.setVisible(true);
            enableBigImg(firstTargetCard.getImage());
        });
        firstTargetCard.setOnMouseExited(event -> {
            cTarget2.setVisible(false);
            bigImgPane.setVisible(false);
        });
    }
    private void initColorEvent(ImageView color, TextField nick) {
       color.setOnMouseEntered(event -> {
            nick.setVisible(true);
        });
        color.setOnMouseExited(event -> {
            nick.setVisible(false);
        });
    }


    private void enableBigImg(Image image) {
        biggerImg.setImage(image);
        bigImgPane.setVisible(true);
    }

    @FXML
    public void exitWin(ActionEvent event) {
        winPane.setVisible(false);
        winPane.setDisable(true);
    }
}


