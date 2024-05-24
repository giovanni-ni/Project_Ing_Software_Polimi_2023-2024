package it.polimi.ingsw.Networking;

public enum DefaultPort {
    SOCKETPORT(4234),
    RMIPORT(1234);

    private final int number;
    DefaultPort(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
