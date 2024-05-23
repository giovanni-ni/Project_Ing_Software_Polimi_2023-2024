package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BoardController extends GenericSceneController {

    private ArrayList<ResourceCard> resourceDeck=new ArrayList<>();

    private ArrayList<GoldCard> goldDeck=new ArrayList<>();

    private ArrayList<TargetCard> targetDeck= new ArrayList<>();

    private ArrayList<TargetCard> commonTarget =new ArrayList<>();

    private ArrayList<ImageView> cardsOnHand= new ArrayList<>();

    private boolean isClickedCardOnHand=false;
    private boolean isClickedBoard= false;
    private boolean deckCardClicked= false;

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

    private void showCardOnHand(ArrayList<Card> cardOnHand){
        Image myImage5;
        int numCard =1;
        for(Card c : cardOnHand) {
            if(c.isGoldCard()) {
                myImage5 = new Image(getClass().getResourceAsStream("/images/cards/GoldCardFront(" + c.getCode() + ").jpg"));
            }
            else {
                myImage5 = new Image(getClass().getResourceAsStream("/images/cards/ResourceCardFront(" + c.getCode() + ").jpg"));
            }
            if(numCard==1){
                firstCardOnHand.setImage(myImage5);
                firstCardOnHand.setCursor(Cursor.HAND);
                    firstCardOnHand.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                isClickedCardOnHand=true;
                            }
                        }

                    });
            }else if(numCard==2){
                secondCardOnHand.setImage(myImage5);
                secondCardOnHand.setCursor(Cursor.HAND);
                secondCardOnHand.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            isClickedCardOnHand=true;
                        }
                    }

                });
            } else if (numCard==3){
                thirdCardOnHand.setImage(myImage5);
                secondCardOnHand.setCursor(Cursor.HAND);
                thirdCardOnHand.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            isClickedCardOnHand=true;
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
                        }
                    }

                });

            }else if(i==1){
                secondGoldCard.setImage(image9);
                secondGoldCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            isClickedCardOnHand=true;
                        }
                    }

                });
                secondResourceCard.setImage(image10);
                secondResourceCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            isClickedCardOnHand=true;
                        }
                    }

                });
            }else if(i==2){
                kingdomGoldDeck.setImage(image9);
                kingdomGoldDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            isClickedCardOnHand=true;
                        }
                    }

                });
                kingdomResourceDeck.setImage(image10);
                kingdomGoldDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            isClickedCardOnHand=true;
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
                        }
                    }

                });
                StackPane stackPane= new StackPane(imageView);
                stackPane.setAlignment(Pos.CENTER);
                gridPane.add(stackPane, i, j);
            }
    }

    public void playACard(int indexCardOnHand, Coordinate coo,  boolean isFront){

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
