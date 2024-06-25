package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.*;

import javax.swing.text.Element;
import java.io.IOException;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.findCardById;

public class pathSearch {
    /**
     * Get the card path by the card id and the front boolean
     * @param cardId : the card id of the card path
     * @param isFront : boolean of the card front or back status
     */
    public static String getPathByCardID(int cardId, boolean isFront)  {
        String path = null;
        Object card = null;
        try {
            card = findCardById(cardId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (card instanceof CountTargetCard || card instanceof ObliqueTargetCard || card instanceof PositionGoalTarget){
            if (isFront){
                path = "/images/cards/TargetCardFront("+cardId+").png";
            }else {
                path = "/images/cards/TargetBack.png";
            }
        }else if (card instanceof GoldCard){
            if (isFront){
                path = "/images/cards/GoldCardFront("+cardId+").png";
            }else {
                Elements elements = ((GoldCard) card).getKingdom();
                switch (elements) {
                    case MUSHROOMS -> {
                        path = "/images/cards/MushroomBackGold.png";
                    }
                    case VEGETAL -> {
                        path = "/images/cards/VegetalBackGold.png";
                    }
                    case ANIMALS -> {
                        path = "/images/cards/AnimalBackGold.png";
                    }
                    default -> {
                        path = "/images/cards/InsectBackGold.png";
                    }
                }

            }

        }else if (card instanceof ResourceCard){

            if (isFront){
                path = "/images/cards/ResourceCardFront("+cardId+").png";
            }else {
                Elements elements = ((ResourceCard)card).getKingdom();
                switch (elements) {
                    case MUSHROOMS -> {
                        path = "/images/cards/MushroomBack.png";
                    }
                    case VEGETAL -> {
                        path = "/images/cards/VegetalBack.png";
                    }
                    case ANIMALS -> {
                        path = "/images/cards/AnimalBack.png";
                    }
                    default -> {
                        path = "/images/cards/InsectBack.png";
                    }
                }

            }
        } else if (card instanceof InitialCard) {
            if (isFront){
               path= "/images/cards/InitialCardFront("+cardId+").png";
            }else{
                path= "/images/cards/InitialCardBack("+cardId+").png";
            }
        }
        return  path;
    }
    /**
     * Get the card path by the card
     * @param card : the card id of the card path
     */
    public static String getPathByCard(Card card) throws IOException {
        return getPathByCardID(card.getCode(),card.getIsFront());
    }

    /**
     * Get the  back card path by the card
     * @param card : the card id of the card path
     */
    public static String getPathByBackCard(Card card) throws IOException {
        return getPathByCardID(card.getCode(),false);
    }
}
