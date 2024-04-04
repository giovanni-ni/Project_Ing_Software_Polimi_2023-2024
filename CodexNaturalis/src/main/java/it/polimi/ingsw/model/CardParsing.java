package it.polimi.ingsw.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardParsing {



    public List<ResourceCard> loadResourceCards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ResourceCard> cards;
        cards =objectMapper.readValue(new File(FilePath.RESOURCECARDPATH.value), new TypeReference<>() {
        });
        return cards;
    }
    public List<ResourceCard> loadResourceCardsGson(){
        String fileName= FilePath.RESOURCECARDPATH.value;
        Gson gson = new GsonBuilder().create();
        List<ResourceCard> resourceCardList = new ArrayList<>();

       ResourceCard[] jsonCard;
        JsonReader reader = new JsonReader(new InputStreamReader(this.getClass().getResourceAsStream(fileName)));
        jsonCard = gson.fromJson(reader, ResourceCard[].class);
        Collections.addAll(resourceCardList, jsonCard);
        return resourceCardList;
    }

    public CardParsing() {
    }
}
