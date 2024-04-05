package it.polimi.ingsw;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.CardParsing;
import it.polimi.ingsw.model.CornerPosition;
import it.polimi.ingsw.model.Elements;
import it.polimi.ingsw.model.ResourceCard;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {

        /*Map<CornerPosition, Elements> corners = new HashMap<>();
        corners.put(CornerPosition.UPRIGHT, Elements.MUSHROOMS);
        corners.put(CornerPosition.UPLEFT, Elements.MUSHROOMS);
        corners.put(CornerPosition.DOWNLEFT, Elements.MUSHROOMS);
        corners.put(CornerPosition.DOWNRIGHT, Elements.MUSHROOMS);


        ResourceCard person = new ResourceCard(1,false,corners, Elements.MUSHROOMS,5 );

        // Create an ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();

        // Serialize the Person object to JSON
        String json = mapper.writeValueAsString(person);

        // Print the JSON string
        System.out.println(json);*/

        /*CardParsing cp = new CardParsing();
        List<ResourceCard> c = cp.loadResourceCards();

        for(int i = 0; i < c.size(); i++) {
            System.out.println(c.get(i).getKingdom());
        }*/

    }
}
