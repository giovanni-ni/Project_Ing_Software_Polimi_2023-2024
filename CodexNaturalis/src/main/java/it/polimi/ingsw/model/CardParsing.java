package it.polimi.ingsw.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardParsing implements Serializable {



    public List<ResourceCard> loadResourceCards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ResourceCard> cards;
        InputStream inputStream = getClass().getResourceAsStream(FilePath.RESOURCECARDPATH.value);
        cards =objectMapper.readValue(inputStream, new TypeReference<>() {
        });
        return cards;
    }

    public List<GoldCard> loadGoldCards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<GoldCard> cards;
        InputStream inputStream = getClass().getResourceAsStream(FilePath.GOLDCARDPATH.value);
        cards =objectMapper.readValue(inputStream, new TypeReference<>() {
        });
        return cards;
    }
    public List<InitialCard> loadInitialCards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<InitialCard> cards;
        InputStream inputStream = getClass().getResourceAsStream(FilePath.INITIALCARDPATH.value);
        cards =objectMapper.readValue(inputStream, new TypeReference<>() {
        });
        return cards;
    }
    public List<TargetCard> loadTargetCards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<ObliqueTargetCard> cards2;
        List<PositionGoalTarget> cards3;
        List<CountTargetCard> cards1;

        InputStream inputStream1 = getClass().getResourceAsStream(FilePath.COUNTTARGETCARDPATH.value);
        InputStream inputStream2 = getClass().getResourceAsStream(FilePath.OBLIQUETARGETCARDPATH.value);
        InputStream inputStream3 = getClass().getResourceAsStream(FilePath.POSITIONGOALTARGETCARDPATH.value);
        cards1=objectMapper.readValue(inputStream1, new TypeReference<>() {
        });
        cards2 =objectMapper.readValue(inputStream2, new TypeReference<>() {
        });
        cards3 =objectMapper.readValue(inputStream3, new TypeReference<>() {
        });

        List<TargetCard> cards = new ArrayList<>();
        cards.addAll(cards1);
        cards.addAll(cards2);
        cards.addAll(cards3);


        return cards;
    }
    public CardParsing() {
    }
}
