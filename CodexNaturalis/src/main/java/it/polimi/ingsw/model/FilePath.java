package it.polimi.ingsw.model;


/**
 * Enumeration of the file paths of the cards json files
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
     * Constructor of the enum with the value of the path
     */
    FilePath(String value) {
        this.value = value;
    }
}
