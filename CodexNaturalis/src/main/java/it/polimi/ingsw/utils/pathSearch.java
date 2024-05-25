package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.*;

import javax.swing.text.Element;
import java.io.IOException;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.findCardById;

public class pathSearch {

    public static String getPathByCardID(int cardId, boolean isFront) throws IOException {
        String path = null;
        Object card =findCardById(cardId);
        if (card instanceof CountTargetCard || card instanceof ObliqueTargetCard || card instanceof PositionGoalTarget){
            if (isFront){
                path = "/images/cards/TargetCardFront("+cardId+").jpg";
            }else {
                path = "/images/cards/TargetBack.jpg";
            }
        }else if (card instanceof GoldCard){
            if (isFront){
                path = "/images/cards/GoldCardFront("+cardId+").jpg";
            }else {
                Elements elements = ((GoldCard) card).getKingdom();
                switch (elements) {
                    case MUSHROOMS -> {
                        path = "/images/cards/MushroomBackGold.jpg";
                    }
                    case VEGETAL -> {
                        path = "/images/cards/VegetalBackGold.jpg";
                    }
                    case ANIMALS -> {
                        path = "/images/cards/AnimalBackGold.jpg";
                    }
                    default -> {
                        path = "/images/cards/InsectBackGold.jpg";
                    }
                }

            }

        }else if (card instanceof ResourceCard){

            if (isFront){
                path = "/images/cards/ResourceCardFront("+cardId+").jpg";
            }else {
                Elements elements = ((ResourceCard)card).getKingdom();
                switch (elements) {
                    case MUSHROOMS -> {
                        path = "/images/cards/MushroomBack.jpg";
                    }
                    case VEGETAL -> {
                        path = "/images/cards/VegetalBack.jpg";
                    }
                    case ANIMALS -> {
                        path = "/images/cards/AnimalBack.jpg";
                    }
                    default -> {
                        path = "/images/cards/InsectBack.jpg";
                    }
                }

            }
        } else if (card instanceof InitialCard) {
            if (isFront){
               path= "/images/cards/InitialCardFront("+cardId+").jpg";
            }else{
                path= "/images/cards/InitialCardBack("+cardId+").jpg";
            }
        }
        return  path;
    }
    public static String getPathByCard(Card card) throws IOException {
        return getPathByCardID(card.getCode(),card.getIsFront());
    }
}