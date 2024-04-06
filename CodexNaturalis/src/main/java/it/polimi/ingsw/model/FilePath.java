package it.polimi.ingsw.model;

public enum FilePath {

    RESOURCECARDPATH("src/main/resources/json/ResourceCard.json"),
    GOLDCARDPATH("src/main/resources/json/GoldCard.json"),
    INITIALCARDPATH("src/main/resources/json/InitialCard.json");

   public final String value;


    FilePath(String value) {

        this.value = value;
    }
}
