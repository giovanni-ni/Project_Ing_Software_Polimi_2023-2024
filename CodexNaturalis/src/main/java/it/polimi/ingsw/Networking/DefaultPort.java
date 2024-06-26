package it.polimi.ingsw.Networking;
/**
 * Enumeration of default ports used for different network protocols.
 */
public enum DefaultPort {
    /**
     * Default port number for Socket-based communication.
     */
    SOCKETPORT(4234),

    /**
     * Default port number for RMI-based communication.
     */
    RMIPORT(1234);

    private final int number;
    /**
     * Constructs a DefaultPort enum with the specified port number.
     *
     * @param number the port number associated with the protocol
     */
    DefaultPort(int number) {
        this.number = number;
    }
    /**
     * Returns the port number associated with the enum value.
     *
     * @return the port number
     */
    public int getNumber() {
        return number;
    }
}
