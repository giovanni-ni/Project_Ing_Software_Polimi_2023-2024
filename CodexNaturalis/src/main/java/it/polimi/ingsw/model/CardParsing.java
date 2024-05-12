package it.polimi.ingsw.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardParsing implements Serializable {



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
    public List<TargetCard> loadTargetCards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<ObliqueTargetCard> cards2;
        List<PositionGoalTarget> cards3;
        List<CountTargetCard> cards1;
        cards1=objectMapper.readValue(new File(FilePath.COUNTTARGETCARDPATH.value), new TypeReference<>() {
        });
        cards2 =objectMapper.readValue(new File(FilePath.OBLIQUETARGETCARDPATH.value), new TypeReference<>() {
        });
        cards3 =objectMapper.readValue(new File(FilePath.POSITIONGOALTARGETCARDPATH.value), new TypeReference<>() {
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
