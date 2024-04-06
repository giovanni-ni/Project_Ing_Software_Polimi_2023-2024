package it.polimi.ingsw;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.*;

import java.io.IOException;
import java.util.ArrayList;
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

      /*  Map<CornerPosition, Elements> corners = new HashMap<>();
        corners.put(CornerPosition.UPRIGHT, Elements.MUSHROOMS);
        corners.put(CornerPosition.UPLEFT, Elements.MUSHROOMS);
        corners.put(CornerPosition.DOWNLEFT, Elements.MUSHROOMS);
        corners.put(CornerPosition.DOWNRIGHT, Elements.MUSHROOMS);
        ArrayList<Elements> elements = new ArrayList<>();
        elements.add(Elements.INSECT);
        elements.add(Elements.ANIMALS);
        InitialCard person = new InitialCard(81,false,corners,elements,corners,corners);


        // Create an ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();

        // Serialize the Person object to JSON
        String json = mapper.writeValueAsString(person);

        // Print the JSON string
        System.out.println(json);

        CardParsing cp = new CardParsing();
        List<InitialCard> c = cp.loadInitialCards();

        for(int i = 0; i < c.size(); i++) {
            System.out.println(c.get(i).getCode());
        }
*/
    }
}
