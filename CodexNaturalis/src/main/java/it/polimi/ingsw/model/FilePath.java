package it.polimi.ingsw.model;

public enum FilePath {

    RESOURCECARDPATH("src/main/resources/json/ResourceCard.json");

   public final String value;


    FilePath(String value) {

        this.value = value;
    }
}
