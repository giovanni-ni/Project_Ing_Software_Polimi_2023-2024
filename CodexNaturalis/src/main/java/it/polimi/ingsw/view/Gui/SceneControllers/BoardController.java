package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoardController extends GenericSceneController {

    private ArrayList<ResourceCard> resourceDeck;

    private ArrayList<GoldCard> goldDeck;

    private ArrayList<TargetCard> targetDeck;

    private ArrayList<TargetCard> commonTarget;

    @FXML
    ImageView cardOnHandBackground, boardBrown, firstCardOnHand, secondCardOnHand, thirdCardOnHand,
            deckBackground, pointTable;
    @FXML
    Label gameStatus;


    public void setUpBoard()
    {
        Image myImage1 = new Image("playGround.png");
        Image myImage2 =new Image("transparentTabCoh.png");
        Image myImage3 =new Image("transparentTabDeck.png");
        Image myImage4 =new Image("pointTableBottom.png");

        boardBrown.setImage(myImage1);
        cardOnHandBackground.setImage(myImage2);
        deckBackground.setImage(myImage3);
        pointTable.setImage(myImage4);
    }



    private void showCardOnHand(ArrayList<Card> cardOnHand){
        for(Card c : cardOnHand) {
            if(c.isGoldCard()) {
                Image myImage5 = new Image("GoldCardFront (" + c.getCode() + ").png");
            }
            else {
                Image myImage5 = new Image("ResourceCardFront (" + c.getCode() + ").png");
            }
        }
    }
    private void showDecks(ArrayList<GoldCard> goldDeck, ArrayList<ResourceCard> resourceDeck,
                           ArrayList<TargetCard> commonTarget){

    }




}
