package it.polimi.ingsw.model;

public class CountTargetCard extends TargetCard{

    Elements[] elemRequired;

    public CountTargetCard(){

    }

    public CountTargetCard(Elements[] elemRequired) {
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

        if(this.elemRequired != null && this.elemRequired.length != 0) {
            for (Elements element : this.elemRequired) {
                if (!board.getCounterOfElements().containsKey(element)) {
                    return 0;
                }
                int count = board.getCounterOfElements().get(element);
                n = Math.min(n, count);
            }
        } else {
            return 0;
        }

        return n;
    }

    public int countPoint(Board board) {
        return super.getbasePoint() * checkGoal(board);
    }
}
