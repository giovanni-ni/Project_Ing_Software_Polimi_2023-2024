package it.polimi.ingsw.model;

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

    public int getCodeCardEnd() {
        return codeCardEnd;
    }

    public int getCodeCardStart() {
        return codeCardStart;
    }

    public int getNumOfCards() {
        return numOfCards;
    }
}
