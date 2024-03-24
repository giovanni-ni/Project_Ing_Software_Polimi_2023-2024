package it.polimi.ingsw.module;

public enum TypeOfCard {
    RESOURCECARD(40,0,39),
    GOLDCARD(40,40,79),
    INITIALCARD(6,80,85),
    TARGETCARD(16,86,101),
    ;

    final int numOfCards;
    final int codeCardStart;

    final int codeCardEnd;

    TypeOfCard(int numOfCards, int codeCardStart, int codeCardEnd) {
        this.numOfCards = numOfCards;
        this.codeCardStart=codeCardStart;
        this.codeCardEnd=codeCardEnd;
    }
}
