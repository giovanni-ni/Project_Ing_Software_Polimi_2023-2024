package it.polimi.ingsw.model;

/**
 * Enumeration representing paths to JSON files for different types of cards in a game.
 * Each constant provides a specific path to a JSON file corresponding to its card type.
 */
public enum FilePath {

    RESOURCECARDPATH("/json/ResourceCard.json"),
    GOLDCARDPATH("/json/GoldCard.json"),
    INITIALCARDPATH("/json/InitialCard.json"),

    COUNTTARGETCARDPATH("/json/CountTargetCard.json"),

    OBLIQUETARGETCARDPATH("/json/ObliqueTargetCard.json"),
    POSITIONGOALTARGETCARDPATH("/json/PositionGoalTarget.json");
   public final String value;

    /**
     * Constructor to associate a file path with each enum constant.
     *
     * @param value the file path associated with the enum constant.
     */
    FilePath(String value) {

        this.value = value;
    }
}
