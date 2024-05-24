package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.Message.ClientToServerMsg.CreateGameMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.playCardMessage;
import it.polimi.ingsw.model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
public class BoardController extends GenericSceneController {

    private ArrayList<ResourceCard> resourceDeck=new ArrayList<>();

    private ArrayList<GoldCard> goldDeck=new ArrayList<>();

    private ArrayList<TargetCard> targetDeck= new ArrayList<>();

    private ArrayList<TargetCard> commonTarget =new ArrayList<>();

    private ArrayList<ImageView> cardsOnHand;
    private HashMap<ImageView, Integer> searchCode;
    Boolean isClickedCardOnHand=false;
    Boolean isClickedBoard= false;
    Boolean toggle1= false, toggle2=false, toggle3= false;
    Integer cardOnHandIndex_toServer=null,
            getResourceCardIndex_toServer=null,
            getGoldCardIndex_toServer=null,
            putACardX_toServer=null,
            putACardY_toServer=null,
            code_toServer=null;
    Coordinate coo_toServer;

    @FXML
    ImageView cardOnHandBackground, boardBrown, firstCardOnHand, secondCardOnHand, thirdCardOnHand,
            deckBackground, pointTable, firstResourceCard, secondResourceCard, kingdomResourceDeck,
            firstGoldCard, secondGoldCard, kingdomGoldDeck, firstTargetCard, secondTargetCard, backTargetCard ;
    @FXML
    Label gameStatus;
    @FXML
    Button button, playACard, getACard;
    @FXML
    GridPane gridPane;

    private void setUpBoard(ArrayList<TargetCard> commonTarget)
    {
        Image myImage1 = new Image(getClass().getResourceAsStream("/images/view/playGround.jpg"));
        System.out.println("clicked");
        Image myImage2 =new Image(getClass().getResourceAsStream("/images/view/transparentTabCoh.png"));
        Image myImage3 =new Image(getClass().getResourceAsStream("/images/view/transparentTabDeck.png"));
        Image myImage4 =new Image(getClass().getResourceAsStream("/images/view/pointTableBottom.png"));
        int i = commonTarget.get(0).getIdCard();
        System.out.println(i);
        Image myImage6= new Image(getClass().getResourceAsStream("/images/cards/TargetCardFront("+commonTarget.get(0).getIdCard()+").jpg"));
        Image myImage7= new Image(getClass().getResourceAsStream("/images/cards/TargetCardFront("+commonTarget.get(1).getIdCard()+").jpg"));
        System.out.println("clicked");
        Image myImage8= new Image(getClass().getResourceAsStream("/images/cards/TargetBack.jpg"));
        boardBrown.setImage(myImage1);
        cardOnHandBackground.setImage(myImage2);
        deckBackground.setImage(myImage3);
        pointTable.setImage(myImage4);
        pointTable.setCursor(Cursor.HAND);
        firstTargetCard.setImage(myImage6);
        secondTargetCard.setImage(myImage7);
        backTargetCard.setImage(myImage8);

    }
    private void resetCardOnHand (){
        firstCardOnHand.setImage(null);
        secondCardOnHand.setImage(null);
        thirdCardOnHand.setImage(null);
    }
    @FXML
    public void init(ActionEvent e) throws IOException {
        CardParsing cp= new CardParsing();
        goldDeck = (ArrayList<GoldCard>) cp.loadGoldCards();
        targetDeck = (ArrayList<TargetCard>) cp.loadTargetCards();
        resourceDeck = (ArrayList<ResourceCard>) cp.loadResourceCards();
        commonTarget=(ArrayList<TargetCard>) cp.loadTargetCards();
        setUpBoard(commonTarget);
        showDecks(goldDeck,resourceDeck);
        setUpGridPane();
    }

