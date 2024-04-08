package it.polimi.ingsw.model;

public enum FilePath {

    RESOURCECARDPATH("src/main/resources/json/ResourceCard.json"),
    GOLDCARDPATH("src/main/resources/json/GoldCard.json"),
    INITIALCARDPATH("src/main/resources/json/InitialCard.json"),

    COUNTTARGETCARDPATH("src/main/resources/json/CountTargetCard.json"),

    OBLIQUETARGETCARDPATH("src/main/resources/json/ObliqueTargetCard.json"),
    POSITIONGOALTARGETCARDPATH("src/main/resources/json/PositionGoalTarget.json");
   public final String value;


    FilePath(String value) {

        this.value = value;
    }
}
