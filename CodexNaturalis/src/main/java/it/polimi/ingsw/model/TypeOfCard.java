package it.polimi.ingsw.model;

/**
 * Enum representing the different types of cards in the game, along with their
 * respective range of codes and the number of cards for each type.
 */
public enum TypeOfCard {
    RESOURCECARD(40,1,40),
    GOLDCARD(40,41,80),
    INITIALCARD(6,81,86),
    TARGETCARD(16,87,102),
    ;

    final int numOfCards;
    final int codeCardStart;

    final int codeCardEnd;


    /**
     * Constructs a TypeOfCard enum with the specified number of cards and the start and end codes.
     *
     * @param numOfCards the number of cards of this type
     * @param codeCardStart the starting code for this type of card
     * @param codeCardEnd the ending code for this type of card
     */
    TypeOfCard(int numOfCards, int codeCardStart, int codeCardEnd) {
        this.numOfCards = numOfCards;
        this.codeCardStart=codeCardStart;
        this.codeCardEnd=codeCardEnd;
    }

    /**
     * Gets the ending code for this type of card.
     *
     * @return the ending code for this type of card
     */
    public int getCodeCardEnd() {
        return codeCardEnd;
    }

    /**
     * Gets the ending code for this type of card.
     *
     * @return the ending code for this type of card
     */
    public int getCodeCardStart() {
        return codeCardStart;
    }

    /**
     * Gets the number of cards of this type.
     *
     * @return the number of cards of this type
     */
    public int getNumOfCards() {
        return numOfCards;
    }
}
