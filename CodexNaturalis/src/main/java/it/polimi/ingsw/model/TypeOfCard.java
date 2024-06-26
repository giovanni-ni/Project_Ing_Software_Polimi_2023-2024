package it.polimi.ingsw.model;

/**
 * Enumeration representing different types of cards in the game.
 * Each type has a range of card codes and a number of cards associated with it.
 */
public enum TypeOfCard {
    /**
     * Resource cards type.
     * Range of card codes: 1 to 40.
     * Number of cards: 40.
     */
    RESOURCECARD(40,1,40),

    /**
     * Gold cards type.
     * Range of card codes: 41 to 80.
     * Number of cards: 40.
     */
    GOLDCARD(40,41,80),

    /**
     * Initial cards type.
     * Range of card codes: 81 to 86.
     * Number of cards: 6.
     */
    INITIALCARD(6,81,86),

    /**
     * Target cards type.
     * Range of card codes: 87 to 102.
     * Number of cards: 16.
     */
    TARGETCARD(16,87,102),
    ;

    /**
     * The total number of cards in this type.
     */
    final int numOfCards;

    /**
     * The starting code for the range of card codes in this type.
     */
    final int codeCardStart;

    /**
     * The ending code (inclusive) for the range of card codes in this type.
     */
    final int codeCardEnd;

    // Constructor

    /**
     * Constructs a TypeOfCard enum with the specified number of cards and code range.
     *
     * @param numOfCards     the number of cards in this type
     * @param codeCardStart  the starting code for the range of card codes
     * @param codeCardEnd    the ending code (inclusive) for the range of card codes
     */
    TypeOfCard(int numOfCards, int codeCardStart, int codeCardEnd) {
        this.numOfCards = numOfCards;
        this.codeCardStart=codeCardStart;
        this.codeCardEnd=codeCardEnd;
    }

    // Methods

    /**
     * Gets the ending code (inclusive) for the range of card codes in this type.
     *
     * @return the ending code for the card range
     */
    public int getCodeCardEnd() {
        return codeCardEnd;
    }


    /**
     * Gets the starting code for the range of card codes in this type.
     *
     * @return the starting code for the card range
     */
    public int getCodeCardStart() {
        return codeCardStart;
    }

    /**
     * Gets the total number of cards in this type.
     *
     * @return the number of cards in this type
     */
    public int getNumOfCards() {
        return numOfCards;
    }
}
