package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.*;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;

public class ActionSuccessMsg extends GenericServerMessage{

    private final ViewModel model;

    public ActionSuccessMsg(Match match) {

        this.model = new ViewModel(match);
        for (ResourceCard card :match.getResourceDeck()){
            System.out.println(card.getCode());
        }
        for (Player player : match.getPlayers()){
            for (Card card : player.getCardOnHand()){
                System.out.println(card.getCode());
            }

        }
        for (Player pla: match.getPlayers()){
            print(pla.getNickname());
            for (Card c:pla.getCardOnHand()){
                print(c.getCode());
            }
        }
    }

    public ViewModel getModel() {
        return model;
    }

}
