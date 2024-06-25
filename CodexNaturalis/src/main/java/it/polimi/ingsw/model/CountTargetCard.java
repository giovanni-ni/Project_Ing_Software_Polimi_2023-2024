package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
// todo javadoc
public class CountTargetCard extends TargetCard implements Serializable {

    Elements[] elemRequired;

    public CountTargetCard(){

    }

    public CountTargetCard(int idCard,int basePoint,boolean ifCommon, Elements[] elemRequired) {
        super(idCard,basePoint,ifCommon);

        this.elemRequired = elemRequired;
    }

    public Elements[] getElemRequired() {
        return elemRequired;
    }

    public void setElemRequired(Elements[] elemRequired) {
        this.elemRequired = elemRequired;
    }

    public int checkGoal(Board board) {

        int n = Integer.MAX_VALUE;

        Map<Elements, Integer> elementCounts = new HashMap<>();
        for (Elements element : this.elemRequired) {
            elementCounts.put(element, elementCounts.getOrDefault(element, 0) + 1);
        }

        for (Map.Entry<Elements, Integer> entry : elementCounts.entrySet()) {
            Elements element = entry.getKey();
            int countInMap = board.getCounterOfElements().containsKey(element) ? board.getCounterOfElements().get(element) : 0;
            int countInArray = entry.getValue();
            int setsPossible = countInMap / countInArray;
            n = Math.min(n, setsPossible);
        }

        return n;
    }

    public int countPoint(Board board) {
        return super.getbasePoint() * checkGoal(board);
    }
}