    @FXML
   /* public void playCardBotton_toServer(ActionEvent e){
        String nickname=getGuiApplication().getGui().getMyMatch().getCurrentPlayer().getNickname();
        cardsOnHand.add(firstCardOnHand);
        cardsOnHand.add(secondCardOnHand);
        cardsOnHand.add(thirdCardOnHand);
        if(isClickedCardOnHand&&isClickedBoard){
            code_toServer=searchCode.get(cardsOnHand.get(cardOnHandIndex_toServer));
            getGuiApplication().getGui().notify(new playCardMessage(nickname, cardOnHandIndex_toServer,isFront, coo_toServer.getX(), coo_toServer.getY());
        }
    }

    */

    private void showCardOnHand(ArrayList<Card> cardOnHand){
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
                    firstCardOnHand.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                if(toggle1) {
                                    isClickedCardOnHand = true;
                                    cardOnHandIndex_toServer = 0;
                                    secondCardOnHand.setDisable(true);
                                    thirdCardOnHand.setDisable(true);
                                    //illumino
                                }
                                else{
                                    isClickedCardOnHand = false;
                                    cardOnHandIndex_toServer = null;
                                    secondCardOnHand.setDisable(false);
                                    thirdCardOnHand.setDisable(false);
                                    //disillumino
                                }
                                toggle1 =!toggle1 ;
                            }
                        }

                    });
            }else if(numCard==2){
                secondCardOnHand.setImage(myImage5);
                searchCode.put(secondCardOnHand, code);
                secondCardOnHand.setCursor(Cursor.HAND);
                secondCardOnHand.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if(toggle2) {
                                isClickedCardOnHand = true;
                                cardOnHandIndex_toServer = 1;
                                thirdCardOnHand.setDisable(true);
                                firstCardOnHand.setDisable(true);
                                //illumino
                            }else{
                                isClickedCardOnHand = false;
                                cardOnHandIndex_toServer = null;
                                thirdCardOnHand.setDisable(false);
                                firstCardOnHand.setDisable(false);
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
                thirdCardOnHand.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if(toggle3) {
                                isClickedCardOnHand = true;
                                firstCardOnHand.setDisable(true);
                                secondCardOnHand.setDisable(true);
                                cardOnHandIndex_toServer = 2;
                                //illumino
                            }else{
                                isClickedCardOnHand = false;
                                firstCardOnHand.setDisable(false);
                                secondCardOnHand.setDisable(false);
                                cardOnHandIndex_toServer = null;
                                //disillumino
                            }
                        }
                    }

                });
            }
            numCard++;
        }
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
                    image9= new Image(getClass().getResourceAsStream("/images/cards/AnimalsBackGold.jpg"));
                else if (goldDeck.get(i).getKingdom().equals(Elements.MUSHROOMS)) {
                    System.out.println("entrato");
                  image9= new Image(getClass().getResourceAsStream("/images/cards/MushroomBackGold.jpg"));
                }
                else if(goldDeck.get(i).getKingdom().equals(Elements.VEGETAL))
                    image9 =new Image(getClass().getResourceAsStream("/images/cards/VegetalBackGold.jpg"));

                if(resourceDeck.get(i).getKingdom().equals(Elements.INSECT))
                    image10= new Image(getClass().getResourceAsStream("/images/cards/InsectBack.jpg"));
                else if (resourceDeck.get(i).getKingdom().equals(Elements.ANIMALS))
                    image10= new Image(getClass().getResourceAsStream("/images/cards/AnimalsBack.jpg"));
                else if (resourceDeck.get(i).getKingdom().equals(Elements.MUSHROOMS)) {
                    image10 = new Image(getClass().getResourceAsStream("/images/cards/MushroomBack.jpg"));
                }
                else if(resourceDeck.get(i).getKingdom().equals(Elements.VEGETAL))
                    image10 =new Image(getClass().getResourceAsStream("/images/cards/VegetalBack.jpg"));
            }
            if(i==0){
                firstGoldCard.setImage(image9);
                firstGoldCard.setCursor(Cursor.HAND);
                firstGoldCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            isClickedCardOnHand=true;
                            getGoldCardIndex_toServer=0;
                        }
                    }

                });
                firstResourceCard.setImage(image10);
                firstResourceCard.setCursor(Cursor.HAND);
                firstResourceCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            isClickedCardOnHand=true;
                            getResourceCardIndex_toServer=0;
                        }
                    }

                });

            }else if(i==1){
                secondGoldCard.setImage(image9);
                secondGoldCard.setCursor(Cursor.HAND);
                secondGoldCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            isClickedCardOnHand=true;
                            getGoldCardIndex_toServer=1;
                        }
                    }

                });
                secondResourceCard.setImage(image10);
                secondResourceCard.setCursor(Cursor.HAND);
                secondResourceCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            isClickedCardOnHand=true;
                            getResourceCardIndex_toServer=1;
                        }
                    }

                });
            }else if(i==2){
                kingdomGoldDeck.setImage(image9);
                kingdomGoldDeck.setCursor(Cursor.HAND);
                kingdomGoldDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            isClickedCardOnHand=true;
                            getGoldCardIndex_toServer=2;
                        }
                    }

                });
                kingdomResourceDeck.setImage(image10);
                kingdomResourceDeck.setCursor(Cursor.HAND);
                kingdomGoldDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            isClickedCardOnHand=true;
                            getGoldCardIndex_toServer=2;
                        }
                    }

                });
            }
        }
    }
    private void setUpGridPane() {
        for(int i= 0; i<82; i++)
            for(int j=0; j<82; j++) {
                ImageView imageView = new ImageView();
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/view/heart.png")));
                imageView.setCursor(Cursor.HAND);
                imageView.setFitWidth(10);
                imageView.setFitHeight(10);
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            isClickedBoard=true;
                            //get Index
                            ImageView imageClicked = (ImageView) event.getSource();
                            StackPane stackPane= (StackPane) imageClicked.getParent();
                            Integer columnIndex = GridPane.getColumnIndex(stackPane);
                            Integer rowIndex = GridPane.getRowIndex(stackPane);

                            // Convertire gli indici null in 0 (in caso di celle in posizione 0,0)
                            putACardX_toServer = columnIndex == null ? 0 : columnIndex;
                            putACardY_toServer = rowIndex == null ? 0 : rowIndex;
                            // create message to server
                            coo_toServer.setX(putACardX_toServer);
                            coo_toServer.setY(putACardY_toServer);

                            //add cardOnhand pic
                            /*
                            if(isClickedCardOnHand){
                                gridPane.add()
                            }

                             */

                            //gridPane.add();

                        }
                    }

                });
                StackPane stackPane= new StackPane(imageView);
                stackPane.setAlignment(Pos.CENTER);
                gridPane.add(stackPane, i, j);
            }
    }
    private void clickCardOnHand(){
        boolean ok=true, player=false;
        for(int i = 0; i<cardsOnHand.size(); i++){
            ImageView im=cardsOnHand.get(i);
            im.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        isClickedBoard=true;
                    }
                }

            });
        }
        // please choose where to put it
    }

    private void clickBoard(){

    }

    private void chooseFrontOrBack(){

    }

    private void RemovePreviousImageview(){
        /*
        public Node removeNodeByRowColumnIndex(final int row,final int column,GridPane gridPane) {

            ObservableList<Node> childrens = gridPane.getChildren();
            for(Node node : childrens) {
                if(node instanceof ImageView && gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                    ImageView imageView=ImageView(node); // use what you want to remove
                    gridPane.getChildren().remove(imageView);
                    break;
                }
            }
        }
        */
    }

    private void askServerIfPossiblePutCard(){

    }

    @Override
    public void updateModel(){
        ViewModel model = getGuiApplication().getGui().getMyMatch();
        //todo update all the scene with the information of the model
    }

}
