package it.polimi.ingsw.module;

public enum TypeOfCard {
    ResourceCard(40,0,39),
    GoldCard(40,40,79),
    InitialCard(6,80,85),
    TargetCard(16,86,101),
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
