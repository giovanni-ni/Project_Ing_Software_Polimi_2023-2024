package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.*;

import javax.swing.text.Element;
import java.io.IOException;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.findCardById;

public class pathSearch {

    public static String getPathByCardID(int cardId, boolean isFront) throws IOException {
        String path = null;
        Object card =findCardById(cardId);
        if (card instanceof TargetCard){
            if (isFront){
                path = "/images/cards/TargetCardFront("+cardId+")";
            }else {
                path = "/images/cards/TargetBack";
            }
        }else if (card instanceof GoldCard){
            if (isFront){
                path = "/images/cards/GoldCardFront("+cardId+")";
            }else {
                Elements elements = ((GoldCard) card).getKingdom();
                switch (elements) {
                    case MUSHROOMS -> {
                        path = "/images/cards/MushroomBackGold";
                    }
                    case VEGETAL -> {
                        path = "/images/cards/VegetalBackGold";
                    }
                    case ANIMALS -> {
                        path = "/images/cards/AnimalBackGold";
                    }
                    default -> {
                        path = "/images/cards/InsectBackGold";
                    }
                }

            }

        }else if (card instanceof ResourceCard){

            if (isFront){
                path = "/images/cards/ResourceCardFront("+cardId+")";
            }else {
                Elements elements = ((ResourceCard)card).getKingdom();
                switch (elements) {
                    case MUSHROOMS -> {
                        path = "/images/cards/MushroomBack";
                    }
                    case VEGETAL -> {
                        path = "/images/cards/VegetalBack";
                    }
                    case ANIMALS -> {
                        path = "/images/cards/AnimalBack";
                    }
                    default -> {
                        path = "/images/cards/InsectBack";
                    }
                }

            }
        } else if (card instanceof InitialCard) {
            if (isFront){
               path= "/images/cards/InitialCardFront("+cardId+")";
            }else{
                path= "/images/cards/InitialCardBack("+cardId+")";
            }
        }
        return  path;
    }
    public static String getPathByCard(Card card) throws IOException {
        return getPathByCardID(card.getCode(),card.getIsFront());
    }
}
