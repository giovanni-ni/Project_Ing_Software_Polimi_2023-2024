package it.polimi.ingsw.view.Gui.SceneControllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BoardController {
    @FXML
    ImageView cardOnHandBackground, boardBrown, firstCardOnHand, secondCardOnHand, thirdCardOnHand;

    public void displayImage()
    {
        Image myImage = new Image("playGround.png");
        Image myImage2 =new Image(".png");
        boardBrown.setImage(myImage);
        cardOnHandBackground.setImage(myImage2);

    }

}
