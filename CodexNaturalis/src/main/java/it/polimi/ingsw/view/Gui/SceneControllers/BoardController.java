package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoardController extends GenericSceneController {

    private ArrayList<ResourceCard> resourceDeck=new ArrayList<>();

    private ArrayList<GoldCard> goldDeck=new ArrayList<>();

    private ArrayList<TargetCard> targetDeck= new ArrayList<>();

    private ArrayList<TargetCard> commonTarget =new ArrayList<>();

    @FXML
    ImageView cardOnHandBackground, boardBrown, firstCardOnHand, secondCardOnHand, thirdCardOnHand,
            deckBackground, pointTable, firstResourceCard, secondResourceCard, kingdomResourceDeck,
            firstGoldCard, secondGoldCard, kingdomGoldDeck, firstTargetCard, secondTargetCard, backTargetCard ;
    @FXML
    Label gameStatus;
    @FXML
    Button button;

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
            }else if(numCard==2){
                secondCardOnHand.setImage(myImage5);
            } else if (numCard==3){
                thirdCardOnHand.setImage(myImage5);
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
                firstResourceCard.setImage(image10);
            }else if(i==1){
                secondGoldCard.setImage(image9);
                secondResourceCard.setImage(image10);
            }else if(i==2){
                kingdomGoldDeck.setImage(image9);
                kingdomResourceDeck.setImage(image10);
            }
        }
    }
    @Override
    public void updateModel(){
        ViewModel model = getGuiApplication().getGui().getMyMatch();
        //todo update all the scene with the information of the model
    }



}
