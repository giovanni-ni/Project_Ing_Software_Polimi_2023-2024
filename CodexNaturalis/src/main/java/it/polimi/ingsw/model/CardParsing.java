package it.polimi.ingsw.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public List<GoldCard> loadGoldCards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<GoldCard> cards;
        cards =objectMapper.readValue(new File(FilePath.GOLDCARDPATH.value), new TypeReference<>() {
        });
        return cards;
    }
    public List<InitialCard> loadInitialCards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<InitialCard> cards;
        cards =objectMapper.readValue(new File(FilePath.INITIALCARDPATH.value), new TypeReference<>() {
        });
        return cards;
    }
    public CardParsing() {
    }
}
