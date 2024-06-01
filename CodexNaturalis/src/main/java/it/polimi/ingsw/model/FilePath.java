package it.polimi.ingsw.model;

public enum FilePath {

    RESOURCECARDPATH("/json/ResourceCard.json"),
    GOLDCARDPATH("/json/GoldCard.json"),
    INITIALCARDPATH("/json/InitialCard.json"),

    COUNTTARGETCARDPATH("/json/CountTargetCard.json"),

    OBLIQUETARGETCARDPATH("/json/ObliqueTargetCard.json"),
    POSITIONGOALTARGETCARDPATH("/json/PositionGoalTarget.json");
   public final String value;


    FilePath(String value) {

        this.value = value;
    }
}
