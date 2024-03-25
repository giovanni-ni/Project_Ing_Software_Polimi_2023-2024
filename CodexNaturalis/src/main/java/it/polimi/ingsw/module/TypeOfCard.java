package it.polimi.ingsw.module;

public enum TypeOfCard {
    RESOURCECARD(40,1,40),
    GOLDCARD(40,41,80),
    INITIALCARD(6,81,86),
    TARGETCARD(16,87,102),
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